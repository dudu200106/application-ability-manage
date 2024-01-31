package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.biz.AbilityApiApplyBizService;
import com.dsj.csp.manage.dto.*;
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
    public void saveApiApply(AbilityApiApplyEntity applyEntity) {
        // 查询是否已存在未提交/待审核/审核通过的接口申请记录
        long cnt =abilityApiApplyService.count(Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                .eq(AbilityApiApplyEntity::getAppId, applyEntity.getAppId())
                .eq(AbilityApiApplyEntity::getApiId,applyEntity.getApiId())
                .in(AbilityApiApplyEntity::getStatus, 0, 1, 2));
        if (cnt!=0){
            throw new BusinessException("接口申请失败!! 请检查是否已存在未提交/待审核/审核通过状态的接口申请记录");
        }
        // 以下信息直接存入能力申请记录信息数据库, 方便查询
        ManageApplicationEntity app = manageApplicationService.getById(applyEntity.getAppId());
        AbilityEntity ability = abilityService.getById(applyEntity.getAbilityId());
        if (app==null || ability==null){
            throw new BusinessException("申请接口异常! 请确保相关的用户应用、能力数据信息正常!");
        }
        applyEntity.setAppName(app.getAppName());
//        applyEntity.setUserId(Long.parseLong(app.getAppUserId()));
        applyEntity.setAbilityName(ability.getAbilityName());
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
        UserApproveEntity user = userApproveService.getById(apply.getUserId());
        if (api==null || user==null){
            throw new BusinessException("申请接口状态异常! 请确保相关申请的应用、接口数据信息正常!");
        }
        List<AbilityApiReq> reqParams = abilityApiReqService.list(Wrappers.lambdaQuery(AbilityApiReq.class).eq(AbilityApiReq::getApiId, api.getApiId()));
        List<AbilityApiResp> respParams = abilityApiRespService.list(Wrappers.lambdaQuery(AbilityApiResp.class).eq(AbilityApiResp::getApiId, api.getApiId()));
        //构造返回能力申请信息DTO
        AbilityApiApplyDTO resApply = new AbilityApiApplyDTO();
        BeanUtil.copyProperties(apply, resApply,true);
        resApply.setApi(api);
        resApply.setReqParams(reqParams);
        resApply.setRespParams(respParams);
        resApply.setApi(api);
        resApply.setCompanyName(user.getCompanyName());
        resApply.setGovName(user.getGovName());
        return resApply;
    }


    public String auditApply(AbilityAuditVO auditVO) {
        AbilityApiApplyEntity apply = abilityApiApplyService.getById(auditVO.getApiApplyId());
        if (apply==null){
            throw new BusinessException("审核失败! 请刷新页面后重试...");
        }
        // 审核流程限制: 状态(0待提交 1待审核 2审核通过 3审核不通过 4已停用 )
        if ((auditVO.getFlag() == 0 && apply.getStatus() != 1)
                || (auditVO.getFlag() == 1 && apply.getStatus() != 0)
                || (auditVO.getFlag() == 2 && !(apply.getStatus() == 1 || apply.getStatus() == 4))
                || (auditVO.getFlag() == 3 && apply.getStatus() != 1)
                || (auditVO.getFlag() == 4 && apply.getStatus() != 2)) {
            throw new BusinessException("审核失败! 请刷新页面后重试...");
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
        Long appId = abilityApiApplyService.getById(auditVO.getApiApplyId()).getAppId();
        ManageApplicationEntity app = manageApplicationService.getById(appId);
        // 如果申请的appId不存在
        if (app == null){
            throw new BusinessException("审核失败! 申请能力的appId不存在!");
        }
        String appSecretKey =  app.getAppSecret();
        String appAppKey =  app.getAppSecret();
        // 如果审核结果不通过 或者应用的公钥私钥有一个不为空, 就不用生成密钥了
        if (auditVO.getFlag() == 1
                && (appSecretKey==null || "".equals(appSecretKey))
                && (appAppKey==null || "".equals(appAppKey))){
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
    public Page<AbilityApiApplyDTO> pageApiApply(Boolean onlySubmitted, Long appId, Long userId, Long abilityId, String keyword, Integer status, Date startTime, Date endTime, int current, int size) {
        // 分页条件构造器
        LambdaQueryWrapper<AbilityApiApplyEntity> qw = Wrappers.lambdaQuery();
        qw.eq(appId != null, AbilityApiApplyEntity::getAppId, appId)
                .eq(userId != null, AbilityApiApplyEntity::getUserId, userId)
                .eq(abilityId != null, AbilityApiApplyEntity::getAbilityId, abilityId)
                .eq(status != null, AbilityApiApplyEntity::getStatus, status)
                // 如果过滤未提交状态
                .notIn(onlySubmitted, AbilityApiApplyEntity::getStatus, 0)
                .ge(Objects.nonNull(startTime), AbilityApiApplyEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityApiApplyEntity::getCreateTime, endTime)
                // 关键字
                .and(keyword!=null && !"".equals(keyword),
                        i -> i.like(AbilityApiApplyEntity::getAbilityName, keyword)
                                .or().like(AbilityApiApplyEntity::getAppName, keyword)
                                .or().like(AbilityApiApplyEntity::getIllustrate, keyword))
                // 排序
                .orderByDesc(AbilityApiApplyEntity::getUpdateTime)
                .orderByAsc(AbilityApiApplyEntity::getStatus);

        // 主表分页, 并单表查询从表信息, 构造分页返回结果
        Page prePage = abilityApiApplyService.page(new Page<>(current, size), qw);
        if (prePage.getTotal()==0){
            return prePage;
        }
        List<AbilityApiApplyEntity> records = prePage.getRecords();
        // 接口表
        Set<Long> apiIds = records.stream().map(e->e.getApiId()).collect(Collectors.toSet());
        List<AbilityApiEntity> apis = abilityApiService.list(Wrappers.lambdaQuery(AbilityApiEntity.class)
                .select(AbilityApiEntity::getApiId, AbilityApiEntity::getApiName, AbilityApiEntity::getDescription)
                .in(AbilityApiEntity::getApiId, apiIds));
        Map<Long, AbilityApiEntity> apiMap = apis.stream().collect(Collectors.toMap(api -> api.getApiId(), api -> api));
        // 用户表 查出企业/政府名称
        Set<Long> userIds = records.stream().map(e->e.getUserId()).collect(Collectors.toSet());
        List<UserApproveEntity> users = userApproveService.list(Wrappers.lambdaQuery(UserApproveEntity.class)
                .select(UserApproveEntity::getUserId, UserApproveEntity::getCompanyName, UserApproveEntity::getGovName)
                .in(UserApproveEntity::getUserId, userIds));
        // 将ID映射到数据上, 方便查找使用
        Map<String, UserApproveEntity> userMap = users.stream().collect(Collectors.toMap(user -> user.getUserId(), user -> user));
        // 返回的分页res
        Page newPage = new Page<>(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        List<AbilityApiApplyDTO> resRecords = records.stream().map(apply ->{
            AbilityApiApplyDTO applyDTO = new AbilityApiApplyDTO();
            BeanUtil.copyProperties(apply, applyDTO, true);
            applyDTO.setCompanyName(userMap.get(apply.getUserId())==null ? null : userMap.get(apply.getAbilityId()).getCompanyName());
            applyDTO.setGovName(userMap.get(apply.getUserId())==null ? null : userMap.get(apply.getAbilityId()).getGovName());
            applyDTO.setApiName(apiMap.get(apply.getApiId())==null ? null : apiMap.get(apply.getApiId()).getApiName());
            applyDTO.setApiDesc(apiMap.get(apply.getApiId())==null ? null : apiMap.get(apply.getApiId()).getDescription());
            return applyDTO;
        }).toList();
        newPage.setRecords(resRecords);
        return newPage;
    }
}
