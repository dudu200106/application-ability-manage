package com.dsj.csp.manage.biz.impl;

import cn.hutool.json.JSONUtil;
import com.dsj.common.dto.BusinessException;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.consts.GatewayCryptKeyConst;
import com.dsj.csp.manage.biz.GatewayAdminBizService;
import com.dsj.csp.manage.dto.gateway.ApiHandleVO;
import com.dsj.csp.manage.dto.gateway.AppHandleVO;
import com.dsj.csp.manage.dto.gateway.ApplyHandleVO;
import com.dsj.csp.manage.dto.gateway.CryptJsonBody;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.dsj.csp.manage.service.ApiFeignService;
import com.dsj.csp.manage.service.AppFeignService;
import com.dsj.csp.manage.service.ApplyFeignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-03-05
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayAdminBizImplService implements GatewayAdminBizService {

    private final AppFeignService appFeignService;
    private final ApplyFeignService applyFeignService;
    private final ApiFeignService apiFeignService;
    @Override
    public boolean addGatewayApp(ManageApplicationEntity appEntity) {
        log.info("------------ 远程调用网关接口: /allow/app/add  新增app ------------");
        // 要传递的JSON数据
        HashMap<String, String> appMap = new HashMap<>();
        appMap.put("appExtId", appEntity.getAppId());
        appMap.put("appName", appEntity.getAppName());
        appMap.put("appDesc", appEntity.getAppSynopsis());
        appMap.put("appPublicKey", appEntity.getAppKey());
        appMap.put("appPrivateKey", appEntity.getAppSecret());
        appMap.put("gatewayPublicKey", appEntity.getAppWgKey());
        appMap.put("gatewayPrivateKey", appEntity.getAppWgSecret());

        // 加密，调用网关接口
        CryptJsonBody cryptJsonBody = CryptJsonBody.encryptObj(appMap, GatewayCryptKeyConst.SERVER_PUBLIC);
        Result<CryptJsonBody> result = appFeignService.addApp(cryptJsonBody);

        // 检查、处理、打印响应结果
        log.info("请求网关新增app 返回响应: ");
        CryptJsonBody data  = checkResultCode(result);
        // 响应解密
        AppHandleVO appHandleVO = CryptJsonBody.decryptToObj(data, GatewayCryptKeyConst.CLIENT_PRIVATE, AppHandleVO.class);
        log.info("data:    {}", JSONUtil.toJsonStr(appHandleVO));
        return true;
    }

    @Override
    public boolean cancelGatewayApp(ManageApplicationEntity appEntity) {
        log.info("------------ 远程调用网关接口: /allow/app/cancel  禁用app ------------");
        // 要传递的JSON数据
        HashMap<String, String> map = new HashMap<>();
        map.put("appExtId", appEntity.getAppId());

        // 加密，调用网关接口
        CryptJsonBody cryptJsonBody = CryptJsonBody.encryptObj(map, GatewayCryptKeyConst.SERVER_PUBLIC);
        Result<CryptJsonBody> result = appFeignService.cancelApp(cryptJsonBody);
        // 检查、处理、打印响应结果
        log.info("请求网关禁用app 返回响应: ");
        CryptJsonBody data = checkResultCode(result);
        // 响应解密
        AppHandleVO appHandleVO = CryptJsonBody.decryptToObj(data, GatewayCryptKeyConst.CLIENT_PRIVATE, AppHandleVO.class);
        log.info("data:    {}", JSONUtil.toJsonStr(appHandleVO));
        return true;
    }

    @Override
    public boolean addGatewayApi(AbilityApiEntity apiEntity) {
        log.info("------------ 远程调用网关接口: /allow/api/add  新增api ------------");
        // 要传递的JSON数据
        HashMap<String, String> map = new HashMap<>();
        map.put("apiExtId", apiEntity.getApiId()+"");
        map.put("apiName", apiEntity.getApiName());
        map.put("apiPath", apiEntity.getApiUrl());
        // 加密，调用网关接口
        CryptJsonBody cryptJsonBody = CryptJsonBody.encryptObj(map, GatewayCryptKeyConst.SERVER_PUBLIC);
        Result<CryptJsonBody> result = apiFeignService.addApi(cryptJsonBody);
        // 检查、处理、打印响应结果
        log.info("网关新增api接口返回响应: ");
        CryptJsonBody data  = checkResultCode(result);
        // 响应解密
        ApiHandleVO apiHandleVO = CryptJsonBody.decryptToObj(data, GatewayCryptKeyConst.CLIENT_PRIVATE, ApiHandleVO.class);
        log.info("data:    {}", JSONUtil.toJsonStr(apiHandleVO));
        return true;
    }

    @Override
    public boolean cancelGatewayApi(AbilityApiEntity apiEntity) {
        log.info("------------ 远程调用网关接口: /allow/api/cancel  禁用api ------------");
        // 要传递的JSON数据
        HashMap<String, String> map = new HashMap<>();
        map.put("apiExtId", apiEntity.getApiId()+"");
        // 加密，调用网关接口
        CryptJsonBody cryptJsonBody = CryptJsonBody.encryptObj(map, GatewayCryptKeyConst.SERVER_PUBLIC);
        Result<CryptJsonBody> result = apiFeignService.cancelApi(cryptJsonBody);
        // 检查、处理、打印响应结果
        log.info("请求网关禁用api 返回响应: ");
        CryptJsonBody data  = checkResultCode(result);
        // 响应解密
        ApiHandleVO apiHandleVO = CryptJsonBody.decryptToObj(data, GatewayCryptKeyConst.CLIENT_PRIVATE, ApiHandleVO.class);
        log.info("data:    {}", JSONUtil.toJsonStr(apiHandleVO));
        return true;
    }

    @Override
    public boolean addGatewayApply(AbilityApiApplyEntity applyEntity) {
        log.info("------------ 远程调用网关接口: /allow/apply/bind  新增申请 ------------");
        HashMap<String, String> applyMap = new HashMap<>();
        applyMap.put("applyExtId", applyEntity.getApiApplyId()+"");
        applyMap.put("appExtId", applyEntity.getAppId()+"");
        applyMap.put("apiExtId", applyEntity.getApiId()+"");

        // 加密，调用网关接口
        CryptJsonBody cryptJsonBody = CryptJsonBody.encryptObj(applyMap, GatewayCryptKeyConst.SERVER_PUBLIC);
        Result<CryptJsonBody> result = applyFeignService.bindApply(cryptJsonBody);
        // 检查、处理、打印响应结果
        log.info("网关新增申请接口 返回响应: ");
        CryptJsonBody data  = checkResultCode(result);
        // 响应解密
        ApplyHandleVO applyHandleVO = CryptJsonBody.decryptToObj(data, GatewayCryptKeyConst.CLIENT_PRIVATE, ApplyHandleVO.class);
        log.info("data:    {}", JSONUtil.toJsonStr(applyHandleVO));
        return true;
    }

    @Override
    public boolean saveApplyComplete(ManageApplicationEntity app, AbilityApiEntity api, AbilityApiApplyEntity apply) {
        log.info("------------ 远程调用网关接口: /allow/apply/add  新增申请 ------------");

        HashMap<String, String> appMap = new HashMap<>();
        HashMap<String, String> apiMap = new HashMap<>();
        HashMap<String, String> applyMap = new HashMap<>();
        appMap.put("appExtId", app.getAppId());
        appMap.put("appName", app.getAppName());
        appMap.put("appDesc", app.getAppSynopsis());
        appMap.put("appPublicKey", app.getAppKey());
        appMap.put("appPrivateKey", app.getAppSecret());
        appMap.put("gatewayPublicKey", app.getAppWgKey());
        appMap.put("gatewayPrivateKey", app.getAppWgSecret());

        apiMap.put("apiExtId", api.getApiId()+"");
        apiMap.put("apiName", api.getApiName());
        apiMap.put("apiPath", api.getApiUrl());

        applyMap.put("applyExtId", apply.getApiApplyId()+"");
        applyMap.put("appExtId", apply.getAppId()+"");
        applyMap.put("apiExtId", apply.getApiId()+"");
//        applyMap.put("sourceCode", "common-support");
//        applyMap.put("sourceName", "共性应用支撑");
        applyMap.put("requestCryptRule", apply.getReqCryptRule());
        applyMap.put("responseCryptRule", apply.getRespCryptRule());

        HashMap<String, HashMap<String, String>> reqMap = new HashMap<>();
        reqMap.put("app", appMap);
        reqMap.put("api", apiMap);
        reqMap.put("apply", applyMap);

        // 加密，调用网关接口
        CryptJsonBody cryptJsonBody = CryptJsonBody.encryptObj(reqMap, GatewayCryptKeyConst.SERVER_PUBLIC);
        Result<CryptJsonBody> result = applyFeignService.addApply(cryptJsonBody);
        // 检查、处理、打印响应结果
        log.info("网关新增申请接口 返回响应: ");
        CryptJsonBody data  = checkResultCode(result);
        // 响应解密
        ApplyHandleVO applyHandleVO = CryptJsonBody.decryptToObj(data, GatewayCryptKeyConst.CLIENT_PRIVATE, ApplyHandleVO.class);
        log.info("data:    {}", JSONUtil.toJsonStr(applyHandleVO));
        return true;
    }

    @Override
    public boolean unbindApply(AbilityApiApplyEntity applyEntity) {
        log.info("------------远程调用网关接口: /allow/apply/unbind  解绑申请 ------------");

        // 要传递的JSON数据
        HashMap<String, String> map = new HashMap<>();
        map.put("applyExtId", applyEntity.getApiApplyId()+"");
        // 加密，调用网关接口
        CryptJsonBody cryptJsonBody = CryptJsonBody.encryptObj(map, GatewayCryptKeyConst.SERVER_PUBLIC);
        Result<CryptJsonBody> result = applyFeignService.unbindApply(cryptJsonBody);

        // 检查、处理、打印响应结果
        log.info("请求网关禁用申请 返回响应: ");
        CryptJsonBody data  = checkResultCode(result);
        // 响应解密
        ApplyHandleVO applyHandleVO = CryptJsonBody.decryptToObj(data, GatewayCryptKeyConst.CLIENT_PRIVATE, ApplyHandleVO.class);
        log.info("data:    {}", JSONUtil.toJsonStr(applyHandleVO));
        return true;
    }


    /**
     * 检查网关的响应码, 返回返回data
     * @param result
     * @return
     */
    public CryptJsonBody checkResultCode(Result<CryptJsonBody> result){
        log.info("code:    {}", result.getCode());
        log.info("success: {}", result.getSuccess());
        log.info("msg:     {}", result.getMsg());
        if (result.getCode()!=2000){
            throw new BusinessException(result.getMsg());
        }
        return result.getData();
    }

    @Override
    public boolean unbindBatchApply(List<Long> applyIdList) {
        log.info("------------远程调用网关接口: /allow/apply/unbind-batch  批量解绑申请 ------------");
        // 要传递的JSON数据
        HashMap<String, List<Long>> map = new HashMap<>();
        map.put("applyExtIdList", applyIdList);
        CryptJsonBody cryptJsonBody = CryptJsonBody.encryptObj(map, GatewayCryptKeyConst.SERVER_PUBLIC);
        Result<Boolean> result = applyFeignService.unbindBatchApply(cryptJsonBody);
        log.info("请求网关解绑申请 返回响应: ");
        log.info("code:    {}", result.getCode());
        log.info("success: {}", result.getSuccess());
        log.info("msg:     {}", result.getMsg());
        if (result.getCode()!=2000){
            throw new BusinessException(result.getMsg());
        }
        // 打印响应data
        log.info("data:    {}", JSONUtil.toJsonStr(result.getData()));
        return true;
    }
}
