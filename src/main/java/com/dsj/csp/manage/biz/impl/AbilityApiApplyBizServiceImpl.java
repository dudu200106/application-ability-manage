package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.biz.AbilityApiApplyBizService;
import com.dsj.csp.manage.dto.*;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.*;
import com.dsj.csp.manage.service.*;
import com.dsj.csp.manage.util.Sm2;
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
    public void saveApiApply(AbilityApiApplyEntity applyEntity, String accessToken) {
        // 判断是否已存在未提交/待审核/审核通过的接口申请记录
        long cnt =abilityApiApplyService.count(Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                .eq(AbilityApiApplyEntity::getAppId, applyEntity.getAppId())
                .eq(AbilityApiApplyEntity::getApiId,applyEntity.getApiId())
                .in(AbilityApiApplyEntity::getStatus, 0, 1, 2));
        if (cnt!=0){
            throw new BusinessException("操作无效！所选应用已保存或者申请过该能力接口");
        }
        // 判断调用接口是否已下线
        if (abilityApiService.getById(applyEntity.getApiId()).getStatus()!=4){
            throw new BusinessException("申请的接口已下线！");
        }
        UserApproveRequest userApproveRequest = userApproveService.identify(accessToken);
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
        AbilityApiApplyDTO resApply = new AbilityApiApplyDTO();
        BeanUtil.copyProperties(apply, resApply,true);
        resApply.setApi(api);
        resApply.setAbilityName(ability.getAbilityName());
        resApply.setCompanyName(user.getCompanyName());
        resApply.setGovName(user.getGovName());
        resApply.setAppName(app.getAppName());
        resApply.setReqParams(reqParams);
        resApply.setRespParams(respParams);
        return resApply;
    }


    public String auditApply(AbilityAuditVO auditVO) {
        AbilityApiApplyEntity apply = abilityApiApplyService.getById(auditVO.getApiApplyId());
        if (apply==null){
            throw new BusinessException("找不到该申请记录! 请刷新页面后重试...");
        }
        // 审核流程限制: 状态(0待提交 1待审核 2审核通过 3审核不通过 4已停用 )
        if ((auditVO.getFlag() == 0 && apply.getStatus() != 1)
                || (auditVO.getFlag() == 1 && apply.getStatus() != 0)
                || (auditVO.getFlag() == 2 && !(apply.getStatus() == 1 || apply.getStatus() == 4))
                || (auditVO.getFlag() == 3 && apply.getStatus() != 1)
                || (auditVO.getFlag() == 4 && apply.getStatus() != 2)) {
            throw new BusinessException("申请状态已发生改变! 请刷新页面后重试...");
        }
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiApplyEntity::getApiApplyId, auditVO.getApiApplyId());
        updateWrapper.set(AbilityApiApplyEntity::getStatus, auditVO.getFlag());
        updateWrapper.set(AbilityApiApplyEntity::getNote, auditVO.getNote());
        updateWrapper.set(AbilityApiApplyEntity::getUpdateTime, new Date());
        updateWrapper.set(AbilityApiApplyEntity::getApproveTime, new Date());
        abilityApiApplyService.update(updateWrapper);

        // 判断是否要生成一对密钥
        Long appId = apply.getAppId();
        ManageApplicationEntity app = manageApplicationService.getById(appId);
        // 如果申请的appId不存在
        if (app == null){
            throw new BusinessException("审核失败! 申请能力的appId不存在!");
        }
        String appSecretKey =  app.getAppSecret();
        String appAppKey =  app.getAppSecret();
        // 如果审核通过, 判断是否为应用生成密钥
        if (auditVO.getFlag() == 2 && ObjectUtil.isEmpty(appSecretKey) && ObjectUtil.isEmpty(appAppKey)){
            Map<String, String> sm2Map = Sm2.sm2Test();
            String appKey = sm2Map.get("publicEncode");
            String secretKey = sm2Map.get("privateEncode");
            Map<String, String> sm2Map2 = Sm2.sm2Test();
            String wgKey = sm2Map2.get("publicEncode");
            String wgSecre = sm2Map2.get("privateEncode");
            LambdaUpdateWrapper<ManageApplicationEntity> appUpdateWrapper
                    = Wrappers.lambdaUpdate(ManageApplicationEntity.class)
                    .eq(ManageApplicationEntity::getAppId, appId)
                    .set(ManageApplicationEntity::getAppKey, appKey)
                    .set(ManageApplicationEntity::getAppSecret, secretKey)
                    .set(ManageApplicationEntity::getAppWgKey, wgKey)
                    .set(ManageApplicationEntity::getAppWgSecret, wgSecre);
            manageApplicationService.update(appUpdateWrapper);
        }
        // 审核反馈信息
        String auditMsg = auditVO.getFlag()==0 ? "审核撤回完毕!" :
                auditVO.getFlag()==1 ? "审核提交完毕, 等待审核..." :
                        auditVO.getFlag()==2 ? "审核通过完毕!" :
                                auditVO.getFlag()==3 ? "审核不通过完毕!." :
                                        "停用完毕!";
        return auditMsg;
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
            ids = users.stream().map(e->e.getUserId()).collect(Collectors.toSet());
        }
        return ids;
    }

    @Override
    public Set<String> getAppIds(Long userId, String keyword) {
        Set<String> ids = manageApplicationService.list(Wrappers.lambdaQuery(ManageApplicationEntity.class)
                        .select(ManageApplicationEntity::getAppId)
                        .eq(userId!= null, ManageApplicationEntity::getAppUserId, userId)
                        .like(!ObjectUtil.isEmpty(keyword), ManageApplicationEntity::getAppName, keyword))
                .stream().map(e->e.getAppId()).collect(Collectors.toSet());
        return ids;
    }

    @Override
    public Page<AbilityApiApplyDTO> pageApiApply(Boolean onlySubmitted, Long appId, Long userId, Long abilityId, String keyword, Integer status, Date startTime, Date endTime, int current, int size) {
        // 1.构造分页条件
        LambdaQueryWrapper<AbilityApiApplyEntity> qw = Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                .eq(appId != null, AbilityApiApplyEntity::getAppId, appId)
                .eq(userId != null, AbilityApiApplyEntity::getUserId, userId)
                .eq(abilityId != null, AbilityApiApplyEntity::getAbilityId, abilityId)
                .eq(status != null, AbilityApiApplyEntity::getStatus, status)
                // 如果过滤未提交状态
                .notIn(onlySubmitted, AbilityApiApplyEntity::getStatus, 0)
                .ge(Objects.nonNull(startTime), AbilityApiApplyEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityApiApplyEntity::getCreateTime, endTime)
                // 排序
                .orderByDesc(AbilityApiApplyEntity::getCreateTime)
                .orderByAsc(AbilityApiApplyEntity::getStatus);

        // 关键字
        if (!ObjectUtil.isEmpty(keyword)){
            List<Long> abilityIds = abilityService.getAbilityIds(keyword.trim());
            Set<String> appIds = getAppIds(null, keyword.trim());
            Set<String> userIds = getUserIds(keyword.trim());
            qw.and(i -> i.like(AbilityApiApplyEntity::getApiName, keyword)
                    .or().like(AbilityApiApplyEntity::getNote, keyword)
                    .or().like(AbilityApiApplyEntity::getIllustrate, keyword)
                    .or().in(abilityIds.size()>0, AbilityApiApplyEntity::getAbilityId, abilityIds)
                    .or().in(appIds.size()>0, AbilityApiApplyEntity::getAppId, appIds)
                    .or().in(userIds.size()>0, AbilityApiApplyEntity::getUserId, userIds)
            );
        }
        // 主表分页, 并单表查询从表信息, 构造分页返回结果
        Page prePage = abilityApiApplyService.page(new Page<>(current, size), qw);
        if (prePage.getTotal()==0){
            return prePage;
        }
        // 2.查出必要的分页返回信息
        List<AbilityApiApplyEntity> records = prePage.getRecords();
//        // 接口表
        Set<Long> apiIds = records.stream().map(e->e.getApiId()).collect(Collectors.toSet());
        Map<Long, AbilityApiEntity> apiMap = SimpleQuery.keyMap(Wrappers.lambdaQuery(AbilityApiEntity.class)
                .in(AbilityApiEntity::getApiId, apiIds), AbilityApiEntity::getApiId);
        // 能力 查出能力名称
        Set<Long> abiltiyIds = records.stream().map(e->e.getAbilityId()).collect(Collectors.toSet());
        Map<Long, AbilityEntity> abilityMap = SimpleQuery.keyMap(Wrappers.lambdaQuery(AbilityEntity.class)
                .in(AbilityEntity::getAbilityId, abiltiyIds), AbilityEntity::getAbilityId);
        // 应用 查出应用名称
        Set<Long> appIds = records.stream().map(e->e.getAppId()).collect(Collectors.toSet());
        Map<String, ManageApplicationEntity> appMap = SimpleQuery.keyMap(Wrappers.lambdaQuery(ManageApplicationEntity.class)
                .in(ManageApplicationEntity::getAppId, appIds), ManageApplicationEntity::getAppId);
        // 用户 查出企业/政府名称
        Set<Long> userIds = records.stream().map(e->e.getUserId()).collect(Collectors.toSet());
        Map<String, UserApproveEntity> userMap = SimpleQuery.keyMap(Wrappers.lambdaQuery(UserApproveEntity.class)
                .in(UserApproveEntity::getUserId, userIds), UserApproveEntity::getUserId);
        // 3.构造返回的分页res
        Page newPage = new Page<>(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        List<AbilityApiApplyDTO> resRecords = records.stream().map(apply ->{
            AbilityApiApplyDTO applyDTO = new AbilityApiApplyDTO();
            BeanUtil.copyProperties(apply, applyDTO, true);
            applyDTO.setApiName(apiMap.get(apply.getApiId())==null ? null : apiMap.get(apply.getApiId()).getApiName());
            applyDTO.setApiDesc(apiMap.get(apply.getApiId())==null ? null : apiMap.get(apply.getApiId()).getApiDesc());
            applyDTO.setAbilityName(abilityMap.get(apply.getAbilityId())==null ? null : abilityMap.get(apply.getAbilityId()).getAbilityName());
            applyDTO.setAppName(appMap.get(apply.getAppId())==null ? null : appMap.get(apply.getAppId()).getAppName());
            applyDTO.setCompanyName(userMap.get(apply.getUserId() + "")==null ? null : userMap.get(apply.getUserId() + "").getCompanyName());
            applyDTO.setGovName(userMap.get(apply.getUserId() + "")==null ? null : userMap.get(apply.getUserId() + "").getGovName());
            return applyDTO;
        }).toList();
        newPage.setRecords(resRecords);
        return newPage;
    }
}
