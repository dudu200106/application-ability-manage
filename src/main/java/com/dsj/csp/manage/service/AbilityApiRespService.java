package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.AbilityApiReq;
import com.dsj.csp.manage.entity.AbilityApiResp;

import java.util.List;

public interface AbilityApiRespService extends IService<AbilityApiResp> {
    Boolean saveRespList(List<AbilityApiResp> respParams, Long apiId);

}
