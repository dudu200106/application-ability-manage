package com.dsj.csp.manage.biz.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dsj.common.dto.BusinessException;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.consts.GatewayCryptKeyConst;
import com.dsj.csp.manage.biz.GatewayAdminBizService;
import com.dsj.csp.manage.dto.gateway.ApiHandleVO;
import com.dsj.csp.manage.dto.gateway.CryptJsonBody;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.dsj.csp.manage.service.ApiFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-03-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayAdminBizImplService implements GatewayAdminBizService {

    private final ApiFeignService apiFeignService;
    @Override
    public boolean addGatewayApp(ManageApplicationEntity appEntity) {
        return false;
    }

    @Override
    public boolean cancelGatewayApp(ManageApplicationEntity appEntity) {
        return false;
    }

    @Override
    public boolean addGatewayApi(AbilityApiEntity apiEntity) {
        // 要传递的JSON数据
        HashMap<String, String> map = new HashMap<>();
        map.put("apiExtId", apiEntity.getApiId()+"");
        map.put("apiName", apiEntity.getApiName());
        map.put("apiPath", apiEntity.getApiUrl());
        map.put("sourceCode", "common-support");
        // 加密
        CryptJsonBody cryptJsonBody = CryptJsonBody.encryptObj(map, GatewayCryptKeyConst.SERVER_PUBLIC);
        Result<CryptJsonBody> result = apiFeignService.addApi(cryptJsonBody);
        if (result.getCode()!=2000){
            throw new BusinessException("远程调用添加网关api接口出错！");
        }
        // 响应解密
        CryptJsonBody data  = result.getData();
        ApiHandleVO apiHandleVO = CryptJsonBody.decryptToObj(data, GatewayCryptKeyConst.CLIENT_PRIVATE, ApiHandleVO.class);
        // 打印日志.
        log.info(JSONUtil.toJsonStr(apiHandleVO));
        return true;
    }

    @Override
    public boolean cancelGatewayApi(AbilityApiEntity apiEntity) {
        // 要传递的JSON数据
        HashMap<String, String> map = new HashMap<>();
        map.put("apiExtId", apiEntity.getApiId()+"");
        // 加密
        CryptJsonBody cryptJsonBody = CryptJsonBody.encryptObj(map, GatewayCryptKeyConst.SERVER_PUBLIC);
        Result<CryptJsonBody> result = apiFeignService.cancelApi(cryptJsonBody);
        if (result.getCode() != 2000){
            throw new BusinessException("远程调用禁用网关api接口失败！");
        }
        // 响应解密
        CryptJsonBody data  = result.getData();
        ApiHandleVO apiHandleVO = CryptJsonBody.decryptToObj(data, GatewayCryptKeyConst.CLIENT_PRIVATE, ApiHandleVO.class);
        // 打印日志.
        log.info(JSONUtil.toJsonStr(apiHandleVO));
        return true;
    }

    @Override
    public boolean addGatewayApply(AbilityApiApplyEntity applyEntity) {
        return false;
    }

    @Override
    public boolean unbindApply(AbilityApiApplyEntity applyEntity) {
        return false;
    }
}
