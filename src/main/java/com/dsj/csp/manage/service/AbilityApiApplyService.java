package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;

import java.util.Set;

public interface AbilityApiApplyService extends IService<AbilityApiApplyEntity> {

    /**
     * 根据条件获取， 接口申请审核通过的apiID集合
     * @param userId
     * @param appId
     * @param abilityId
     * @param keyword
     * @return 申请审核通过的apiID集合
     */
    Set<Long> getPassedApiIds(Long userId, Long appId, Long abilityId, String keyword);

    /**
     * 根据应用ID, 删除应用关联所有接口申请
     * @param appId 应用ID
     * @return
     */
    int deleteApiApplyByAppId(Long appId);

    long countUserAbility(String userId);

    long countUserApi(String userId);
}
