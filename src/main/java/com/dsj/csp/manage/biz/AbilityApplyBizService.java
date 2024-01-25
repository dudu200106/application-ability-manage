package com.dsj.csp.manage.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.AbilityApplyAuditVO;
import com.dsj.csp.manage.dto.AbilityApplyDTO;
import com.dsj.csp.manage.dto.AbilityApplyQueryVO;
import com.dsj.csp.manage.dto.AbilityApplyVO;

import java.util.List;

public interface AbilityApplyBizService {
    public void saveAbilityApply(AbilityApplyVO applyVO);

    // 查询申请能力信息
    AbilityApplyDTO getApplyInfo(Long abilityApplyId);

    // 审核能力申请
    void auditApply(AbilityApplyAuditVO auditVO);

    Page pageApply(AbilityApplyQueryVO applyQueryVO);
}
