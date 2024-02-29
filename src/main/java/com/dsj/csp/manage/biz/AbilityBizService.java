package com.dsj.csp.manage.biz;

import com.dsj.csp.manage.entity.AbilityEntity;

import java.util.List;

/**
 * 功能说明：
 *
 * @author 蔡云
 * 2024/1/17
 */
public interface AbilityBizService {

    boolean removeAbilityBatch(List<AbilityEntity> abilityList);


    boolean removeAbility(AbilityEntity ability);
}
