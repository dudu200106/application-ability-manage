package com.dsj.csp.manage.biz;

import com.dsj.csp.manage.dto.AbilityApplyAuditVO;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import org.springframework.beans.BeanUtils;

public interface AbilityApplyBizService {
    public void saveAbilityApply(AbilityApplyVO applyVO);

    // 审核能力申请
    void auditApply(AbilityApplyAuditVO auditVO);
}
