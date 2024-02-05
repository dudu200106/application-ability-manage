package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.entity.AbilityEntity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
public interface AbilityService extends IService<AbilityEntity> {
    // 统计不同能力数量
    long countAbility(Integer status);

    // 统计可用能力数量
    long countAvailAbility();



    // 审核能力注册
    String auditAbility(AbilityAuditVO auditVO);

    Page<AbilityEntity> pageAbilitys(Long userId, String keyword, Date startTime, Date endTime, int current, int size);

    // 根据关键字查询查出匹配的能力ID集合
    List<Long> getAbilityIds(String keyword);

    // 根据abilityIds查询出主键(Long)-能力映射集合
    Map<Long, AbilityEntity> getAbilityMap(Collection<Long> collection);
}
