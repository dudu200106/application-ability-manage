package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.dto.AbilityLoginVO;
import com.dsj.csp.manage.entity.AbilityEntity;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
public interface AbilityService extends IService<AbilityEntity> {

    void saveAbility(AbilityLoginVO abilityLoginVO);

    void updateAbilityLogin(AbilityLoginVO abilityLogin);

    // 统计不同能力数量
    long countAbility(Integer status);

    // 统计可用能力数量
    long countAvailAbility();


}
