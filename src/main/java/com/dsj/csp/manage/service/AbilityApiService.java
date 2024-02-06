package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.entity.AbilityApiEntity;

import java.util.List;

public interface AbilityApiService extends IService<AbilityApiEntity> {



    List<Long> getApiIds(String keyword);

}
