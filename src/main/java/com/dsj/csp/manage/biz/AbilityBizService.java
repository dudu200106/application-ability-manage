package com.dsj.csp.manage.biz;

import com.dsj.csp.manage.dto.AbilityApplyDTO;
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
}
