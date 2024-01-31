package com.dsj.csp.manage.service.impl;


import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.mapper.AbilityApiMapper;
import com.dsj.csp.manage.service.AbilityApiService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional(propagation = Propagation.REQUIRED)
@Service
@RequiredArgsConstructor
public class AbilityApiServiceImpl extends ServiceImpl<AbilityApiMapper, AbilityApiEntity> implements AbilityApiService  {


    @Override
    public String auditApi(AbilityAuditVO auditVO) {
        AbilityApiEntity api = this.getBaseMapper().selectById(auditVO.getApiId());
        if (api==null){
            throw new BusinessException("审核失败! 请刷新页面后重试...");
        }
        // 审核流程限制: 状态(0未提交 1待审核 2审核未通过 3未发布 4已发布 5已下线)
        if ((auditVO.getFlag() == 0 && api.getStatus() != 1)
                || (auditVO.getFlag() == 1 && api.getStatus() != 0)
                || (auditVO.getFlag() == 2 && api.getStatus() != 1)
                || (auditVO.getFlag() == 3 && api.getStatus() != 1)
                || (auditVO.getFlag() == 4 && !(api.getStatus() == 3 || api.getStatus() == 5))
                || (auditVO.getFlag() == 5 && api.getStatus() != 4)) {
            throw new BusinessException("审核失败! 请刷新页面后重试...");
        }
        LambdaUpdateWrapper<AbilityApiEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiEntity::getApiId, auditVO.getApiId());
        updateWrapper.set(AbilityApiEntity::getStatus, auditVO.getFlag());
        updateWrapper.set(AbilityApiEntity::getNote, auditVO.getNote());
        updateWrapper.set(AbilityApiEntity::getUpdateTime, DateTime.now());
        this.getBaseMapper().update(updateWrapper);
        // 审核成功反馈信息
        String auditMsg = auditVO.getFlag()==0 ? "审核撤回完毕!" :
                auditVO.getFlag()==1 ? "审核提交完毕, 等待审核..." :
                        auditVO.getFlag()==2 ? "审核不通过完毕!" :
                                auditVO.getFlag()==3 ? "审核通过完毕! 等待发布..." :
                                        auditVO.getFlag()==4 ? "接口发布完毕!" :
                                                "接口下线完毕!";
        return auditMsg;

    }
}
