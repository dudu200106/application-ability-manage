package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.biz.AbilityBizService;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.dto.AbilityDTO;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityApplyService;
import com.dsj.csp.manage.service.AbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 功能说明：
 *
 * @author 蔡云
 * 2024/1/17
 */
@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityBizServiceImpl implements AbilityBizService {

    private final AbilityService abilityService;
    private final AbilityApiService abilityApiService;
    private final AbilityApplyService abilityApplyService;

    @Override
    public AbilityDTO getAbilityInfo(Long abilityId) {
        AbilityEntity ability = abilityService.getById(abilityId);
        AbilityDTO abilityDTO = new AbilityDTO();
        BeanUtil.copyProperties(ability, abilityDTO, true);
        List<AbilityApiEntity> apis = abilityApiService.list(
                Wrappers.lambdaQuery(AbilityApiEntity.class).eq(AbilityApiEntity::getAbilityId, abilityId)
        );
        abilityDTO.setApiList(apis);
        return abilityDTO;
    }

    @Override
    public Boolean removeAbilityByIds(String abilityIds) {
        List<Long> ids = Arrays.asList(abilityIds.split(",")).stream().map(id -> Long.parseLong(id.trim())).toList();
        long countRelateApply = abilityApplyService.count(Wrappers.lambdaUpdate(AbilityApplyEntity.class).
                in(AbilityApplyEntity::getAbilityId, ids)
                .and(apply->apply.eq(AbilityApplyEntity::getIsDelete, 0)));
        if (countRelateApply>0){
            throw new BusinessException("删除能力失败:能力还在被应用使用!");
        }
        Boolean delFlag = abilityService.removeBatchByIds(ids);
        Boolean apiDelFlag = abilityApiService.remove(Wrappers.lambdaUpdate(AbilityApiEntity.class).in(AbilityApiEntity::getAbilityId, ids));
        return delFlag && apiDelFlag;
    }

    @Override
    public long countUserApplyAbility(String userId) {
        LambdaQueryWrapper<AbilityApplyEntity> queryWrapper = Wrappers.lambdaQuery((AbilityApplyEntity.class))
                .eq(AbilityApplyEntity::getUserId, Long.parseLong(userId))
                .select(AbilityApplyEntity::getAbilityId);
        Set<Long> abilityIdSet = abilityApplyService.list(queryWrapper).stream().map(e-> e.getAbilityId()).collect(Collectors.toSet());
        return abilityIdSet.size();
    }

    @Override
    public String auditAbility(AbilityAuditVO auditVO) {
        AbilityEntity ability = abilityService.getById(auditVO.getAbilityId());
        if (ability==null){
            throw new BusinessException("审核失败! 请刷新页面后重试...");
        }
        // 审核流程限制: 状态(0未提交 1待审核 2审核未通过 3未发布 4已发布 5已下线)
        if ((auditVO.getFlag() == 0 && ability.getStatus() != 1)
                || (auditVO.getFlag() == 1 && ability.getStatus() != 0)
                || (auditVO.getFlag() == 2 && ability.getStatus() != 1)
                || (auditVO.getFlag() == 3 && ability.getStatus() != 1)
                || (auditVO.getFlag() == 4 && ability.getStatus() != 3)
                || (auditVO.getFlag() == 5 && ability.getStatus() != 4)) {
            throw new BusinessException("审核失败! 请刷新页面后重试...");
        }
        LambdaUpdateWrapper<AbilityEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityEntity::getAbilityId, auditVO.getAbilityId());
        updateWrapper.set(AbilityEntity::getStatus, auditVO.getFlag());
        updateWrapper.set(AbilityEntity::getNote, auditVO.getNote());
        updateWrapper.set(AbilityEntity::getUpdateTime, DateTime.now());
        abilityService.update(updateWrapper);
        // 审核成功反馈信息
        String auditMsg = auditVO.getFlag()==0 ? "审核撤回完毕!" :
                auditVO.getFlag()==1 ? "审核提交完毕, 等待审核..." :
                        auditVO.getFlag()==2 ? "审核不通过完毕!" :
                                auditVO.getFlag()==3 ? "审核通过完毕! 等待发布..." :
                                        auditVO.getFlag()==4 ? "能力发布完毕!" :
                                                "能力下线完毕!";
        return auditMsg;

    }


}
