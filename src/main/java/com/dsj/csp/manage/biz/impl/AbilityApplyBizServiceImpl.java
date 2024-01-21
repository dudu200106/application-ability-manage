package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.csp.manage.biz.AbilityApplyBizService;
import com.dsj.csp.manage.dto.AbilityApplyAuditVO;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.service.AbilityApplyService;
import com.dsj.csp.manage.service.AbilityService;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.service.UserApproveService;
import com.dsj.csp.manage.util.Sm4;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityApplyBizServiceImpl implements AbilityApplyBizService {

    private final ManageApplicationService manageApplicationService;
    private final UserApproveService userApproveService;
    private final AbilityService abilityService;
    private final AbilityApplyService abilityApplyService;

    @Override
    public void saveAbilityApply(AbilityApplyVO applyVO) {
        AbilityApplyEntity applyEntity = new AbilityApplyEntity();
        BeanUtils.copyProperties(applyVO, applyEntity);
        // 1.获取应用名称和用户ID
        ManageApplicationEntity app = manageApplicationService.getById(applyVO.getAppId());
        applyEntity.setAppName(app.getAppName());
        applyEntity.setUserId(Long.parseLong(app.getAppUserId()));

        // 2.通过用户ID获取政府名称
        UserApproveEntity userApproveEntity = userApproveService.getById(app.getAppUserId());
        applyEntity.setGovName(userApproveEntity.getGovName());

        // 3.通过能力ID获取能力名称和能力类型
        AbilityEntity ability = abilityService.getById(applyVO.getAbilityId());
        applyEntity.setAbilityName(ability.getAbilityName());
        applyEntity.setAbilityType(ability.getAbilityName());
        abilityApplyService.save(applyEntity);
    }


    public void auditApply(AbilityApplyAuditVO auditVO) {
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApplyEntity::getAbilityApplyId, auditVO.getAbilityApplyId());
        updateWrapper.set(AbilityApplyEntity::getStatus, auditVO.getFlag());
        updateWrapper.set(AbilityApplyEntity::getNote, auditVO.getNote());
        updateWrapper.set(AbilityApplyEntity::getUpdateTime, DateTime.now());
        abilityApplyService.update(updateWrapper);

        // 判断是否生成APP Key 和 Secret Key
        ManageApplicationEntity app = manageApplicationService.getById(auditVO.getAppId());
//        Long appId = abilityApplyService.getOne()
        if (app == null){
            return;
        }
        String appSecretKey =  app.getAppSecret();
        String appAppKey =  app.getAppSecret();
        if (auditVO.getFlag() != 1
                || (appSecretKey!=null && !"".equals(appSecretKey))
                || (appAppKey!=null && !"".equals(appAppKey))){
            return;
        }
        String appKey = Sm4.sm();
        String secretKey = Sm4.sm();
        LambdaUpdateWrapper<ManageApplicationEntity> appUpdateWrapper = Wrappers.lambdaUpdate();
        // 设置更新条件，这里假设要更新 id 为 1 的记录
        appUpdateWrapper.eq(ManageApplicationEntity::getAppId, auditVO.getAppId());
        // 设置要更新的字段和值
        appUpdateWrapper.set(ManageApplicationEntity::getAppKey, appKey);
        appUpdateWrapper.set(ManageApplicationEntity::getAppSecret, secretKey);
        manageApplicationService.update(appUpdateWrapper);
    }
}
