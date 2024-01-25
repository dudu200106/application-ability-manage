package com.dsj.csp.manage.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AbilityApiResp;
import com.dsj.csp.manage.mapper.AbilityApiRespMapper;
import com.dsj.csp.manage.service.AbilityApiRespService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(propagation = Propagation.REQUIRED)
@Service
@RequiredArgsConstructor
public class AbilityApiRespServiceImpl extends ServiceImpl<AbilityApiRespMapper, AbilityApiResp>
        implements AbilityApiRespService {

    @Override
    public Boolean saveRespList(List<AbilityApiResp> resps, Long apiId) {
        resps.stream().peek(req -> {
            req.setApiId(apiId);
        }).toList();
        return this.saveBatch(resps);
    }
}
