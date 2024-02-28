package com.dsj.csp.manage.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.dsj.common.dto.BusinessException;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.aop.annotation.LoginAuthentication;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.biz.DocBizService;
import com.dsj.csp.manage.dto.DocDto;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.*;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityService;
import com.dsj.csp.manage.service.DocCatalogService;
import com.dsj.csp.manage.service.DocService;
import com.dsj.csp.manage.util.IdentifyUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-06
 */
@RestController
@RequestMapping("/doc")
@Tag(name = "文档管理", description = "用于管理文档")
@RequiredArgsConstructor
public class DocController {

    private final DocService docService;
    private final DocBizService docBizService;
    private final DocCatalogService docCatalogService;
    private final AbilityApiService abilityApiService;
    private final AbilityService abilityService;

    @AopLogger(describe = "新增文档", operateType = LogEnum.INSERT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "新增文档")
    @PostMapping("/add")
    @LoginAuthentication
    @Transactional
    @Caching(evict = {
            @CacheEvict(allEntries = true, cacheNames = "Doc", cacheManager = "caffeineCacheManager"),
            @CacheEvict(allEntries = true, cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager")
    })
    public Result<?> add(@RequestBody DocEntity doc){
        // 是否该目录下已存在同名文档
        long cntSameDoc = docService.count(Wrappers.lambdaQuery(DocEntity.class)
                .eq(DocEntity::getDocName, doc.getDocName())
                .eq(DocEntity::getCatalogId, doc.getCatalogId()));
        if (cntSameDoc>0){
            throw new BusinessException("文档名称在该目录下已存在!");
        }
        // 是否有接口与之关联
        if (doc.getApiId()!=null){
            // Api接口是否多次关联文档
            long cntMultipleApi = docService.count(Wrappers.lambdaQuery(DocEntity.class)
                    .eq(DocEntity::getApiId, doc.getApiId())
                    // 2审核不通过 5已下线
                    .notIn(DocEntity::getStatus, 2, 5));
            if (cntMultipleApi>0){
                throw new BusinessException("该文档关联的Api已被其他文档关联!");
            }
        }
        UserApproveRequest userApproveRequest = IdentifyUser.getUserInfo();
        doc.setCreator(userApproveRequest.getUserName());
        boolean addFlag = docService.save(doc);
        return Result.success("文档保存" +(addFlag ? "成功!" : "失败!"));
    }

//    @AopLogger(describe = "查看文档", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查看文档")
    @GetMapping("/info")
    @Cacheable(key = "'docId_' + #docId", cacheNames = "Doc", cacheManager = "caffeineCacheManager")
    public Result<?> info(Long docId){
        DocEntity doc = docService.getById(docId);
        if (doc == null){
            return Result.success("文档不存在!", doc);
        }
        DocDto docDto = new DocDto();
        BeanUtil.copyProperties(doc, docDto);
        DocCatalogEntity catalog = docCatalogService.getById(doc.getCatalogId());
        docDto.setCatalogName(catalog.getCatalogName());
        //文档是否关联了能力接口
        if (doc.getApiId()!=null){
            AbilityApiEntity api = abilityApiService.getById(doc.getApiId());
            AbilityEntity ability = abilityService.getById(api.getAbilityId());
            docDto.setApiName(api.getApiName());
            docDto.setAbilityId(api.getAbilityId());
            docDto.setAbilityName(ability.getAbilityName());
        }
        return Result.success(docDto);
    }

//    @AopLogger(describe = "分页查询文档", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "分页查询文档")
    @GetMapping("/page")
    @Transactional(propagation = Propagation.REQUIRED)
    public Result<?> page(@Parameter(description = "是否过滤未发布的文档") boolean onlySubmit,
                          @Parameter(description = "接口ID") Long apiId,
                          @Parameter(description = "目录ID") Long catalogId,
                          @Parameter(description = "接口ID") Integer status,
                          @Parameter(description = "查询关键字") String keyword,
                          @Parameter(description = "当前页数", required = true) int current,
                          @Parameter(description = "分页条数", required = true) int size,
                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", fallbackPatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"})
                          @Parameter(description = "开始时间") Date startTime,
                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", fallbackPatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"})
                          @Parameter(description = "结束时间") Date endTime) {
        // 构造分页条件
        LambdaQueryWrapper<DocEntity> queryWrapper = Wrappers.lambdaQuery(DocEntity.class)
                .eq(apiId!=null, DocEntity::getApiId, apiId)
                .eq(catalogId!=null, DocEntity::getCatalogId, catalogId)
                .eq(status!=null, DocEntity::getStatus, status)
                .ge(Objects.nonNull(startTime), DocEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), DocEntity::getCreateTime, endTime)
                .in(onlySubmit, DocEntity::getStatus, 3)
                // 排序
                .orderByDesc(DocEntity::getCreateTime)
                .orderByAsc(DocEntity::getStatus);
        // 关键字
        if (!ObjectUtil.isEmpty(keyword)){
            List<Long> catalogIds = docCatalogService.matchCatalogIdList(keyword.trim());
            queryWrapper.and(i -> i.like(DocEntity::getDocId, keyword)
                    .or().like(DocEntity::getDocName, keyword)
                    .or().like(DocEntity::getNote, keyword)
                    .or().in(catalogIds.size()>0, DocEntity::getCatalogId, catalogIds)
            );
        }
        // 主表分页查询
        Page<DocEntity> page = docService.page(new Page<>(current, size), queryWrapper);
        if (page.getRecords().isEmpty()){
            return Result.success(new Page<>(page.getCurrent(), page.getSize(), page.getTotal()));
        }
        List<DocEntity> records = page.getRecords();
        Set<Long> catalogIds = records.stream().map(DocEntity::getCatalogId).collect(Collectors.toSet());
        Set<Long> apiIds = records.stream().map(DocEntity::getApiId).collect(Collectors.toSet());
        Map<Long, DocCatalogEntity> catalogMap= SimpleQuery.keyMap(Wrappers.lambdaQuery(DocCatalogEntity.class)
                .in(DocCatalogEntity::getCatalogId, catalogIds), DocCatalogEntity::getCatalogId);
        Map<Long, AbilityApiEntity> apiMap= SimpleQuery.keyMap(Wrappers.lambdaQuery(AbilityApiEntity.class)
                .in(AbilityApiEntity::getApiId, apiIds), AbilityApiEntity::getApiId);
        List<Long> abilityIds = apiMap.values().stream().map(AbilityApiEntity::getAbilityId).toList();
        Map<Long, AbilityEntity> abilityMap= SimpleQuery.keyMap(Wrappers.lambdaQuery(AbilityEntity.class)
                .in(AbilityEntity::getAbilityId, abilityIds), AbilityEntity::getAbilityId);

        List<DocDto> resRecords = records.stream().map(doc -> {
            DocDto docDto = new DocDto();
            BeanUtil.copyProperties(doc, docDto);
            docDto.setCatalogName(catalogMap.getOrDefault(doc.getCatalogId(), new DocCatalogEntity()).getCatalogName());
            docDto.setApiName(apiMap.getOrDefault(doc.getApiId(), new AbilityApiEntity()).getApiName());
            Long abilityId = apiMap.getOrDefault(doc.getApiId(), new AbilityApiEntity()).getAbilityId();
            docDto.setAbilityId(abilityId);
            docDto.setAbilityName(abilityMap.getOrDefault(abilityId, new AbilityEntity()).getAbilityName());
            return docDto;
        }).toList();
        // 初始化返回分页
        Page<DocDto> resPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resPage.setRecords(resRecords);
        return Result.success(resPage);
    }

    @AopLogger(describe = "提交文档", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "提交文档")
    @PostMapping("/audit-submit")
    @LoginAuthentication
    public Result<?> auditSubmit(@RequestBody DocEntity doc){
        docBizService.auditSubmit(doc.getDocId());
        return Result.success("文档提交完成!");
    }

    @AopLogger(describe = "撤回文档", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "撤回文档")
    @PostMapping("/audit-withdraw")
    @LoginAuthentication
    public Result<?> auditWithdraw(@RequestBody DocEntity doc){
        docBizService.auditWithdraw(doc.getDocId());
        return Result.success("文档撤回完成!");
    }

    @AopLogger(describe = "文档审核通过", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "文档审核通过")
    @PostMapping("/audit-pass")
    @LoginAuthentication
    public Result<?> auditPass(@RequestBody DocEntity doc){
        docBizService.auditPass(doc.getDocId(), doc.getNote());
        return Result.success("文档审核通过!");
    }

    @AopLogger(describe = "文档审核不通过", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "文档审核不通过")
    @PostMapping("/audit-not-pass")
    @LoginAuthentication
    public Result<?> auditNotPass(@RequestBody DocEntity doc){
        docBizService.auditNotPass(doc.getDocId(), doc.getNote());
        return Result.success("文档审核不通过!");
    }


    @AopLogger(describe = "发布文档", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "发布文档")
    @PostMapping("/audit-publish")
    @LoginAuthentication
    public Result<?> auditPublish(@RequestBody DocEntity doc){
        docBizService.auditPublish(doc.getDocId(), null);
        return Result.success("文档发布成功!");
    }

//     // 取消已上线状态
//    @AopLogger(describe = "上线文档", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
//    @Operation(summary = "上线文档")
//    @PostMapping("/audit-online")
//    public Result<?> auditOnline(@RequestBody DocEntity doc, @RequestHeader("accessToken") String accessToken){
//        String operatorName = userApproveService.identify(accessToken).getUserName();
//        docBizService.auditOnline(doc.getDocId(), doc.getNote(), operatorName);
//        return Result.success("文档上线成功!");
//    }

    @AopLogger(describe = "下线文档", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "下线文档")
    @PostMapping("/audit-offline")
    @LoginAuthentication
    public Result<?> abortPublish(@RequestBody DocEntity doc){
        docBizService.auditOffline(doc.getDocId(), doc.getNote());
        return Result.success("文档下线成功!");
    }

    @AopLogger(describe = "编辑文档", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "编辑文档")
    @PostMapping("/edit")
    @LoginAuthentication
    @CachePut(key = "'docId_' + #doc.getDocId()", cacheNames = "Doc", cacheManager = "caffeineCacheManager")
    public Result<?> edit(@RequestBody DocEntity doc){
        doc.setUpdateTime(new Date());
        boolean editFlag = docService.updateById(doc);
        return Result.success("文档编辑完成!", editFlag );
    }

    @AopLogger(describe = "删除文档", operateType = LogEnum.DELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "删除文档")
    @PostMapping("/delete")
    @LoginAuthentication
    @Caching(evict = {
            @CacheEvict(allEntries = true, cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager"),
            @CacheEvict(key = "'docId_' + #docEntity.getDocId()", cacheNames = "Doc", cacheManager = "caffeineCacheManager")
    })
    public Result<?> delete(@RequestBody DocEntity docEntity){
        boolean deleteFlag = docService.removeById(docEntity);
        return Result.success(deleteFlag+"", "文档删除完成!");
    }

}
