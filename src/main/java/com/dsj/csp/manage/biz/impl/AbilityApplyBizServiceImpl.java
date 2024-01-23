package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
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
import com.dsj.csp.manage.util.Sm2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


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
        BeanUtil.copyProperties(applyVO, applyEntity, true);
        // 1.获取应用名称/用户ID
        ManageApplicationEntity app = manageApplicationService.getById(applyVO.getAppId());
        applyEntity.setAppName(app.getAppName());
        applyEntity.setUserId(Long.parseLong(app.getAppUserId()));

        // 2.通过用户ID获取企业/政府名称
        UserApproveEntity userApproveEntity = userApproveService.getById(app.getAppUserId());
        applyEntity.setCompanyName(userApproveEntity.getCompanyName());
        applyEntity.setGovName(userApproveEntity.getGovName());

        // 3.通过能力ID获取能力名称/能力类型
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

        // 判断是否要生成一对密钥
        Long appId = abilityApplyService.getById(auditVO.getAbilityApplyId()).getAppId();
        ManageApplicationEntity app = manageApplicationService.getById(appId);
        // 如果申请的appId不存在
        if (app == null){
            return;
        }
        String appSecretKey =  app.getAppSecret();
        String appAppKey =  app.getAppSecret();
        // 如果审核结果不通过 或者应用的公钥私钥有一个不为空, 就不用生成密钥了
        if (auditVO.getFlag() != 1
                || (appSecretKey!=null && !"".equals(appSecretKey))
                || (appAppKey!=null && !"".equals(appAppKey))){
            return;
        }
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
}
