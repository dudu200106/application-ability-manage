package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.*;
import com.dsj.csp.manage.service.*;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityApiBizServiceImpl implements AbilityApiBizService {

    private final AbilityApiApplyService abilityApiApplyService;
    private final AbilityApiService abilityApiService;
    private final AbilityApiReqService abilityApiReqService;
    private final AbilityApiRespService abilityApiRespService;
    private final AbilityService abilityService;
    private final UserApproveService userApproveService;

    @Override
    public void saveApi(AbilityApiVO apiVO, String accessToken) {
        long cnt = abilityApiService.count(Wrappers.lambdaQuery(AbilityApiEntity.class)
                .or().and(i->i.eq(AbilityApiEntity::getAbilityId, apiVO.getAbilityId())
                        .eq(AbilityApiEntity::getApiName, apiVO.getApiName()))
                .or().eq(AbilityApiEntity::getApiUrl, apiVO.getApiUrl()));
        if (cnt>0) {
            throw new BusinessException("保存api信息出错! 接口URL重名或者能力下已有同名接口");
        }
        // 插入能力基本信息
        AbilityApiEntity api = new AbilityApiEntity();
        BeanUtil.copyProperties(apiVO, api, true);
        UserApproveRequest userApprove = userApproveService.identify(accessToken);
        api.setUserId(Long.parseLong(userApprove.getUserId()));
        abilityApiService.save(api);
        // 插入接口的出参入参列表
        abilityApiReqService.saveReqList(apiVO.getReqList(), api.getApiId());
        abilityApiRespService.saveRespList(apiVO.getRespList(), api.getApiId());
    }

    @Override
    public boolean updateApi(AbilityApiVO apiVO) {
        AbilityApiEntity api = new AbilityApiEntity();
        BeanUtil.copyProperties(apiVO, api, true);
        api.setUpdateTime(new Date());

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

    @Override
    public List<AbilityApiEntity> getAbilityApiList(Long abilityId) {
        List<AbilityApiEntity> apis = abilityApiService.list(
                Wrappers.lambdaQuery(AbilityApiEntity.class).eq(AbilityApiEntity::getAbilityId, abilityId)
        );
        return apis;
    }

    @Override
    public List<AbilityApiVO> getAppApiList(Long appId) {
        // 查询出申请通过的apiId集合
        Set<Long> apiIds = abilityApiApplyService.getPassedApiIds(null, appId, null, null);
        if (apiIds.size()==0){
            return null;
        }
        // 构造查询条件
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery(AbilityApiEntity.class)
                .in(AbilityApiEntity::getApiId, apiIds)
                // 排序
                .orderByDesc(AbilityApiEntity::getUpdateTime)
                .orderByAsc(AbilityApiEntity::getStatus);
        // 主表查询
        List<AbilityApiEntity> apiList = abilityApiService.list(queryWrapper);
        // 构造返回结果
        Set<Long> abilityIds = apiList.stream().map(api -> api.getAbilityId()).collect(Collectors.toSet());
        Map<Long, AbilityEntity> abilityMap = abilityService.getAbilityMap(abilityIds);
        List<AbilityApiVO> apiVOs = apiList.stream().map(api->{
            AbilityApiVO apiVO = new AbilityApiVO();
            BeanUtil.copyProperties(api, apiVO, true);
            apiVO.setAbilityName(abilityMap.get(api.getAbilityId())==null ? null : abilityMap.get(api.getAbilityId()).getAbilityName());
            return apiVO;
        }).toList();
        return apiVOs;
    }

    @Override
    public List<AbilityApiVO> getUserApiList(Long userId) {
        // 查询出申请通过的apiId集合
        Set<Long> apiIds = abilityApiApplyService.getPassedApiIds(userId, null, null, null);
        if (apiIds.size()==0){
            return null;
        }
        // 构造查询条件
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery(AbilityApiEntity.class)
                .in(AbilityApiEntity::getApiId, apiIds)
                // 排序
                .orderByDesc(AbilityApiEntity::getUpdateTime)
                .orderByAsc(AbilityApiEntity::getStatus);
        // 主表查询
        List<AbilityApiEntity> apiList = abilityApiService.list(queryWrapper);
        // 构造返回结果
        Set<Long> abilityIds = apiList.stream().map(api -> api.getAbilityId()).collect(Collectors.toSet());
        Map<Long, AbilityEntity> abilityMap = abilityService.getAbilityMap(abilityIds);
        List<AbilityApiVO> apiVOs = apiList.stream().map(api->{
            AbilityApiVO apiVO = new AbilityApiVO();
            BeanUtil.copyProperties(api, apiVO, true);
            apiVO.setAbilityName(abilityMap.get(api.getAbilityId())==null ? null : abilityMap.get(api.getAbilityId()).getAbilityName());
            return apiVO;
        }).toList();
        return apiVOs;
    }


    @Override
    public Page pagePassedApis(Long userId, Long appId, Long abilityId, String keyword, int current, int size, Date startTime, Date endTime) {
        // 查询出申请通过的apiId集合
        Set<Long> apiIds = abilityApiApplyService.getPassedApiIds(userId, appId, abilityId, keyword);
        if (apiIds.size()==0){
            return new Page(current,size,0);
        }
        // 构造分页条件
        LambdaQueryWrapper<AbilityApiEntity> queryWrapper = Wrappers.lambdaQuery(AbilityApiEntity.class)
                .ge(Objects.nonNull(startTime), AbilityApiEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityApiEntity::getCreateTime, endTime)
                .in( AbilityApiEntity::getApiId, apiIds)
                .eq(AbilityApiEntity::getStatus, 4)
                // 排序
                .orderByDesc(AbilityApiEntity::getCreateTime)
                .orderByAsc(AbilityApiEntity::getStatus);
        // 关键字不为空
        if (!ObjectUtil.isEmpty(keyword)){
            // 获取符合关键字模糊查询的能力ID集合
            List<Long> abiltiyIds = abilityService.getAbilityIds(keyword.trim());
            queryWrapper.and(i -> i.like(AbilityApiEntity::getApiName, keyword)
                    .or().like(AbilityApiEntity::getApiDesc, keyword)
                    .or().like(AbilityApiEntity::getApiUrl, keyword)
                    .or().in(abiltiyIds.size()>0, AbilityApiEntity::getAbilityId, abiltiyIds)
            );
        }
        // 主表分页查询
        Page prePage = abilityApiService.page(new Page<>(current, size), queryWrapper);
        List<AbilityApiEntity> preRecords = prePage.getRecords();
        if (preRecords.size()==0){
            return prePage;
        }
        // 构造返回分页
        Set<Long> abilityIds = preRecords.stream().map(api -> api.getAbilityId()).collect(Collectors.toSet());
        Map<Long, AbilityEntity> abilityMap = abilityService.getAbilityMap(abilityIds);
        Page resPage = new Page(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        List<AbilityApiVO> resRecords = preRecords.stream().map(api -> {
            AbilityApiVO apiVO = new AbilityApiVO();
            BeanUtil.copyProperties(api, apiVO);
            apiVO.setAbilityName(abilityMap.get(api.getAbilityId())==null ? null : abilityMap.get(api.getAbilityId()).getAbilityName());
            return apiVO;
        }).toList();
        resPage.setRecords(resRecords);
        return resPage;
    }


    @Override
    public Page pageApiCatalog(Boolean onlyPublished, String reqMethod, Integer status, Long userId, Long abilityId, String keyword, int current, int size, Date startTime, Date endTime) {
        // 构造分页条件构造器
        LambdaQueryWrapper<AbilityApiEntity> queryWrapper = Wrappers.lambdaQuery(AbilityApiEntity.class)
                .eq(userId!=null, AbilityApiEntity::getUserId, userId)
                .eq(abilityId!=null, AbilityApiEntity::getAbilityId, abilityId)
                .eq(reqMethod!=null, AbilityApiEntity::getReqMethod, reqMethod)
                .eq(status!=null, AbilityApiEntity::getStatus, status)
                .ge(Objects.nonNull(startTime), AbilityApiEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityApiEntity::getCreateTime, endTime)
                .in(onlyPublished, AbilityApiEntity::getStatus, 4)
                // 排序
                .orderByDesc(AbilityApiEntity::getCreateTime);
        // 关键字不为空
        if (!ObjectUtil.isEmpty(keyword)){
            // 获取符合关键字模糊查询的能力ID集合
            List<Long> abiltiyIds = abilityService.getAbilityIds(keyword.trim());
            queryWrapper.and(i -> i.like(AbilityApiEntity::getApiName, keyword)
                            .or().like(AbilityApiEntity::getApiDesc, keyword)
                            .or().like(AbilityApiEntity::getApiUrl, keyword)
                            .or().in(abiltiyIds.size()>0, AbilityApiEntity::getAbilityId, abiltiyIds));
        }
        Page prePage = abilityApiService.page(new Page<>(current, size), queryWrapper);
        List<AbilityApiEntity> preRecords = prePage.getRecords();
        if (preRecords.size()==0){
            return prePage;
        }

        Set<Long> abilityIds = preRecords.stream().map(api -> api.getAbilityId()).collect(Collectors.toSet());
        Map<Long, AbilityEntity> abilityMap = abilityService.getAbilityMap(abilityIds);
        // 构造返回分页
        Page resPage = new Page(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        List<AbilityApiVO> resRecords = preRecords.stream().map(api -> {
            AbilityApiVO apiVO = new AbilityApiVO();
            BeanUtil.copyProperties(api, apiVO);
            apiVO.setAbilityName(abilityMap.get(api.getAbilityId())==null ? null : abilityMap.get(api.getAbilityId()).getAbilityName());
            return apiVO;
        }).toList();
        resPage.setRecords(resRecords);
        return resPage;
    }


}
