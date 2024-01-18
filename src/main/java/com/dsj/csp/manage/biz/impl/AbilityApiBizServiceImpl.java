package com.dsj.csp.manage.biz.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.ManageApplication;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityApplyService;
import com.dsj.csp.manage.service.impl.ManageApplicationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AbilityApiBizServiceImpl implements AbilityApiBizService {


    @Autowired
    private ManageApplicationServiceImpl manageApplicationService;
    private final AbilityApplyService abilityApplyService;
    private final AbilityApiService abilityApiService;


    @Override
    public List<String> getApiList(String appCode) {
        LambdaQueryWrapper<ManageApplication> appQW = Wrappers.lambdaQuery();
        appQW.eq(ManageApplication::getAppCode, appCode);
        Long appId = manageApplicationService.getOne(appQW).getAppId();
        // 查出应用关联的所有能力
        LambdaQueryWrapper<AbilityApplyEntity> applyQW = Wrappers.lambdaQuery();
        applyQW.eq(AbilityApplyEntity::getAppId, appId);
        List<AbilityApplyEntity> applyList = abilityApplyService.list(applyQW);

        // 查出所有能力关联的api的Id列表
        List<String> apiIdList= new ArrayList<>();
        for (AbilityApplyEntity applyEntity: applyList){
            String apiIds = applyEntity.getApiIds();
            String[] apiIdArr =  apiIds.split(",");
            apiIdList.addAll(Arrays.asList(apiIds.split(",")));
        }
        // 最后查出所有api对应的path
        List<String> apiPaths = new ArrayList<>();
        for (String apiId : apiIdList){
            AbilityApiEntity api = abilityApiService.getById(Long.parseLong(apiId));
            apiPaths.add(api.getApiUrl());
        }

        return apiPaths;
    }
}
