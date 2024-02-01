package com.dsj.csp.manage.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.*;


public interface AbilityApplyBizService {
    public void saveAbilityApply(AbilityApplyVO applyVO);

    // 查询申请能力信息
    AbilityApplyDTO getApplyInfo(Long abilityApplyId);

    /**
     *  审核能力申请
     * @param auditVO
     * @return 审核反馈信息
     */
    String auditApply(AbilityAuditVO auditVO);

    Page pageApply(AbilityApplyQueryVO applyQueryVO);
}
