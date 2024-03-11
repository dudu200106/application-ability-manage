package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.common.enums.ApiStatusEnum;
import com.dsj.csp.common.enums.ApplyStatusEnum;
import com.dsj.csp.manage.biz.AbilityApiApplyBizService;
import com.dsj.csp.manage.biz.GatewayAdminBizService;
import com.dsj.csp.manage.dto.*;
import com.dsj.csp.manage.dto.convertor.AbilityApiApplyConvertor;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.*;
import com.dsj.csp.manage.mapper.ManageApplicationMapper;
import com.dsj.csp.manage.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AbilityApiApplyBizServiceImpl implements AbilityApiApplyBizService {

    private final AbilityApiApplyService abilityApiApplyService;
    private final ManageApplicationMapper manageApplicationMapper;
    private final UserApproveService userApproveService;
    private final AbilityService abilityService;
    private final AbilityApiService abilityApiService;
    private final AbilityApiReqService abilityApiReqService;
    private final AbilityApiRespService abilityApiRespService;
    private final GatewayAdminBizService gatewayAdminBizService;

    @Override
    public void saveApiApply(AbilityApiApplyEntity applyEntity, UserApproveRequest userApproveRequest) {
        // 判断调用接口是否已下线
        AbilityApiEntity apiEntity = abilityApiService.getById(applyEntity.getApiId());
        ApiStatusEnum apiStatus = ApiStatusEnum.of(apiEntity.getStatus());
        if (apiEntity ==null || apiStatus!= ApiStatusEnum.PUBLISHED){
            throw new BusinessException("申请的接口不存在或者已下线！");
        }
        if(isAppAppliedApi(applyEntity.getAppId(), applyEntity.getApiId())){
            throw new BusinessException("申请无效！所选应用已保存或者已经申请过该能力接口");
        }
        applyEntity.setUserId(Long.parseLong(userApproveRequest.getUserId()));
        applyEntity.setSubmitTime(new Date());
        abilityApiApplyService.save(applyEntity);
    }

    /**
     * app是否已经申请过该接口
     * @param appId
     * @param apiId
     * @return true:有 false:没有
     */
    public boolean isAppAppliedApi(Long appId, Long apiId) throws BusinessException{
        // 判断是否已存在未提交/待审核/审核通过的接口申请记录
        long cnt =abilityApiApplyService.count(Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                .eq(AbilityApiApplyEntity::getAppId, appId)
                .eq(AbilityApiApplyEntity::getApiId,apiId)
                // 状态0:未提交 1:待审核 2审核通过
                .in(AbilityApiApplyEntity::getStatus,
                        ApplyStatusEnum.NOT_SUBMIT.getCode(),
                        ApplyStatusEnum.WAIT_AUDIT.getCode(),
                        ApplyStatusEnum.PASSED.getCode()
                ));
        return cnt == 1;
    }

    @Override
    public AbilityApiApplyDTO getApplyInfo(Long apiApplyId) {
        AbilityApiApplyEntity apply = abilityApiApplyService.getById(apiApplyId);
        if (apply==null){
            throw new BusinessException("接口申请记录不存在!!请核实你的能力申请");
        }
        // 查询需要返回的申请的其他信息
        AbilityApiEntity api = abilityApiService.getById(apply.getApiId());
        AbilityEntity ability = abilityService.getById(apply.getAbilityId());
        ManageApplicationEntity app = manageApplicationMapper.selectById(apply.getAppId());
        UserApproveEntity user = userApproveService.getById(apply.getUserId());
        if (api==null || ability==null || user==null){
            throw new BusinessException("接口申请异常! 请确认申请相关的用户、应用、能力、接口是否存在!");
        }
        List<AbilityApiReq> reqParams = abilityApiReqService.list(Wrappers.lambdaQuery(AbilityApiReq.class).eq(AbilityApiReq::getApiId, api.getApiId()));
        List<AbilityApiResp> respParams = abilityApiRespService.list(Wrappers.lambdaQuery(AbilityApiResp.class).eq(AbilityApiResp::getApiId, api.getApiId()));
        //构造返回能力申请信息DTO
        AbilityApiApplyDTO resApply = AbilityApiApplyConvertor.INSTANCE.toDTO(apply);
        resApply.setApi(api);
        resApply.setApiName(api.getApiName());
        resApply.setAbilityName(ability.getAbilityName());
        resApply.setCompanyName(user.getCompanyName());
        resApply.setGovName(user.getGovName());
        resApply.setAppName(app.getAppName());
        resApply.setReqParams(reqParams);
        resApply.setRespParams(respParams);
        return resApply;
    }

    @Override
    public boolean auditApplyBatch(List<AbilityApiApplyEntity> applyEntities, Integer auditStatus, String note) {
        AtomicBoolean auditFlag = new AtomicBoolean(false);
        // 审核
        applyEntities.forEach(apply->{
            Long applyId = apply.getApiApplyId();
            auditFlag.set(switch (auditStatus) {
                case 0 -> auditWithdraw(applyId, note);
                case 1 -> auditSubmit(applyId, note);
                case 2 -> auditPass(applyId, note);
                case 3 -> auditNotPass(applyId, note);
                default -> auditStop(applyId, note);
            });
        });
        return auditFlag.get();
    }

    @Override
    public boolean auditWithdraw(Long applyId, String note) {
        AbilityApiApplyEntity validApply = checkApplyValid(applyId, ApplyStatusEnum.WAIT_AUDIT);
        if (validApply==null) {
            throw new BusinessException("只有'待审核'的申请才能撤回!请刷新页面后重试");
        }
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiApplyEntity::getApiApplyId, applyId);
        updateWrapper.set(AbilityApiApplyEntity::getStatus, ApplyStatusEnum.NOT_SUBMIT.getCode());
        return abilityApiApplyService.update(updateWrapper);
    }

    @Override
    public boolean auditSubmit(Long applyId, String note) {
        AbilityApiApplyEntity validApply = checkApplyValid(applyId, ApplyStatusEnum.NOT_SUBMIT);
        if (validApply==null) {
            throw new BusinessException("只有'未提交'的申请才能提交!请刷新页面后重试");
        }
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiApplyEntity::getApiApplyId, applyId);
        updateWrapper.set(AbilityApiApplyEntity::getStatus, ApplyStatusEnum.WAIT_AUDIT.getCode());
        updateWrapper.set(AbilityApiApplyEntity::getSubmitTime, new Date());
        return abilityApiApplyService.update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditPass(Long applyId, String note) {
        // 判断审核为申请审核通过还是启用操作
        AbilityApiApplyEntity validApply = checkApplyValid(applyId, ApplyStatusEnum.WAIT_AUDIT);
        if (validApply==null){
            validApply = checkApplyValid(applyId, ApplyStatusEnum.STOPPED);
            if (validApply==null){
                throw new BusinessException("只有'待审核'或者'停用'的申请才能审核通过!请刷新页面后重试");
            }
        }
        boolean flag = abilityApiApplyService.lambdaUpdate()
                .eq(AbilityApiApplyEntity::getApiApplyId, applyId)
                .set(AbilityApiApplyEntity::getStatus, ApplyStatusEnum.PASSED.getCode())
                .set(AbilityApiApplyEntity::getNote, note)
                .set(AbilityApiApplyEntity::getApproveTime, new Date())
                .update();
        // 远程调用网关接口新增申请
        if (!flag) {
            return false;
        }
        AbilityApiApplyEntity apply = abilityApiApplyService.getById(applyId);
        ManageApplicationEntity app = manageApplicationMapper.selectById(apply.getAppId());
        AbilityApiEntity api = abilityApiService.getById(apply.getApiId());
//        gatewayAdminBizService.addGatewayApp(app);
//        gatewayAdminBizService.addGatewayApi(api);
//        gatewayAdminBizService.addGatewayApply(apply);
        // 新增申请, 包括根据是否已存在传入应用和api而新增/修改
        return gatewayAdminBizService.saveApplyComplete(app, api, apply);

    }

    @Override
    public boolean auditNotPass(Long applyId, String note) {
        AbilityApiApplyEntity validApply = checkApplyValid(applyId, ApplyStatusEnum.WAIT_AUDIT);
        if (validApply==null) {
            throw new BusinessException("只有'待审核'的申请才能审核不通过!请刷新页面后重试");
        }
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiApplyEntity::getApiApplyId, applyId);
        updateWrapper.set(AbilityApiApplyEntity::getStatus, ApplyStatusEnum.NOT_PASSED.getCode());
        updateWrapper.set(AbilityApiApplyEntity::getNote, note);
        updateWrapper.set(AbilityApiApplyEntity::getApproveTime, new Date());
        return abilityApiApplyService.update(updateWrapper);
    }

    @Override
    public boolean auditStop(Long applyId, String note) {
        AbilityApiApplyEntity validApply = checkApplyValid(applyId, ApplyStatusEnum.PASSED);
        if (validApply==null) {
            throw new BusinessException("只有'审核通过'的申请才能停用!请刷新页面后重试");
        }
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiApplyEntity::getApiApplyId, applyId);
        updateWrapper.set(AbilityApiApplyEntity::getStatus, ApplyStatusEnum.STOPPED.getCode());
        updateWrapper.set(AbilityApiApplyEntity::getNote, note);
        updateWrapper.set(AbilityApiApplyEntity::getApproveTime, new Date());
        updateWrapper.set(AbilityApiApplyEntity::getSubmitTime, null);
        return abilityApiApplyService.update(updateWrapper);
    }

    /**
     * 审核申请操作前的判断, 判断审核操作的申请是否有效
     * @param applyId 申请id
     * @param targetPrevStatus 期待审核操作前的申请状态
     * @return 审核操作的申请是否有效
     */
    public AbilityApiApplyEntity checkApplyValid(Long applyId, ApplyStatusEnum targetPrevStatus){
        AbilityApiApplyEntity apply = abilityApiApplyService.getById(applyId);
        if (apply==null){
            throw new BusinessException("审核通过失败!找不到该申请记录!");
        }
        ManageApplicationEntity app = manageApplicationMapper.selectById(apply.getAppId());
        if (app==null){
            throw new BusinessException("审核通过失败!找不到申请所属的应用!");
        }
        AbilityApiEntity api = abilityApiService.getById(apply.getApiId());
        ApiStatusEnum apiStatus = ApiStatusEnum.of(api.getStatus());
        if (api==null || apiStatus != ApiStatusEnum.PUBLISHED){
            throw new BusinessException("申请的接口不存在,或者已下线！");
        }
        // 审核流程限制: 状态(0待提交 1待审核 2审核通过 3审核不通过 4已停用 )
        // 申请状态是否为期待状态
        return apply.getStatus()==targetPrevStatus.getCode() ? apply : null;
    }

    @Override
    public Set<String> getUserIds(String keyword) {
        Set<String> ids = new HashSet<>();
        if (!ObjectUtil.isEmpty(keyword)){
            List<UserApproveEntity> users = userApproveService.list(Wrappers.lambdaQuery(UserApproveEntity.class)
                    .select(UserApproveEntity::getUserId)
                    .like(UserApproveEntity::getUserName, keyword)
//                    .like(UserApproveEntity::getCompanyName, keyword)
                    .like(UserApproveEntity::getGovName, keyword));
            ids = users.stream().map(UserApproveEntity::getUserId).collect(Collectors.toSet());
        }
        return ids;
    }

    @Override
    public Set<String> getAppIds(Long userId, String keyword) {
        Set<String> ids = manageApplicationMapper.selectList(Wrappers.lambdaQuery(ManageApplicationEntity.class)
                        .select(ManageApplicationEntity::getAppId)
                        .eq(userId!= null, ManageApplicationEntity::getAppUserId, userId)
                        .like(!ObjectUtil.isEmpty(keyword), ManageApplicationEntity::getAppName, keyword))
                .stream().map(ManageApplicationEntity::getAppId).collect(Collectors.toSet());
        return ids;
    }

    @Override
    public void saveApiApplyBatch(List<AbilityApiApplyEntity> applyList, UserApproveRequest userApproveRequest) {
        // 判断调用接口列表中是否存在已下线的接口
        List<Long> apiIds = applyList.stream().map(AbilityApiApplyEntity::getApiId).toList();
        long cntApi = abilityApiService.lambdaQuery()
                .in(AbilityApiEntity::getApiId, apiIds)
                .ne(AbilityApiEntity::getStatus, ApiStatusEnum.PUBLISHED.getCode())
                .count();
        if (cntApi > 0){
            throw new BusinessException("批量申请的接口中存在已下线或者不存在的接口！");
        }
        // 判断应用是否已申请过接口
        Long appId = applyList.get(0).getAppId();
        long cntApply = abilityApiApplyService.count(Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                // 查询条件: 该应用下存在相同的Api申请, 且申请状态为未提交/待审核/审核通过
                .eq(AbilityApiApplyEntity::getAppId, appId)
                .in(AbilityApiApplyEntity::getApiId, apiIds)
                // 状态0:未提交 1:待审核 2审核通过
                .in(AbilityApiApplyEntity::getStatus,
                        ApplyStatusEnum.NOT_SUBMIT.getCode(),
                        ApplyStatusEnum.WAIT_AUDIT.getCode(),
                        ApplyStatusEnum.PASSED.getCode()));
        if (cntApply > 0){
            throw new BusinessException("申请无效！所选应用已保存或者已经申请过该能力接口");
        }
        // 对userId属性赋值
        applyList.stream().peek(apply -> {
            apply.setUserId(Long.parseLong(userApproveRequest.getUserId()));
        }).toList();
        abilityApiApplyService.saveBatch(applyList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteApiApplyByAppId(Long appId) {
        List<Long> applyIds = abilityApiApplyService.lambdaQuery()
                .eq(AbilityApiApplyEntity::getAppId, appId)
                .list()
                .stream().map(AbilityApiApplyEntity::getApiApplyId)
                .toList();
        boolean flag = abilityApiApplyService.lambdaUpdate()
                .eq(AbilityApiApplyEntity::getAppId, appId)
                .remove();
        // 同步网关数据, 禁用网关内的app并解绑申请
        if (flag){
            ManageApplicationEntity app = new ManageApplicationEntity();
            app.setAppId(appId + "");
            gatewayAdminBizService.cancelGatewayApp(app);
            gatewayAdminBizService.unbindBatchApply(applyIds);
        }
    }

    @Override
    public Page<AbilityApiApplyDTO> pageApiApply(Boolean onlySubmitted, Long appId, Long userId, Long abilityId,
                                                 String keyword, Integer status, Date startTime, Date endTime,
                                                 int current, int size) {
        // 1.构造分页条件, 查询主表分页
        LambdaQueryWrapper<AbilityApiApplyEntity> qw = Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                .eq(appId != null, AbilityApiApplyEntity::getAppId, appId)
                .eq(userId != null, AbilityApiApplyEntity::getUserId, userId)
                .eq(abilityId != null, AbilityApiApplyEntity::getAbilityId, abilityId)
                .eq(status != null, AbilityApiApplyEntity::getStatus, status)
                // 过滤未提交状态 ( 状态0: 未提交 )
                .notIn(onlySubmitted, AbilityApiApplyEntity::getStatus, 0)
                // 过滤用户中心的能力审核
                .ge(Objects.nonNull(startTime), AbilityApiApplyEntity::getSubmitTime, startTime)
                .le(Objects.nonNull(endTime), AbilityApiApplyEntity::getSubmitTime, endTime)
                // 排序
                .orderByDesc(AbilityApiApplyEntity::getSubmitTime)
                .orderByAsc(AbilityApiApplyEntity::getStatus);
        // 关键字
        if (!ObjectUtil.isEmpty(keyword)){
            List<Long> abilityIds = abilityService.getAbilityIds(keyword.trim());
            Set<String> appIds = getAppIds(null, keyword.trim());
            Set<String> userIds = getUserIds(keyword.trim());
            qw.and(i -> i.like(AbilityApiApplyEntity::getApiApplyId, keyword)
                    .or().like(AbilityApiApplyEntity::getApiName, keyword)
                    .or().like(AbilityApiApplyEntity::getNote, keyword)
                    .or().like(AbilityApiApplyEntity::getIllustrate, keyword)
                    .or().in(abilityIds.size()>0, AbilityApiApplyEntity::getAbilityId, abilityIds)
                    .or().in(appIds.size()>0, AbilityApiApplyEntity::getAppId, appIds)
                    .or().in(userIds.size()>0, AbilityApiApplyEntity::getUserId, userIds)
            );
        }
        // 主表分页, 并单表查询从表信息, 构造分页返回结果
        Page<AbilityApiApplyEntity> prePage = abilityApiApplyService.page(new Page<>(current, size), qw);
        if (prePage.getTotal()==0){
            return new Page<>(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        }
        // 2.单表查询 查出必要的分页返回信息(避免过多的分页)
        List<AbilityApiApplyEntity> records = prePage.getRecords();
        // 接口表
        Set<Long> apiIds = records.stream().map(AbilityApiApplyEntity::getApiId).collect(Collectors.toSet());
        Map<Long, AbilityApiEntity> apiMap = SimpleQuery.keyMap(Wrappers.lambdaQuery(AbilityApiEntity.class)
                .in(AbilityApiEntity::getApiId, apiIds), AbilityApiEntity::getApiId);
        // 能力 查出能力名称
        Set<Long> abilityIds = records.stream().map(AbilityApiApplyEntity::getAbilityId).collect(Collectors.toSet());
        Map<Long, AbilityEntity> abilityMap = SimpleQuery.keyMap(Wrappers.lambdaQuery(AbilityEntity.class)
                .in(AbilityEntity::getAbilityId, abilityIds), AbilityEntity::getAbilityId);
        // 应用 查出应用名称
        Set<Long> appIds = records.stream().map(AbilityApiApplyEntity::getAppId).collect(Collectors.toSet());
        Map<String, ManageApplicationEntity> appMap = SimpleQuery.keyMap(Wrappers.lambdaQuery(ManageApplicationEntity.class)
                .in(ManageApplicationEntity::getAppId, appIds), ManageApplicationEntity::getAppId);
        // 用户 查出企业/政府名称
        Set<Long> userIds = records.stream().map(AbilityApiApplyEntity::getUserId).collect(Collectors.toSet());
        Map<String, UserApproveEntity> userMap = SimpleQuery.keyMap(Wrappers.lambdaQuery(UserApproveEntity.class)
                .in(UserApproveEntity::getUserId, userIds), UserApproveEntity::getUserId);
        // 3.初始化返回的分页, 并为必要的返回属性赋值
        Page<AbilityApiApplyDTO> newPage = new Page<>(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        List<AbilityApiApplyDTO> resRecords = records.stream().map(apply ->{
            AbilityApiApplyDTO applyDTO = AbilityApiApplyConvertor.INSTANCE.toDTO(apply);
            applyDTO.setApiName(apiMap.getOrDefault(apply.getApiId(), new AbilityApiEntity()).getApiName());
            applyDTO.setApiDesc(apiMap.getOrDefault(apply.getApiId(), new AbilityApiEntity()).getApiDesc());
            applyDTO.setAbilityName(abilityMap.getOrDefault(apply.getAbilityId(), new AbilityEntity()).getAbilityName());
            applyDTO.setAppName(appMap.getOrDefault(apply.getAppId() + "", new ManageApplicationEntity()).getAppName());
            applyDTO.setCompanyName(userMap.getOrDefault(apply.getUserId() + "", new UserApproveEntity()).getCompanyName());
            applyDTO.setGovName(userMap.getOrDefault(apply.getUserId() + "", new UserApproveEntity()).getGovName());
            return applyDTO;
        }).toList();
        newPage.setRecords(resRecords);
        return newPage;
    }
}
