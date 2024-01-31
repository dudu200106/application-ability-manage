package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.mapper.AbilityApplyMapper;
import com.dsj.csp.manage.service.AbilityApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityApplyServiceImpl extends ServiceImpl<AbilityApplyMapper, AbilityApplyEntity> implements AbilityApplyService {

    @Override
    public Set<Long> getApiIds(Long userId, Long appId, Long abilityId, String keyword) {
        List<String> apiIdsList = this.getBaseMapper().selectList(Wrappers.lambdaQuery(AbilityApplyEntity.class)
                        .eq(userId!= null, AbilityApplyEntity::getUserId, userId)
                        .eq(appId!= null, AbilityApplyEntity::getAppId, appId)
                        .eq(abilityId!= null, AbilityApplyEntity::getAbilityId, abilityId)
                        .and(keyword!=null && !"".equals(keyword), i -> i.like(AbilityApplyEntity::getAbilityName, keyword))
                        .select(AbilityApplyEntity::getApiIds))
                .stream().map(e->e.getApiIds()).toList();
        // 分割去重得到apiId集合
        Set<Long> ids = new HashSet<>();
        apiIdsList.forEach(apiIds ->{
            ids.addAll(Arrays.asList(apiIds.split(",")).stream().map(e->Long.parseLong(e)).toList());
        });
        System.out.println("apiId集合:====================: " + ids.toString());
        return ids;
    }


    @Override
    public long countUserApplyApi(String userId) {
        List<AbilityApplyEntity> applyEntities = this.getBaseMapper().selectList(Wrappers.lambdaQuery(AbilityApplyEntity.class)
                .eq(AbilityApplyEntity::getUserId, Long.parseLong(userId))
                .select(AbilityApplyEntity::getApiIds));
        List<String> ids = applyEntities.stream().map(e-> e.getApiIds()).toList();
        AtomicLong cnt= new AtomicLong();
        ids.forEach(e -> {
            cnt.addAndGet(e.trim().split(",").length);
        });
        return cnt.get();
    }


    @Override
    public long countUserApplyAbility(String userId) {
        LambdaQueryWrapper<AbilityApplyEntity> queryWrapper = Wrappers.lambdaQuery((AbilityApplyEntity.class))
                .eq(AbilityApplyEntity::getUserId, Long.parseLong(userId))
                .select(AbilityApplyEntity::getAbilityId);
        Set<Long> abilityIdSet = this.getBaseMapper().selectList(queryWrapper).stream().map(e-> e.getAbilityId()).collect(Collectors.toSet());
        return abilityIdSet.size();
    }

    @Override
    public int deleteApplyByAppId(Long appId) {
        LambdaUpdateWrapper deleteUW = Wrappers.lambdaUpdate(AbilityApplyEntity.class)
                .eq(AbilityApplyEntity::getAppId, appId);
        return this.getBaseMapper().delete(deleteUW);
    }
}
