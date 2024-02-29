package com.dsj.csp.manage.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.entity.AbilityApiReq;
import com.dsj.csp.manage.entity.AbilityApiResp;
import com.dsj.csp.manage.mapper.AbilityApiRespMapper;
import com.dsj.csp.manage.service.AbilityApiRespService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Transactional(propagation = Propagation.REQUIRED)
@Service
@RequiredArgsConstructor
public class AbilityApiRespServiceImpl extends ServiceImpl<AbilityApiRespMapper, AbilityApiResp>
        implements AbilityApiRespService {

    @Override
    public Boolean saveRespList(List<AbilityApiResp> respParams, Long apiId) {
        if (respParams==null){
            return true;
        }
        Set<String> paramNames = respParams.stream().map(AbilityApiResp::getRespName).collect(Collectors.toSet());
        if (respParams.size() != paramNames.size()){
            throw new BusinessException("新增接口异常: 接口响应参数存在重名");
        }
        respParams.stream().peek(req -> req.setApiId(apiId)).toList();
        return this.saveBatch(respParams);
    }
}
