package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.dto.AbilityApplyAuditVO;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.entity.AbilityApplyEntity;

import java.util.Set;

public interface AbilityApplyService extends IService<AbilityApplyEntity> {

    // 返回接口Id结合
    Set<Long> getApiIds(Long userId, Long appId, Long abilityId, String keyword);

    // 统计用户申请的接口数量
    long countUserApplyApi(String userId);

    // 统计用户申请的能力数
    long countUserApplyAbility(String userId);
}
