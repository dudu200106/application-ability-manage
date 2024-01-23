package com.dsj.csp.manage.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AbilityApiReq;
import com.dsj.csp.manage.mapper.AbilityApiReqMapper;
import com.dsj.csp.manage.service.AbilityApiReqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(propagation = Propagation.REQUIRED)
@Service
@RequiredArgsConstructor
public class AbilityApiReqServiceImpl extends ServiceImpl<AbilityApiReqMapper, AbilityApiReq>
        implements AbilityApiReqService {

    @Override
    public Boolean saveReqList(List<AbilityApiReq> reqs, Long apiId) {
        reqs.stream().peek(req -> {
            req.setApiId(apiId);
        }).toList();
        return this.saveBatch(reqs);
    }
}
