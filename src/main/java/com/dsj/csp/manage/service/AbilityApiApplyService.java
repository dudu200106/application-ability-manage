package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;

import java.util.Set;

public interface AbilityApiApplyService extends IService<AbilityApiApplyEntity> {

    /**
     * 根据条件查询申请通过的apiID集合
     * @param userId
     * @param appId
     * @param abilityId
     * @param keyword
     * @return 申请审核通过的apiID集合
     */
    Set<Long> getPassedApiIds(Long userId, Long appId, Long abilityId, String keyword);

    long countUserAbility(String userId);

    long countUserApi(String userId);
}
