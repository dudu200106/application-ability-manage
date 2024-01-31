package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;

import java.util.Set;

public interface AbilityApiApplyService extends IService<AbilityApiApplyEntity> {

    // 根据条件获取apiID集合
    Set<Long> getApiIds(Long userId, Long appId, Long abilityId, String keyword);



}
