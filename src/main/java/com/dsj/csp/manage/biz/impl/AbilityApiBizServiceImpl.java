package com.dsj.csp.manage.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityApplyService;
import com.dsj.csp.manage.service.ManageApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityApiBizServiceImpl implements AbilityApiBizService {


    @Autowired
    private ManageApplicationService manageApplicationService;
    private final AbilityApplyService abilityApplyService;
    private final AbilityApiService abilityApiService;


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
}
