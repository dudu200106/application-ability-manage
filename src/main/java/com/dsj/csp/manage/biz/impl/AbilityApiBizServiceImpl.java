package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.common.enums.ApiStatusEnum;
import com.dsj.csp.common.enums.ApplyStatusEnum;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.dto.convertor.AbilityApiConvertor;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.*;
import com.dsj.csp.manage.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AbilityApiBizServiceImpl implements AbilityApiBizService {

    private final AbilityApiApplyService abilityApiApplyService;
    private final AbilityApiService abilityApiService;
    private final AbilityApiReqService abilityApiReqService;
    private final AbilityApiRespService abilityApiRespService;
    private final AbilityService abilityService;

    @Override
    @CacheEvict(allEntries = true, cacheNames = "Api", cacheManager = "caffeineCacheManager")
    @Transactional(rollbackFor = Exception.class)
    public void saveApi(AbilityApiVO apiVO, UserApproveRequest userApproveRequest) {
        long cnt = abilityApiService.count(Wrappers.lambdaQuery(AbilityApiEntity.class)
                .or().and(i->i.eq(AbilityApiEntity::getAbilityId, apiVO.getAbilityId())
                        .eq(AbilityApiEntity::getApiName, apiVO.getApiName()))
                .or().eq(AbilityApiEntity::getApiUrl, apiVO.getApiUrl()));
        if (cnt>0) {
            throw new BusinessException("保存api信息出错! 接口URL重名或者能力下已有同名接口");
        }
        // 插入能力基本信息
        AbilityApiEntity api = AbilityApiConvertor.INSTANCE.toEntity(apiVO);
        api.setUserId(Long.parseLong(userApproveRequest.getUserId()));
        api.setApiDesc(apiVO.getApiDesc());
        abilityApiService.save(api);
        // 插入接口的出参入参列表
        abilityApiReqService.saveReqList(apiVO.getReqList(), api.getApiId());
        abilityApiRespService.saveRespList(apiVO.getRespList(), api.getApiId());
    }

    @Override
    @CacheEvict(allEntries = true, cacheNames = "Api", cacheManager = "caffeineCacheManager")
    public String auditApi(AbilityAuditVO auditVO) {
        Long apiId = auditVO.getApiId();
        String note = auditVO.getNote();
        // 审核
        return switch(auditVO.getFlag()) {
            case 0 -> auditWithdraw(apiId, note);
            case 1 -> auditSubmit(apiId, note);
            case 2 -> auditNotPass(apiId, note);
            case 3 -> auditPass(apiId, note);
            case 4 -> auditPublish(apiId, note);
            default -> auditOffline(apiId, note);
        };
    }

    @Override
    public String auditWithdraw(Long apiId, String note) {
        boolean isValid = isApiValid(apiId, ApiStatusEnum.WAIT_AUDIT) ;
        if (!isValid) {
            throw new BusinessException("只用待审核的接口才能撤回,请刷新页面后重试!");
        }
        LambdaUpdateWrapper<AbilityApiEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiEntity::getApiId, apiId);
        updateWrapper.set(AbilityApiEntity::getStatus, ApiStatusEnum.NOT_SUBMIT.getCode());
        updateWrapper.set(AbilityApiEntity::getNote, note);
        updateWrapper.set(AbilityApiEntity::getUpdateTime, new Date());
        abilityApiService.update(updateWrapper);
        return "审核撤回完毕!";
    }

    @Override
    @CacheEvict(allEntries = true, cacheNames = "Api", cacheManager = "caffeineCacheManager")
    public String auditSubmit(Long apiId, String note) {
        boolean isValid = isApiValid(apiId, ApiStatusEnum.NOT_SUBMIT) ;
        if (!isValid) {
            throw new BusinessException("只用待提交状态的接口才能够提交,请刷新页面后重试!");
        }
        LambdaUpdateWrapper<AbilityApiEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiEntity::getApiId, apiId);
        updateWrapper.set(AbilityApiEntity::getStatus, ApiStatusEnum.WAIT_AUDIT.getCode());
        updateWrapper.set(AbilityApiEntity::getNote, note);
        updateWrapper.set(AbilityApiEntity::getUpdateTime, new Date());
        abilityApiService.update(updateWrapper);
        return "审核提交完毕! 请等待审核...";
    }

    @Override
    @CacheEvict(allEntries = true, cacheNames = "Api", cacheManager = "caffeineCacheManager")
    public String auditNotPass(Long apiId, String note) {
        boolean isValid = isApiValid(apiId, ApiStatusEnum.WAIT_AUDIT) ;
        if (!isValid) {
            throw new BusinessException("只用待审核的接口才能审核不通过,请刷新页面后重试!");
        }
        LambdaUpdateWrapper<AbilityApiEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiEntity::getApiId, apiId);
        updateWrapper.set(AbilityApiEntity::getStatus, ApiStatusEnum.NOT_PASSED.getCode());
        updateWrapper.set(AbilityApiEntity::getNote, note);
        updateWrapper.set(AbilityApiEntity::getApproveTime, new Date());
        updateWrapper.set(AbilityApiEntity::getUpdateTime, new Date());
        abilityApiService.update(updateWrapper);
        return "审核不通过完毕!";
    }

    @Override
    @CacheEvict(allEntries = true, cacheNames = "Api", cacheManager = "caffeineCacheManager")
    public String auditPass(Long apiId, String note) {
        boolean isValid = isApiValid(apiId, ApiStatusEnum.WAIT_AUDIT) ;
        if (!isValid) {
            throw new BusinessException("只用待审核的接口才能审核通过,请刷新页面后重试!");
        }
        LambdaUpdateWrapper<AbilityApiEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiEntity::getApiId, apiId);
        updateWrapper.set(AbilityApiEntity::getStatus, ApiStatusEnum.PASSED.getCode());
        updateWrapper.set(AbilityApiEntity::getNote, note);
        updateWrapper.set(AbilityApiEntity::getApproveTime, new Date());
        updateWrapper.set(AbilityApiEntity::getUpdateTime, new Date());
        abilityApiService.update(updateWrapper);
        return "审核通过完毕! 等待发布...";
    }

    @Override
    @CacheEvict(allEntries = true, cacheNames = "Api", cacheManager = "caffeineCacheManager")
    public String auditPublish(Long apiId, String note) {
        boolean isValid = isApiValid(apiId, ApiStatusEnum.PASSED) || isApiValid(apiId, ApiStatusEnum.OFFLINE) ;
        if (!isValid) {
            throw new BusinessException("只用审核通过过的接口才能发布,请刷新页面后重试!");
        }
        LambdaUpdateWrapper<AbilityApiEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiEntity::getApiId, apiId);
        updateWrapper.set(AbilityApiEntity::getStatus, ApiStatusEnum.PUBLISHED.getCode());
        updateWrapper.set(AbilityApiEntity::getNote, note);
        updateWrapper.set(AbilityApiEntity::getApproveTime, new Date());
        updateWrapper.set(AbilityApiEntity::getUpdateTime, new Date());
        abilityApiService.update(updateWrapper);
        return "接口发布完毕!";
    }

    @Override
    @CacheEvict(allEntries = true, cacheNames = "Api", cacheManager = "caffeineCacheManager")
    public String auditOffline(Long apiId, String note) {
        boolean isValid = isApiValid(apiId, ApiStatusEnum.PUBLISHED) ;
        if (!isValid) {
            throw new BusinessException("只用发布的接口才能下线,请刷新页面后重试!");
        }
        long cntUsing = abilityApiApplyService.count(Wrappers.lambdaQuery(AbilityApiApplyEntity.class)
                .eq(AbilityApiApplyEntity::getApiId, apiId)
                .eq(AbilityApiApplyEntity::getStatus, ApplyStatusEnum.PASSED.getCode()));
        if (cntUsing>0){
            throw new BusinessException("该接口还有应用正在使用!");
        }
        LambdaUpdateWrapper<AbilityApiEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApiEntity::getApiId, apiId);
        updateWrapper.set(AbilityApiEntity::getStatus, ApiStatusEnum.OFFLINE.getCode());
        updateWrapper.set(AbilityApiEntity::getNote, note);
        updateWrapper.set(AbilityApiEntity::getApproveTime, new Date());
        updateWrapper.set(AbilityApiEntity::getUpdateTime, new Date());
        abilityApiService.update(updateWrapper);
        return "接口下线完毕!";
    }

    @Override
    public boolean deleteApi(AbilityApiEntity api) {
        long countApply = abilityApiApplyService.lambdaQuery()
                .eq(AbilityApiApplyEntity::getApiId, api.getApiId())
                .eq(AbilityApiApplyEntity::getStatus, ApplyStatusEnum.PASSED.getCode())
                .count();
        if (countApply>0){
            throw new BusinessException("删除接口失败:该接口还有应用在使用!");
        }
        return abilityApiService.removeById(api);
    }

    @Override
    public boolean deleteApiBatch(List<AbilityApiEntity> apiList) {
        List<Long> apiIds = apiList.stream().map(AbilityApiEntity::getApiId).toList();
        long countApply = abilityApiApplyService.lambdaQuery()
                .in(AbilityApiApplyEntity::getApiId, apiIds)
                .eq(AbilityApiApplyEntity::getStatus, ApplyStatusEnum.PASSED.getCode())
                .count();
        if (countApply>0){
            throw new BusinessException("删除接口失败:该接口还有应用在使用!");
        }
        return abilityApiService.removeBatchByIds(apiList);
    }

    /**
     * 审核操作前的判断,
     * @param apiId 接口ID
     * @param targetPrevStatus 期待的前任审核状态
     * @return 审核操作是否有效
     */
    public boolean isApiValid(Long apiId, ApiStatusEnum targetPrevStatus){
        // 审核的接口是否还存在
        AbilityApiEntity api = abilityApiService.getById(apiId);
        if (api==null){
            throw new BusinessException("接口不存在!");
        }
        // 审核操作是否有效
        // 审核流程限制: 状态(0未提交 1待审核 2审核未通过 3未发布 4已发布 5已下线)
        return api.getStatus()==targetPrevStatus.getCode();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApi(AbilityApiVO apiVO) {
        AbilityApiEntity api = AbilityApiConvertor.INSTANCE.toEntity(apiVO);
        api.setUpdateTime(new Date());
        // 覆盖参数列表
        abilityApiReqService.lambdaUpdate()
                .eq(AbilityApiReq::getApiId, apiVO.getApiId())
                .remove();
        abilityApiRespService.lambdaUpdate()
                .eq(AbilityApiResp::getApiId, apiVO.getApiId())
                .remove();
        Long apiId = apiVO.getApiId();
        return abilityApiService.updateById(api) &&
                abilityApiReqService.saveReqList(apiVO.getReqList(), apiId) &&
                abilityApiRespService.saveRespList(apiVO.getRespList(), apiId);
    }

    @Override
    public AbilityApiVO getApiInfo(Long apiId) {

        AbilityApiEntity apiEntity = abilityApiService.getById(apiId);

        String abilityName = abilityService.getById(apiEntity.getAbilityId()).getAbilityName();
        List<AbilityApiResp> resps = abilityApiRespService.list(
                Wrappers.lambdaQuery(AbilityApiResp.class).eq(AbilityApiResp::getApiId, apiId));
        List<AbilityApiReq> reqs = abilityApiReqService.getBaseMapper().selectList(
                Wrappers.lambdaQuery(AbilityApiReq.class).eq(AbilityApiReq::getApiId, apiId));

        AbilityApiVO res = AbilityApiConvertor.INSTANCE.toVO(apiEntity);
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
        if (apiIds.isEmpty()){
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
        Set<Long> abilityIds = apiList.stream().map(AbilityApiEntity::getAbilityId).collect(Collectors.toSet());
        Map<Long, AbilityEntity> abilityMap = abilityService.getAbilityMap(abilityIds);
        return apiList.stream().map(api->{
            AbilityApiVO apiVO = AbilityApiConvertor.INSTANCE.toVO(api);
            apiVO.setAbilityName(abilityMap.getOrDefault(api.getAbilityId(), new AbilityEntity()).getAbilityName());
            return apiVO;
        }).toList();
    }

    @Override
    public List<AbilityApiVO> getUserApiList(Long userId) {
        // 查询出申请通过的apiId集合
        Set<Long> apiIds = abilityApiApplyService.getPassedApiIds(userId, null, null, null);
        if (apiIds.isEmpty()){
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
        Set<Long> abilityIds = apiList.stream().map(AbilityApiEntity::getAbilityId).collect(Collectors.toSet());
        Map<Long, AbilityEntity> abilityMap = abilityService.getAbilityMap(abilityIds);
        return apiList.stream().map(api->{
            AbilityApiVO apiVO = AbilityApiConvertor.INSTANCE.toVO(api);
            apiVO.setAbilityName(abilityMap.getOrDefault(api.getAbilityId(), new AbilityEntity()).getAbilityName());
            return apiVO;
        }).toList();
    }

    @Override
    public Page<AbilityApiVO> pagePassedApis(Long userId, Long appId, Long abilityId, String keyword, int current, int size, Date startTime, Date endTime) {
        // 查询出申请通过的apiId集合
        Set<Long> apiIds = abilityApiApplyService.getPassedApiIds(userId, appId, abilityId, keyword);
        if (apiIds.isEmpty()){
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
            List<Long> abilityIds = abilityService.getAbilityIds(keyword.trim());
            queryWrapper.and(i -> i.like(AbilityApiEntity::getApiId, keyword)
                    .or().like(AbilityApiEntity::getApiName, keyword)
                    .or().like(AbilityApiEntity::getApiDesc, keyword)
                    .or().like(AbilityApiEntity::getApiUrl, keyword)
                    .or().in(abilityIds.size()>0, AbilityApiEntity::getAbilityId, abilityIds)
            );
        }
        // 主表分页查询
        Page<AbilityApiEntity> prePage = abilityApiService.page(new Page<>(current, size), queryWrapper);
        List<AbilityApiEntity> preRecords = prePage.getRecords();
        if (preRecords.isEmpty()){
            return new Page<>(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        }
        // 构造返回分页
        Set<Long> abilityIds = preRecords.stream().map(AbilityApiEntity::getAbilityId).collect(Collectors.toSet());
        Map<Long, AbilityEntity> abilityMap = abilityService.getAbilityMap(abilityIds);
        Page<AbilityApiVO> resPage = new Page<>(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        List<AbilityApiVO> resRecords = preRecords.stream().map(api -> {
            AbilityApiVO apiVO = AbilityApiConvertor.INSTANCE.toVO(api);
            apiVO.setAbilityName(abilityMap.getOrDefault(api.getAbilityId(), new AbilityEntity()).getAbilityName());
            return apiVO;
        }).toList();
        resPage.setRecords(resRecords);
        return resPage;
    }

    @Override
    @Cacheable(keyGenerator = "selfKeyGenerate", cacheNames = "Api", cacheManager = "caffeineCacheManager")
    public Page<AbilityApiVO> pageApiCatalog(Boolean onlyPublished, String reqMethod, Integer status, Long userId, Long abilityId, String keyword, int current, int size, Date startTime, Date endTime) {
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
            queryWrapper.and(i -> i.like(AbilityApiEntity::getApiId, keyword)
                            .or().like(AbilityApiEntity::getApiName, keyword)
                            .or().like(AbilityApiEntity::getApiDesc, keyword)
                            .or().like(AbilityApiEntity::getApiUrl, keyword)
                            .or().in(!abiltiyIds.isEmpty(), AbilityApiEntity::getAbilityId, abiltiyIds));
        }
        Page<AbilityApiEntity> prePage = abilityApiService.page(new Page<>(current, size), queryWrapper);
        List<AbilityApiEntity> preRecords = prePage.getRecords();
        if (preRecords.isEmpty()){
            return new Page<>(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        }

        Set<Long> abilityIds = preRecords.stream().map(AbilityApiEntity::getAbilityId).collect(Collectors.toSet());
        List<Long> apiIds = preRecords.stream().map(AbilityApiEntity::getApiId).toList();
        // 能力分类
        Map<Long, AbilityEntity> abilityMap = abilityService.getAbilityMap(abilityIds);
        // 文档 查出申请的接口对应的文档id
        Map<Long, Long> docMap = SimpleQuery.map(
                Wrappers.lambdaQuery(DocEntity.class)
                        .in(DocEntity::getApiId, apiIds)
                        .eq(DocEntity::getStatus, 3),
                DocEntity::getApiId, DocEntity::getDocId
        );
        // 构造返回分页
        Page<AbilityApiVO> resPage = new Page<>(prePage.getCurrent(), prePage.getSize(), prePage.getTotal());
        List<AbilityApiVO> resRecords = preRecords.stream().map(api -> {
            AbilityApiVO apiVO = AbilityApiConvertor.INSTANCE.toVO(api);
            apiVO.setAbilityName(abilityMap.getOrDefault(api.getAbilityId(), new AbilityEntity())
                    .getAbilityName());
            apiVO.setDocId(docMap.get(api.getApiId()));
            return apiVO;
        }).toList();
        resPage.setRecords(resRecords);
        return resPage;
    }

    @Override
    public List<AbilityApiEntity> getApiCatalog(boolean onlyPublished, String reqMethod, Integer status, Long userId, Long abilityId) {
        // 构造分页条件构造器
        LambdaQueryWrapper<AbilityApiEntity> queryWrapper = Wrappers.lambdaQuery(AbilityApiEntity.class)
                .eq(userId!=null, AbilityApiEntity::getUserId, userId)
                .eq(abilityId!=null, AbilityApiEntity::getAbilityId, abilityId)
                .eq(reqMethod!=null, AbilityApiEntity::getReqMethod, reqMethod)
                .eq(status!=null, AbilityApiEntity::getStatus, status)
                .in(onlyPublished, AbilityApiEntity::getStatus, 4)
                // 排序
                .orderByDesc(AbilityApiEntity::getCreateTime)
                // 查询字段
                .select(AbilityApiEntity::getApiId, AbilityApiEntity::getApiName);
        return abilityApiService.list(queryWrapper);
    }


}
