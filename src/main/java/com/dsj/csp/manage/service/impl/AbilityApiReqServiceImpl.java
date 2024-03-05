package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.entity.AbilityApiReq;
import com.dsj.csp.manage.mapper.AbilityApiReqMapper;
import com.dsj.csp.manage.service.AbilityApiReqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AbilityApiReqServiceImpl extends ServiceImpl<AbilityApiReqMapper, AbilityApiReq>
        implements AbilityApiReqService {

    @Override
    public boolean saveReqList(List<AbilityApiReq> reqParams, Long apiId) {
        if (reqParams==null){
            return true;
        }
        Set<String> paramNames = reqParams.stream().map(AbilityApiReq::getReqName).collect(Collectors.toSet());
        if (reqParams.size() != paramNames.size()){
            throw new BusinessException("新增接口异常: 接口请求参数存在重名");
        }
        reqParams.stream().peek(req -> req.setApiId(apiId)).toList();
        return this.saveBatch(reqParams);
    }
}
