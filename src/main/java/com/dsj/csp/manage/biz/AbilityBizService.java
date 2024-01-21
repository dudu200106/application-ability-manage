package com.dsj.csp.manage.biz;

import com.dsj.csp.manage.dto.AbilityLoginVO;

/**
 * 功能说明：
 *
 * @author 蔡云
 * 2024/1/17
 */
public interface AbilityBizService {
    Boolean saveAbility(AbilityLoginVO abilityLoginVO);

    void updateAbilityLogin(AbilityLoginVO abilityLogin);
}
