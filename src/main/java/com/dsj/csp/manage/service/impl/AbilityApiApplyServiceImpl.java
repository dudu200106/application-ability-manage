package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.mapper.AbilityApiApplyMapper;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AbilityApiApplyServiceImpl extends ServiceImpl<AbilityApiApplyMapper, AbilityApiApplyEntity> implements AbilityApiApplyService  {

    @Override
    public Set<Long> getPassedApiIds(Long userId, Long appId, Long abilityId, String keyword) {
        return this.lambdaQuery()
                .eq(AbilityApiApplyEntity::getStatus, 2)
                .eq(userId!= null, AbilityApiApplyEntity::getUserId, userId)
                .eq(appId!= null, AbilityApiApplyEntity::getAppId, appId)
                .eq(abilityId!= null, AbilityApiApplyEntity::getAbilityId, abilityId)
                //接口申请信息关键字模糊查询
                .and(keyword!=null && !"".equals(keyword),
                        i -> i.like(AbilityApiApplyEntity::getAbilityName, keyword)
                                .or().like(AbilityApiApplyEntity::getAppName, keyword))
                .select(AbilityApiApplyEntity::getApiId)
                .list()
                .stream().map(e->e.getApiId()).collect(Collectors.toSet());
    }



    @Override
    public long countUserAbility(String userId) {
        Set<Long> abilityIds = this.list( Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                        .select(AbilityApiApplyEntity::getAbilityId)
                        .eq(AbilityApiApplyEntity::getUserId, userId)
                        .eq(AbilityApiApplyEntity::getStatus, 2))
                .stream().map(e->e.getAbilityId()).collect(Collectors.toSet());
        return abilityIds.size();
    }

    @Override
    public long countUserApi(String userId) {
        long cnt = this.count(Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                .eq(AbilityApiApplyEntity::getUserId, userId)
                .eq(AbilityApiApplyEntity::getStatus, 2));
        return cnt;
    }

}
