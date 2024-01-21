package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.dto.AbilityApplyAuditVO;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.entity.AbilityApplyEntity;

public interface AbilityApplyService extends IService<AbilityApplyEntity> {

    // 保存能力使用申请
    void saveAbilityApply(AbilityApplyVO applyVO);

}
