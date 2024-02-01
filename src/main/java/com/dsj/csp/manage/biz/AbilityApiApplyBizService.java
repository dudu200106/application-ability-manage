package com.dsj.csp.manage.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.AbilityApiApplyDTO;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;

import java.util.Date;

public interface AbilityApiApplyBizService {

    void saveApiApply(AbilityApiApplyEntity applyVO);

    // 获取接口申请信息
    AbilityApiApplyDTO getApplyInfo(Long apiApplyId);

    //分页获取接口申请列表
    Page<AbilityApiApplyDTO> pageApiApply(Boolean onlySubmitted, Long appId, Long userId, Long abilityId, String keyword, Integer status, Date startTime, Date endTime, int current, int size);

    // 审核接口申请
    String auditApply(AbilityAuditVO auditVO);

    // 分页获取接口列表
//    Page pageApiList(Boolean onlyPublished, Long userId, Long appId, Long abilityId, String keyword, int size, int current, Date startTime, Date endTime);

}
