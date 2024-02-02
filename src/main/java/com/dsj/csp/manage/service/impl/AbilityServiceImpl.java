package com.dsj.csp.manage.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.mapper.AbilityMapper;
import com.dsj.csp.manage.service.AbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityServiceImpl extends ServiceImpl<AbilityMapper, AbilityEntity> implements AbilityService  {

    @Override
    public long countAbility(Integer status) {
        QueryWrapper<AbilityEntity> qw = new QueryWrapper<>();
        qw.lambda().eq(AbilityEntity::getStatus, status);
        return this.getBaseMapper().selectCount(qw);
    }

    @Override
    public long countAvailAbility() {
        QueryWrapper<AbilityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(AbilityEntity::getStatus, 3)
                .or()
                .eq(AbilityEntity::getStatus, 4);
        return this.getBaseMapper().selectCount(queryWrapper);
    }

    @Override
    public String auditAbility(AbilityAuditVO auditVO) {
        AbilityEntity ability = this.getBaseMapper().selectById(auditVO.getAbilityId());
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
        this.getBaseMapper().update(updateWrapper);
        // 审核成功反馈信息
        String auditMsg = auditVO.getFlag()==0 ? "审核撤回完毕!" :
                auditVO.getFlag()==1 ? "审核提交完毕, 等待审核..." :
                        auditVO.getFlag()==2 ? "审核不通过完毕!" :
                                auditVO.getFlag()==3 ? "审核通过完毕! 等待发布..." :
                                        auditVO.getFlag()==4 ? "能力发布完毕!" :
                                                "能力下线完毕!";
        return auditMsg;

    }

    @Override
    public Page<AbilityEntity> pageAbilitys(Long userId, String keyword, Date startTime, Date endTime, int current, int size) {
        LambdaQueryWrapper<AbilityEntity> qw = Wrappers.lambdaQuery();
        qw
                .eq(userId != null, AbilityEntity::getUserId, userId)
                .ge(Objects.nonNull(startTime), AbilityEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityEntity::getCreateTime, endTime)
                .and(keyword!=null && !"".equals(keyword), i -> i
                        .like(AbilityEntity::getAbilityName, keyword)
                        .or().like(AbilityEntity::getAbilityProvider, keyword)
                        .or().like(AbilityEntity::getAbilityDesc, keyword))
                .orderByDesc(AbilityEntity::getUpdateTime)
                .orderByAsc(AbilityEntity::getStatus);
        return this.page(new Page<>(current, size), qw);
    }

    @Override
    public List<Long> getAbilityIds(String keyword) {
        List<Long> ids = this.list(Wrappers.lambdaQuery(AbilityEntity.class)
                        .select(AbilityEntity::getAbilityId)
                        .like(AbilityEntity::getAbilityName, keyword))
                .stream().map(e->e.getAbilityId()).toList();
        return ids;
    }

    @Override
    public Map<Long, AbilityEntity> getAbilityMap(Collection<Long> ids) {
        List<AbilityEntity> abiltiys = this.list(Wrappers.lambdaQuery(AbilityEntity.class)
                .select(AbilityEntity::getAbilityId, AbilityEntity::getAbilityName, AbilityEntity::getAbilityDesc)
                .in(AbilityEntity::getAbilityId, ids));
        Map<Long, AbilityEntity> map = abiltiys.stream().collect(Collectors.toMap(ability->ability.getAbilityId(), ability->ability));
        return map;
    }


}
