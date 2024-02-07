package com.dsj.csp.manage.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.AbilityApiApplyDTO;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;

import java.util.Date;
import java.util.Set;

public interface AbilityApiApplyBizService {

    void saveApiApply(AbilityApiApplyEntity applyVO, String accessToken);

    // 获取接口申请信息
    AbilityApiApplyDTO getApplyInfo(Long apiApplyId);

    //分页获取接口申请列表
    Page<AbilityApiApplyDTO> pageApiApply(Boolean onlySubmitted, Long appId, Long userId, Long abilityId, String keyword, Integer status, Date startTime, Date endTime, int current, int size);

    // 审核接口申请
    String auditApply(AbilityAuditVO auditVO);

    String auditWithdraw(Long applyId, String note);
    String auditSubmit(Long applyId, String note);
    String auditPass(Long applyId, String note);
    String auditNotPass(Long applyId, String note);
    String auditBlockUp(Long applyId, String note);

    Set<String> getUserIds(String keyword);

    Set<String> getAppIds(Long userId, String keyword);


}
