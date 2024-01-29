package com.dsj.csp.manage.biz;

import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.dto.AbilityDTO;

/**
 * 功能说明：
 *
 * @author 蔡云
 * 2024/1/17
 */
public interface AbilityBizService {
    // 获取能力信息（包括接口列表）
    AbilityDTO getAbilityInfo(Long abilityApplyId);

    Boolean removeAbilityByIds(String abilityId);

    // 统计用户申请的能力数
    long countUserApplyAbility(String userId);

    // 审核能力注册
    String auditAbility(AbilityAuditVO auditVO);

}
