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
    /**
     * 批量删除能力分类
     * @param abilityList
     * @return
     */
    boolean removeAbilityBatch(List<AbilityEntity> abilityList);

    /**
     * 批量删除能力分类
     * @param ability
     * @return
     */
    boolean removeAbility(AbilityEntity ability);
}
