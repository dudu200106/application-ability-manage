package com.dsj.csp.manage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.mapper.AbilityMapper;
import com.dsj.csp.manage.service.AbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Service
@RequiredArgsConstructor
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
    public Page<AbilityEntity> pageAbilitys(Long userId, String keyword, Date startTime, Date endTime, int current, int size) {
        LambdaQueryWrapper<AbilityEntity> qw = Wrappers.lambdaQuery();
        qw
                .eq(userId != null, AbilityEntity::getUserId, userId)
                .ge(Objects.nonNull(startTime), AbilityEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityEntity::getCreateTime, endTime)
                .and(!ObjectUtil.isEmpty(keyword), i -> i
                        .like(AbilityEntity::getAbilityName, keyword)
                        .or().like(AbilityEntity::getAbilityProvider, keyword)
                        .or().like(AbilityEntity::getAbilityDesc, keyword))
                .orderByDesc(AbilityEntity::getCreateTime)
                .orderByAsc(AbilityEntity::getStatus);
        return this.page(new Page<>(current, size), qw);
    }

    @Override
    public List<Long> getAbilityIds(String keyword) {
        List<Long> ids = this.list(Wrappers.lambdaQuery(AbilityEntity.class)
                        .select(AbilityEntity::getAbilityId)
                        .like(AbilityEntity::getAbilityName, keyword.trim()))
                .stream().map(e->e.getAbilityId()).toList();
        return ids;
    }

    @Override
    public Map<Long, AbilityEntity> getAbilityMap(Collection<Long> ids) {
        if (ids.size()==0){
            return new HashMap<>();
        }
        List<AbilityEntity> abiltiys = this.list(Wrappers.lambdaQuery(AbilityEntity.class)
                .select(AbilityEntity::getAbilityId, AbilityEntity::getAbilityName, AbilityEntity::getAbilityDesc)
                .in(AbilityEntity::getAbilityId, ids));
        Map<Long, AbilityEntity> map = abiltiys.stream().collect(Collectors.toMap(ability->ability.getAbilityId(), ability->ability));
        return map;
    }


}
