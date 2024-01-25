package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.dto.AbilityApiQueryVO;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.entity.*;
import com.dsj.csp.manage.service.*;
import com.dsj.csp.manage.util.Sm2;
import lombok.RequiredArgsConstructor;
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
    private final AbilityService abilityService;

    @Override
    public List<String> getApiList(String appCode) {
        LambdaQueryWrapper<ManageApplicationEntity> appQW = Wrappers.lambdaQuery();
        appQW.eq(ManageApplicationEntity::getAppCode, appCode);
        String appId = manageApplicationService.getOne(appQW).getAppId();
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
        BeanUtil.copyProperties(apiVO, api, true);
        Map<String, String> sm2Map = Sm2.sm2Test();
        api.setPublicKey(sm2Map.get("publicEncode"));
        api.setSecretKey(sm2Map.get("privateEncode"));
        abilityApiService.save(api);

        // 插入接口的出参入参
        abilityApiReqService.saveReqList(apiVO.getReqList(), api.getApiId());
        abilityApiRespService.saveRespList(apiVO.getRespList(), api.getApiId());
    }

    @Override
    public boolean updateApi(AbilityApiVO apiVO) {
        AbilityApiEntity api = new AbilityApiEntity();
        BeanUtil.copyProperties(apiVO, api, true);

        // 覆盖参数列表
        LambdaQueryWrapper reqQW = Wrappers.lambdaQuery(AbilityApiReq.class).eq(AbilityApiReq::getApiId, apiVO.getApiId());
        abilityApiReqService.remove(reqQW);
        LambdaQueryWrapper respQW = Wrappers.lambdaQuery(AbilityApiResp.class).eq(AbilityApiResp::getApiId, apiVO.getApiId());
        abilityApiRespService.remove(respQW);

        Long apiId = apiVO.getApiId();
        return abilityApiService.updateById(api) &&
                abilityApiReqService.saveReqList(apiVO.getReqList(), apiId) &&
                abilityApiRespService.saveRespList(apiVO.getRespList(), apiId);
    }

    @Override
    public AbilityApiVO getApiInfo(Long apiId) {
        AbilityApiVO res = new AbilityApiVO();
        AbilityApiEntity apiEntity = abilityApiService.getById(apiId);

        String abilityName = abilityService.getById(apiEntity.getAbilityId()).getAbilityName();
        List<AbilityApiResp> resps = abilityApiRespService.list(
                Wrappers.lambdaQuery(AbilityApiResp.class).eq(AbilityApiResp::getApiId, apiId));
        List<AbilityApiReq> reqs = abilityApiReqService.getBaseMapper().selectList(
                Wrappers.lambdaQuery(AbilityApiReq.class).eq(AbilityApiReq::getApiId, apiId));
        BeanUtil.copyProperties(apiEntity, res, true);
        res.setAbilityName(abilityName);
        res.setRespList(resps);
        res.setReqList(reqs);
        return res ;
    }

    public Page pageApi(AbilityApiQueryVO apiQueryVO){
        Page<AbilityApiEntity> p = abilityApiService.page(apiQueryVO.toPage(), apiQueryVO.getQueryWrapper());
        // 查出能力ID和能力名称的映射
        List<AbilityApiEntity> records = p.getRecords();
        Set<Long> abilityIds = records.stream().map(e->e.getAbilityId()).collect(Collectors.toSet());
        LambdaQueryWrapper abilityQW= Wrappers.lambdaQuery(AbilityEntity.class)
                .in(AbilityEntity::getAbilityId, abilityIds)
                .select(AbilityEntity::getAbilityId, AbilityEntity::getAbilityName);
        List<AbilityEntity> abilitys = abilityService.getBaseMapper().selectList(abilityQW);
        Map<Long, String> abilityMap = abilitys
                .stream()
                .collect(Collectors
                .toMap(ability -> ability.getAbilityId(), ability -> ability.getAbilityName()));
        // 构造返回的分页resPage
        List<AbilityApiVO> newRecords = records.stream().map(api->{
            AbilityApiVO apiVO = new AbilityApiVO();
            BeanUtil.copyProperties(api, apiVO, true);
            apiVO.setAbilityName(abilityMap.get(api.getAbilityId()));
            return apiVO;
        }).toList();
        Page<AbilityApiVO> resPage = new Page<>(p.getCurrent(), p.getSize(), p.getTotal());
        resPage.setRecords(newRecords);
        return resPage;
    }

    @Override
    public List<AbilityApiEntity> getApplyApiList(Long abilityApplyId) {
        String apiIds = abilityApplyService.getById(abilityApplyId).getApiIds();
        List<Long> idList = Arrays.asList(apiIds.split(",")).stream().map(e->Long.parseLong(e)).toList();
        List<AbilityApiEntity> apis = abilityApiService.listByIds(idList);
        return apis;
    }

    @Override
    public List<AbilityApiEntity> getAbilityApiList(Long abilityId) {
        List<AbilityApiEntity> apis = abilityApiService.list(
                Wrappers.lambdaQuery(AbilityApiEntity.class).eq(AbilityApiEntity::getAbilityId, abilityId)
        );
        return apis;
    }
}
