package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.biz.AbilityApiApplyBizService;
import com.dsj.csp.manage.dto.*;
import com.dsj.csp.manage.dto.convertor.AbilityApiApplyConvertor;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.*;
import com.dsj.csp.manage.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityApiApplyBizServiceImpl implements AbilityApiApplyBizService {

    private final AbilityApiApplyService abilityApiApplyService;
    private final ManageApplicationService manageApplicationService;
    private final UserApproveService userApproveService;
    private final AbilityService abilityService;
    private final AbilityApiService abilityApiService;
    private final AbilityApiReqService abilityApiReqService;
    private final AbilityApiRespService abilityApiRespService;

    @Override
    public void saveApiApply(AbilityApiApplyEntity applyEntity, UserApproveRequest userApproveRequest) {
        // 判断调用接口是否已下线
        AbilityApiEntity apiEntity = abilityApiService.getById(applyEntity.getApiId());
        if (apiEntity ==null || apiEntity.getStatus()!=4){
            throw new BusinessException("申请的接口不存在或者已下线！");
        }
        // 判断是否已存在未提交/待审核/审核通过的接口申请记录
        long cnt =abilityApiApplyService.count(Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                .eq(AbilityApiApplyEntity::getAppId, applyEntity.getAppId())
                .eq(AbilityApiApplyEntity::getApiId,applyEntity.getApiId())
                // 状态0:未提交 1:待审核 2审核通过
                .in(AbilityApiApplyEntity::getStatus, 0, 1, 2));
        if (cnt!=0){
            throw new BusinessException("申请无效！所选应用已保存或者已经申请过该能力接口");
        }
        applyEntity.setUserId(Long.parseLong(userApproveRequest.getUserId()));
        abilityApiApplyService.save(applyEntity);
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
        ManageApplicationEntity app = manageApplicationService.getById(apply.getAppId());
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
    public String auditApply(AbilityAuditVO auditVO) {
        // 审核
        Long applyId = auditVO.getApiApplyId();
        String note = auditVO.getNote();
        return switch (auditVO.getFlag()) {
            case 0 -> auditWithdraw(applyId, note);
            case 1 -> auditSubmit(applyId, note);
            case 2 -> auditPass(applyId, note);
            case 3 -> auditNotPass(applyId, note);
            default -> auditBlockUp(applyId, note);
        };
    }

    @Override
    public String auditWithdraw(Long applyId, String note) {
        boolean isValid = isApplyValid(applyId, 1);
        if (!isValid) {
            throw new BusinessException("只有'待审核'的申请才能撤回!请刷新页面后重试");
        }
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiApplyEntity::getApiApplyId, applyId);
        updateWrapper.set(AbilityApiApplyEntity::getStatus, 0);
        abilityApiApplyService.update(updateWrapper);
        return "提交完成!";
    }

    @Override
    public String auditSubmit(Long applyId, String note) {
        boolean isValid = isApplyValid(applyId, 0);
        if (!isValid) {
            throw new BusinessException("只有'未提交'的申请才能提交!请刷新页面后重试");
        }
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiApplyEntity::getApiApplyId, applyId);
        updateWrapper.set(AbilityApiApplyEntity::getStatus, 1);
        abilityApiApplyService.update(updateWrapper);
        return "提交完成!";
    }

    @Override
    public String auditPass(Long applyId, String note) {
        boolean isValid = isApplyValid(applyId, 1) || isApplyValid(applyId, 4);
        if (!isValid) {
            throw new BusinessException("只有'待审核'或者'停用'的申请才能审核通过!请刷新页面后重试");
        }
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiApplyEntity::getApiApplyId, applyId);
        updateWrapper.set(AbilityApiApplyEntity::getStatus, 2);
        updateWrapper.set(AbilityApiApplyEntity::getNote, note);
        updateWrapper.set(AbilityApiApplyEntity::getApproveTime, new Date());
        abilityApiApplyService.update(updateWrapper);

//        // 判断是否要生成一对密钥
//        Long appId = abilityApiApplyService.getById(applyId).getAppId();
//        ManageApplicationEntity app = manageApplicationService.getById(appId);
//        // 如果申请的appId不存在
//        if (app == null){
//            throw new BusinessException("审核失败! 申请能力的appId不存在!");
//        }
//        String appSecretKey =  app.getAppSecret();
//        String appAppKey =  app.getAppSecret();
//        // 如果审核通过, 判断是否为应用生成密钥
//        if (ObjectUtil.isEmpty(appSecretKey) && ObjectUtil.isEmpty(appAppKey)){
//            Map<String, String> sm2Map = Sm2.sm2Test();
//            String appKey = sm2Map.get("publicEncode");
//            String secretKey = sm2Map.get("privateEncode");
//            Map<String, String> sm2Map2 = Sm2.sm2Test();
//            String wgKey = sm2Map2.get("publicEncode");
//            String wgSecre = sm2Map2.get("privateEncode");
//            LambdaUpdateWrapper<ManageApplicationEntity> appUpdateWrapper
//                    = Wrappers.lambdaUpdate(ManageApplicationEntity.class)
//                    .eq(ManageApplicationEntity::getAppId, appId)
//                    .set(ManageApplicationEntity::getAppKey, appKey)
//                    .set(ManageApplicationEntity::getAppSecret, secretKey)
//                    .set(ManageApplicationEntity::getAppWgKey, wgKey)
//                    .set(ManageApplicationEntity::getAppWgSecret, wgSecre);
//            manageApplicationService.update(appUpdateWrapper);
//        }
        return "审核通过完毕!";
    }

    @Override
    public String auditNotPass(Long applyId, String note) {
        boolean isValid = isApplyValid(applyId, 1);
        if (!isValid) {
            throw new BusinessException("只有'待审核'的申请才能审核不通过!请刷新页面后重试");
        }
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiApplyEntity::getApiApplyId, applyId);
        updateWrapper.set(AbilityApiApplyEntity::getStatus, 3);
        updateWrapper.set(AbilityApiApplyEntity::getNote, note);
        updateWrapper.set(AbilityApiApplyEntity::getApproveTime, new Date());
        abilityApiApplyService.update(updateWrapper);
        return "审核不通过完毕!";
    }

    @Override
    public String auditBlockUp(Long applyId, String note) {
        boolean isValid = isApplyValid(applyId, 2);
        if (!isValid) {
            throw new BusinessException("只有'审核通过'的申请才能停用!请刷新页面后重试");
        }
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiApplyEntity::getApiApplyId, applyId);
        updateWrapper.set(AbilityApiApplyEntity::getStatus, 4);
        updateWrapper.set(AbilityApiApplyEntity::getNote, note);
        updateWrapper.set(AbilityApiApplyEntity::getApproveTime, new Date());
        abilityApiApplyService.update(updateWrapper);
        return "停用完毕!";
    }

    /**
     * 审核申请操作前的判断,
     * @param applyId 申请id
     * @param targetPrevStatus 期待的前任审核状态
     * @return 审核操作是否有效
     */
    public boolean isApplyValid(Long applyId, int targetPrevStatus){
        AbilityApiApplyEntity apply = abilityApiApplyService.getById(applyId);
        if (apply==null){
            throw new BusinessException("审核通过失败!找不到该申请记录!");
        }
        AbilityApiEntity api = abilityApiService.getById(apply.getApiId());
        if (api==null || api.getStatus()!=4){
            throw new BusinessException("申请的接口不存在,或者已下线！");
        }
        // 审核流程限制: 状态(0待提交 1待审核 2审核通过 3审核不通过 4已停用 )
        // 审核操作是否有效
        return apply.getStatus()==targetPrevStatus;
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
        Set<String> ids = manageApplicationService.list(Wrappers.lambdaQuery(ManageApplicationEntity.class)
                        .select(ManageApplicationEntity::getAppId)
                        .eq(userId!= null, ManageApplicationEntity::getAppUserId, userId)
                        .like(!ObjectUtil.isEmpty(keyword), ManageApplicationEntity::getAppName, keyword))
                .stream().map(ManageApplicationEntity::getAppId).collect(Collectors.toSet());
        return ids;
    }

    @Override
    public void saveApiApplyBatch(List<AbilityApiApplyEntity> applyList, UserApproveRequest userApproveRequest) {
        applyList.stream().peek(apply -> {
            // 判断调用接口列表中是否存在已下线的接口
            long cntApiNum = abilityApiService.count(Wrappers.lambdaQuery(AbilityApiEntity.class)
                    // 查询条件: 接口存在且状态为'已发布'
                    .eq(AbilityApiEntity::getApiId, apply.getApiId())
                    .eq(AbilityApiEntity::getStatus, 4));
            if (cntApiNum != 1){
                throw new BusinessException("批量申请的接口中存在已下线或者不存在的接口！");
            }
            // 判断应用是否存在Api相同的未提交/待审核/审核通过的申请记录
            long cntApplyNum = abilityApiApplyService.count(Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                    // 查询条件: 相同应用下 存在相同Api申请 且 申请状态为未提交/待审核/审核通过
                    .eq(AbilityApiApplyEntity::getAppId, apply.getAppId())
                    .in(AbilityApiApplyEntity::getApiId, apply.getApiId())
                    // 状态0:未提交 1:待审核 2审核通过
                    .in(AbilityApiApplyEntity::getStatus, 0, 1, 2));
            if (cntApplyNum != 0){
                throw new BusinessException("申请无效！所选应用已保存或者已经申请过该能力接口");
            }
            // 对userId属性赋值
            apply.setUserId(Long.parseLong(userApproveRequest.getUserId()));
        }).toList();
        abilityApiApplyService.saveBatch(applyList);
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
                .ge(Objects.nonNull(startTime), AbilityApiApplyEntity::getUpdateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityApiApplyEntity::getUpdateTime, endTime)
                // 排序
                .orderByDesc(AbilityApiApplyEntity::getCreateTime)
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
