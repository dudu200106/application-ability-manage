package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.dto.AbilityListDTO;
import com.dsj.csp.manage.dto.AbilityLoginVO;
import com.dsj.csp.manage.entity.AbilityEntity;

import java.util.List;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
public interface AbilityService extends IService<AbilityEntity> {

    // 查询所以能力目录
    List<AbilityListDTO> getAllAbilityList();

    void saveAbility(AbilityLoginVO abilityLoginVO);

    void saveAbilityApply(AbilityApplyVO applyVO);

    void updateAbilityLogin(AbilityLoginVO abilityLogin);

    void auditApply(Long abilityApplyId, Long appId, Integer flag);

//    // 审核应用注册是否通过
//    void auditAbility(Long abilityId);

    // 统计不同能力数量
    long countAbility(Integer status);

    // 统计可用能力数量
    long countAvailAbility();


}
