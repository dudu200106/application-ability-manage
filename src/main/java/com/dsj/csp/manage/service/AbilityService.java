package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.AbilityEntity;

import java.util.*;

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

    Page<AbilityEntity> pageAbilitys(Long userId, String keyword, Date startTime, Date endTime, int current, int size);

    // 根据关键字查询查出匹配的能力ID集合
    List<Long> getAbilityIds(String keyword);

    // 根据abilityIds查询出主键(Long)-能力映射集合
    Map<Long, AbilityEntity> getAbilitysMap(Collection<Long> collection);

    // 查出符合条件的abilityId集合
}
