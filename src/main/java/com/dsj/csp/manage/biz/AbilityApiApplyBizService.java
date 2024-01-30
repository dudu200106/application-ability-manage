package com.dsj.csp.manage.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.AbilityApiApplyDTO;
import com.dsj.csp.manage.dto.AbilityApplyAuditVO;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;

import java.util.Date;

public interface AbilityApiApplyBizService {

    void saveApiApply(AbilityApiApplyEntity applyVO);

    // 获取接口申请信息
    AbilityApiApplyDTO getApplyInfo(Long apiApplyId);

    //分页获取接口申请信息列表
    Page<AbilityApiApplyDTO> pageApply(Long appId, Long userId, Long abilityId, String keyword, Date startTime, Date endTime, int current, int size);

    // 审核接口申请
    String auditApply(AbilityAuditVO auditVO);

}
