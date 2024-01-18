package com.dsj.csp.manage.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.dto.AbilityApplyAuditVO;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.dsj.csp.manage.mapper.AbilityApplyMapper;
import com.dsj.csp.manage.service.AbilityApplyService;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.util.Sm4;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityApplyServiceImpl extends ServiceImpl<AbilityApplyMapper, AbilityApplyEntity> implements AbilityApplyService {

    @Autowired
    AbilityApplyMapper abilityApplyMapper;
    @Autowired
    private ManageApplicationService manageApplicationService;

    @Override
    public void saveAbilityApply(AbilityApplyVO applyVO) {
        AbilityApplyEntity applyEntity = new AbilityApplyEntity();
        BeanUtils.copyProperties(applyVO, applyEntity);
        abilityApplyMapper.insert(applyEntity);

    }

    @Override
    public void auditApply(AbilityApplyAuditVO auditVO) {
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApplyEntity::getAbilityApplyId, auditVO.getAbilityApplyId());
        updateWrapper.set(AbilityApplyEntity::getStatus, auditVO.getFlag());
        updateWrapper.set(AbilityApplyEntity::getNote, auditVO.getNote());
        updateWrapper.set(AbilityApplyEntity::getUpdateTime, DateTime.now());
        abilityApplyMapper.update(updateWrapper);

        // 判断是否生成APP Key 和 Secret Key
        ManageApplicationEntity app = manageApplicationService.getById(auditVO.getAppId());
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
