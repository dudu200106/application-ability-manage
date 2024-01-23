package com.dsj.csp.manage.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.entity.*;
import com.dsj.csp.manage.service.*;
import com.dsj.csp.manage.util.Sm2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityApiBizServiceImpl implements AbilityApiBizService {

    private final ManageApplicationService manageApplicationService;
    private final AbilityApplyService abilityApplyService;
    private final AbilityApiService abilityApiService;
    private final AbilityApiReqService abilityApiReqService;
    private final AbilityApiRespService abilityApiRespService;

    @Override
    public List<String> getApiList(String appCode) {
        LambdaQueryWrapper<ManageApplicationEntity> appQW = Wrappers.lambdaQuery();
        appQW.eq(ManageApplicationEntity::getAppCode, appCode);
        Long appId = manageApplicationService.getOne(appQW).getAppId();
        // 查出应用关联的所有能力
        LambdaQueryWrapper<AbilityApplyEntity> applyQW = Wrappers.lambdaQuery();
        applyQW.eq(AbilityApplyEntity::getAppId, appId);
        List<AbilityApplyEntity> applyList = abilityApplyService.list(applyQW);

        // 查出所有能力关联的api的Id列表
        List<String> apiIdList = applyList.stream()
                .map(AbilityApplyEntity::getApiIds).collect(Collectors.toList());
        Set<String> apiSet = new HashSet<>();
        apiIdList.forEach(e->
            apiSet.addAll(Arrays.asList(e.split(",")))
        );
        // 最后查出所有api对应的path
        LambdaQueryWrapper<AbilityApiEntity> apiQW =
                Wrappers.lambdaQuery(AbilityApiEntity.class).in(AbilityApiEntity::getApiId, apiSet);
        List<String> apiPaths = abilityApiService
                .getBaseMapper().selectList(apiQW)
                .stream().map(i->i.getApiUrl()).toList();
        return apiPaths;
    }


    @Override
    public void saveApi(AbilityApiVO apiVO) {
        // 插入能力基本信息
        AbilityApiEntity api = new AbilityApiEntity();
        BeanUtils.copyProperties(apiVO, api);
        Map<String, String> sm2Map = Sm2.sm2Test();
        api.setPublicKey(sm2Map.get("publicEncode"));
        api.setSecretKey(sm2Map.get("privateEncode"));
        abilityApiService.save(api);

        // 插入接口的出参入参
        Long apiId = api.getApiId();
        List<AbilityApiReq> apiReqList = apiVO.getReqList()
                .stream()
                .peek(req -> {
                    req.setApiId(apiId);
                }).toList();
        List<AbilityApiResp> apiRespList = apiVO.getRespList()
                .stream()
                .peek(resp -> {
                    resp.setApiId(apiId);
                }).toList();
        abilityApiReqService.saveBatch(apiReqList);
        abilityApiRespService.saveBatch(apiRespList);
    }

    @Override
    public boolean updateApi(AbilityApiVO apiVO) {
        AbilityApiEntity api = new AbilityApiEntity();
        BeanUtils.copyProperties(apiVO, api);
        // 覆盖参数列表
        LambdaQueryWrapper reqQW = Wrappers.lambdaQuery(AbilityApiReq.class).eq(AbilityApiReq::getApiId, apiVO.getApiId());
        abilityApiReqService.remove(reqQW);
        LambdaQueryWrapper respQW = Wrappers.lambdaQuery(AbilityApiResp.class).eq(AbilityApiResp::getApiId, apiVO.getApiId());
        abilityApiRespService.remove(respQW);

        Long apiId = apiVO.getApiId();
        List<AbilityApiReq> apiReqList = apiVO.getReqList()
                .stream()
                .peek(req -> {
                    req.setApiId(apiId);
                }).toList();
        List<AbilityApiResp> apiRespList = apiVO.getRespList()
                .stream()
                .peek(resp -> {
                    resp.setApiId(apiId);
                }).toList();
        return abilityApiService.updateById(api) &&
        abilityApiReqService.saveBatch(apiVO.getReqList()) &&
        abilityApiRespService.saveBatch(apiVO.getRespList());
    }

    @Override
    public AbilityApiVO getApiInfo(Long apiId) {
        AbilityApiVO res = new AbilityApiVO();
        AbilityApiEntity apiEntity = abilityApiService.getById(apiId);

        LambdaQueryWrapper<AbilityApiResp> respQW =
                Wrappers.lambdaQuery(AbilityApiResp.class).eq(AbilityApiResp::getApiId, apiId);
        List<AbilityApiResp> resps = abilityApiRespService.getBaseMapper().selectList(respQW);

        LambdaQueryWrapper<AbilityApiReq> reqQW =
                Wrappers.lambdaQuery(AbilityApiReq.class).eq(AbilityApiReq::getApiId, apiId);
        List<AbilityApiReq> reqs = abilityApiReqService.getBaseMapper().selectList(reqQW);
        BeanUtils.copyProperties(apiEntity, res);
        res.setRespList(resps);
        res.setReqList(reqs);
        return res ;
    }

}
