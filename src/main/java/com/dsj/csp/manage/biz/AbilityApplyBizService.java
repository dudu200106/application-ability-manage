package com.dsj.csp.manage.biz;

import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import org.springframework.beans.BeanUtils;

public interface AbilityApplyBizService {
    public void saveAbilityApply(AbilityApplyVO applyVO);
}
