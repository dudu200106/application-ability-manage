package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.dto.AbilityApiQueryVO;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.entity.*;
import com.dsj.csp.manage.service.*;
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
    private final AbilityApiApplyService abilityApiApplyService;
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
        try{
            BeanUtil.copyProperties(apiVO, api, true);
            abilityApiService.save(api);
        }catch (Exception e){
            throw new BusinessException("保存api信息出错! 可能存在的异常:输入的URL地址重名");
        }

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
        Page<AbilityApiEntity> page = abilityApiService.page(apiQueryVO.toPage(), apiQueryVO.getQueryWrapper());
        // 数据条数为空, 直接返回, 避免空指针
        if (page.getTotal()==0){
            return page;
        }
        // 查出能力ID和能力名称的映射
        List<AbilityApiEntity> records = page.getRecords();
        Set<Long> abilityIds = records.stream().map(e->e.getAbilityId()).collect(Collectors.toSet());
        List<AbilityEntity> abilitys = abilityService.list(Wrappers.lambdaQuery(AbilityEntity.class)
                .in(AbilityEntity::getAbilityId, abilityIds)
                .select(AbilityEntity::getAbilityId, AbilityEntity::getAbilityName));
        Map<Long, String> abilityMap = abilitys.stream()
                .collect(Collectors.toMap(ability -> ability.getAbilityId(), ability -> ability.getAbilityName()));

        // 构造返回的分页resPage
        List<AbilityApiVO> newRecords = records.stream().map(api->{
            AbilityApiVO apiVO = new AbilityApiVO();
            BeanUtil.copyProperties(api, apiVO, true);
            apiVO.setAbilityName(abilityMap.get(api.getAbilityId()));
            return apiVO;
        }).toList();
        Page<AbilityApiVO> resPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
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

    @Override
    public List<AbilityApiVO> getAppApiList(Long appId) {
        List<String> apiIdsList = abilityApplyService.list(Wrappers.lambdaQuery(AbilityApplyEntity.class)
                        .eq(AbilityApplyEntity::getAppId, appId)
                        .eq(AbilityApplyEntity::getStatus, 1)
                        .select(AbilityApplyEntity::getApiIds))
                .stream().map(e->e.getApiIds()).toList();
        // 分割去重得到apiId集合
        Set<Long> ids = new HashSet<>();
        apiIdsList.forEach(apiIds ->{
            ids.addAll(Arrays.asList(apiIds.split(",")).stream().map(e->Long.parseLong(e)).toList());
        });
        if (ids.size()==0){
            return null;
        }
        List<AbilityApiEntity> apis = abilityApiService.listByIds(ids);
        // 查询api对应能力id集合, 并查出能力ID对应能力名称
        Set<Long> abilityIds = apis.stream().map(e->e.getAbilityId()).collect(Collectors.toSet());
        List<AbilityEntity> abilitys = abilityService.list(Wrappers.lambdaQuery(AbilityEntity.class)
                .select(AbilityEntity::getAbilityId, AbilityEntity::getAbilityName)
                .in(AbilityEntity::getAbilityId, abilityIds));
        // 构造能力ID与能力name映射, 方便查找
        Map<Long, String> abilityMap = abilitys.stream().collect(Collectors.toMap(e->e.getAbilityId(), e-> e.getAbilityName()));
        // 构造返回值
        List<AbilityApiVO> apiVOs = apis.stream().map(api->{
            AbilityApiVO apiVO = new AbilityApiVO();
            BeanUtil.copyProperties(api, apiVO, true);
            apiVO.setAbilityName(abilityMap.get(api.getAbilityId()));
            return apiVO;
        }).toList();
        return apiVOs;
    }

    @Override
    public List<AbilityApiVO> getUserApiList(Long userId) {
        List<String> apiIdsList = abilityApplyService.list(Wrappers.lambdaQuery(AbilityApplyEntity.class)
                        .eq(AbilityApplyEntity::getUserId, userId)
                        .eq(AbilityApplyEntity::getStatus, 1)
                        .select(AbilityApplyEntity::getApiIds))
                .stream().map(e->e.getApiIds()).toList();
        // 分割去重得到apiId集合
        Set<Long> ids = new HashSet<>();
        apiIdsList.forEach(apiIds ->{
            ids.addAll(Arrays.asList(apiIds.split(",")).stream().map(e->Long.parseLong(e)).toList());
        });
        List<AbilityApiEntity> apis = abilityApiService.listByIds(ids);
        // 查询api对应能力id集合, 并查出能力ID对应能力名称
        Set<Long> abilityIds = apis.stream().map(e->e.getAbilityId()).collect(Collectors.toSet());
        List<AbilityEntity> abilitys = abilityService.list(Wrappers.lambdaQuery(AbilityEntity.class)
                .select(AbilityEntity::getAbilityId, AbilityEntity::getAbilityName)
                .in(AbilityEntity::getAbilityId, abilityIds));
        // 构造能力ID与能力name映射, 方便查找
        Map<Long, String> abilityMap = abilitys.stream().collect(Collectors.toMap(e->e.getAbilityId(), e-> e.getAbilityName()));
        // 构造返回值
        List<AbilityApiVO> apiVOs = apis.stream().map(api->{
            AbilityApiVO apiVO = new AbilityApiVO();
            BeanUtil.copyProperties(api, apiVO, true);
            apiVO.setAbilityName(abilityMap.get(api.getAbilityId()));
            return apiVO;
        }).toList();
        return apiVOs;
    }


    @Override
    public Page pageApplyApis(Long userId, Long appId, Long abilityId, String keyword, int size, int current, Date startTime, Date endTime) {
        // 查询出申请通过的apiId集合
        Set<Long> apiIds = abilityApiApplyService.getPassedApiIds(userId, appId, abilityId, keyword);
        if (apiIds.size()==0){
            return new Page(1,0,0);
        }
        // 构造分页条件
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery(AbilityApiEntity.class)
                .ge(Objects.nonNull(startTime), AbilityApiEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityApiEntity::getCreateTime, endTime)
                .in(AbilityApiEntity::getApiId, apiIds)
                // 接口信息关键字模糊查询
                .and(keyword!=null && !"".equals(keyword),i -> i
                        .like(AbilityApiEntity::getApiName, keyword)
                        .or().like(AbilityApiEntity::getDescription, keyword)
                        .or().like(AbilityApiEntity::getApiUrl, keyword))
                // 排序
                .orderByAsc(AbilityApiEntity::getStatus)
                .orderByDesc(AbilityApiEntity::getCreateTime);
        // 主表分页查询
        Page prePage = abilityApiService.page(new Page<>(current, size), queryWrapper);
        List<AbilityApiEntity> preRecords = prePage.getRecords();
        if (preRecords.size()==0){
            return prePage;
        }
        // 构造返回分页
        Set<Long> abilityIds = preRecords.stream().map(api -> api.getAbilityId()).collect(Collectors.toSet());
        List<AbilityEntity> abilitys = abilityService.list(Wrappers.lambdaQuery(AbilityEntity.class)
                .select(AbilityEntity::getAbilityId, AbilityEntity::getAbilityName)
                .in(AbilityEntity::getAbilityId, abilityIds));
        Map<Long, String> abilityMap = abilitys.stream().collect(Collectors.toMap(ability -> ability.getAbilityId(), ability -> ability.getAbilityName()));
        Page resPage = new Page(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        List<AbilityApiVO> resRecords = preRecords.stream().map(api -> {
            AbilityApiVO apiVO = new AbilityApiVO();
            BeanUtil.copyProperties(api, apiVO);
            apiVO.setAbilityName(abilityMap.get(api.getAbilityId()));
            return apiVO;
        }).toList();
        resPage.setRecords(resRecords);
        return resPage;
    }


    @Override
    public Page pageApis(Boolean onlyPublished, Long userId, Long abilityId, String keyword, int size, int current, Date startTime, Date endTime) {
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery(AbilityApiEntity.class)
                .eq(userId!=null, AbilityApiEntity::getUserId, userId)
                .eq(userId!=null, AbilityApiEntity::getAbilityId, abilityId)
                .ge(Objects.nonNull(startTime), AbilityApiEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityApiEntity::getCreateTime, endTime)
                .in(onlyPublished, AbilityApiEntity::getStatus, 3)
                .and(keyword!=null && !"".equals(keyword),
                        i -> i.like(AbilityApiEntity::getApiName, keyword)
                                .or().like(AbilityApiEntity::getDescription, keyword)
                                .or().like(AbilityApiEntity::getApiUrl, keyword))
                // 排序
                .orderByAsc(AbilityApiEntity::getStatus)
                .orderByDesc(AbilityApiEntity::getCreateTime);
        Page prePage = abilityApiService.page(new Page<>(current, size), queryWrapper);
        List<AbilityApiEntity> preRecords = prePage.getRecords();
        if (preRecords.size()==0){
            return prePage;
        }
        Set<Long> abilityIds = preRecords.stream().map(api -> api.getAbilityId()).collect(Collectors.toSet());
        List<AbilityEntity> abilitys = abilityService.list(Wrappers.lambdaQuery(AbilityEntity.class)
                .select(AbilityEntity::getAbilityId, AbilityEntity::getAbilityName)
                .in(AbilityEntity::getAbilityId, abilityIds));
        Map<Long, String> abilityMap = abilitys.stream().collect(Collectors.toMap(ability -> ability.getAbilityId(), ability -> ability.getAbilityName()));

        // 构造返回分页
        Page resPage = new Page(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        List<AbilityApiVO> resRecords = preRecords.stream().map(api -> {
            AbilityApiVO apiVO = new AbilityApiVO();
            BeanUtil.copyProperties(api, apiVO);
            apiVO.setAbilityName(abilityMap.get(api.getAbilityId()));
            return apiVO;
        }).toList();
        resPage.setRecords(resRecords);
        return resPage;
    }


}
