package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.mapper.AbilityApiApplyMapper;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityApiApplyServiceImpl extends ServiceImpl<AbilityApiApplyMapper, AbilityApiApplyEntity> implements AbilityApiApplyService  {

    @Override
    public Set<Long> getApiIds(Long userId, Long appId, Long abilityId, String keyword) {
        List<Long> apiIdsList = this.getBaseMapper().selectList(Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                        .eq(userId!= null, AbilityApiApplyEntity::getUserId, userId)
                        .eq(appId!= null, AbilityApiApplyEntity::getAppId, appId)
                        .eq(abilityId!= null, AbilityApiApplyEntity::getAbilityId, abilityId)
                        .and(keyword!=null && !"".equals(keyword),i -> i
                                .like(AbilityApiApplyEntity::getAbilityName, keyword)
                                .like(AbilityApiApplyEntity::getAppName, keyword))
                        .select(AbilityApiApplyEntity::getApiId))
                .stream().map(e->e.getApiId()).toList();
        // 分割去重得到apiId集合
        Set<Long> ids = new HashSet<>(apiIdsList);
        System.out.println("apiId集合:====================================: " + ids.toString());
        return ids;
    }

}
