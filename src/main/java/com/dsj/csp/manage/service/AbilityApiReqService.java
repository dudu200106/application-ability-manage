package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.AbilityApiReq;

import java.util.List;

public interface AbilityApiReqService extends IService<AbilityApiReq> {
    Boolean saveReqList(List<AbilityApiReq> reqs, Long apiId);
}
