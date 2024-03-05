package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.AbilityApiReq;

import java.util.List;

public interface AbilityApiReqService extends IService<AbilityApiReq> {
    boolean saveReqList(List<AbilityApiReq> reqParams, Long apiId);
}
