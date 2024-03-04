package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.BusinessException;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.enums.DocStatusEnum;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.dto.DocCatalogDto;
import com.dsj.csp.manage.dto.convertor.DocCatalogConvertor;
import com.dsj.csp.manage.entity.DocCatalogEntity;
import com.dsj.csp.manage.entity.DocEntity;
import com.dsj.csp.manage.service.DocCatalogService;
import com.dsj.csp.manage.service.DocService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
@RequestMapping("/doc-catalog")
@Tag(name = "文档目录管理", description = "用于管理文档目录")
@RequiredArgsConstructor
public class DcoCatalogController {

    private final DocCatalogService docCatalogService;
    private final DocService docService;

    @AopLogger(describe = "新增文档目录", operateType = LogEnum.INSERT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "新增文档目录")
    @PostMapping("/add")
    @CacheEvict(allEntries = true, cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager")
    public Result<?> add(@RequestBody DocCatalogEntity catalogEntity){
        long cntCatalog = docCatalogService.count(Wrappers.lambdaQuery(DocCatalogEntity.class)
                .eq(DocCatalogEntity::getCatalogName, catalogEntity.getCatalogName()));
        if (cntCatalog>0){
            throw new BusinessException("文档目录名称已存在!");
        }
        Boolean addFlag = docCatalogService.save(catalogEntity);
        return Result.success(addFlag+"", "添加文档目录" + (addFlag ? "成功!" : "失败!"));
    }

//    @AopLogger(describe = "查看目录详情", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查看文档目录详情")
    @GetMapping("/info")
    public Result<?> queryInfo(@Parameter(description = "目录Id") Long catalogId){
        return Result.success(docCatalogService.getById(catalogId));
    }

//    @AopLogger(describe = "分页查询目录列表", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "分页查询文档目录列表")
    @GetMapping("/page")
    public Result<?> page(@Parameter(description = "当前页数") Integer current, @Parameter(description = "分页页数") Integer size){
        return Result.success(docCatalogService.page(new Page<>(current, size), Wrappers.lambdaQuery()));
    }

    @AopLogger(describe = "编辑文档目录", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "编辑文档目录")
    @PostMapping("/edit")
    @CacheEvict(allEntries = true, cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager")
    public Result<?> edit(@RequestBody DocCatalogEntity catalogEntity){
        boolean editFlag = docCatalogService.updateById(catalogEntity);
        return Result.success(editFlag + "", "编辑文档目录完毕!");
    }

    @AopLogger(describe = "删除文档目录", operateType = LogEnum.DELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "删除文档目录")
    @PostMapping("/delete")
    @Transactional
    @Caching(evict = {
            @CacheEvict(allEntries = true, cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager"),
            @CacheEvict(allEntries = true, cacheNames = "Doc", cacheManager = "caffeineCacheManager")
    })
    public Result<?> delete(@RequestBody DocCatalogEntity catalogEntity){
        // 删除目录下的所有文档
        docService.remove(Wrappers.lambdaQuery(DocEntity.class).eq(DocEntity::getCatalogId, catalogEntity.getCatalogId()));
        // 删除目录
        boolean delFlag = docCatalogService.removeById(catalogEntity.getCatalogId());
        return Result.success(delFlag+"", "删除文档目录完毕!");
    }

//    @AopLogger(describe = "查询所有目录及其文档列表", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查询所有文档目录及其文档列表")
    @GetMapping("/doc-list")
    @Cacheable(key = "'docList'", cacheNames = "DocCatalog", cacheManager = "caffeineCacheManager")
    public Result<?> queryAllCatalogAndDoc(){
        // 查出所有目录列表
        List<DocCatalogEntity> catalogs = docCatalogService.list();
        // 查出目录列表下的所有文档列表(一次性查出, 减少DB查询次数)
        Set<Long> catalogIds = catalogs.stream().map(DocCatalogEntity::getCatalogId).collect(Collectors.toSet());
        // 状态3: 已发布
        List<DocEntity> docs = docService.lambdaQuery()
                .select(DocEntity::getDocId, DocEntity::getCatalogId, DocEntity::getDocName)
                .in(DocEntity::getCatalogId, catalogIds)
                .eq(DocEntity::getStatus, DocStatusEnum.PUBLISHED.getCode())
                .list();
        Map<Long, List<DocEntity>> docEntityMap = new HashMap<>();
        // 将文档存入目录-文档键值对
        docs.forEach(doc -> {
            // 如果key不存在则初始化一个空列表
            docEntityMap.putIfAbsent(doc.getCatalogId(), new ArrayList<>());
            docEntityMap.get(doc.getCatalogId()).add(doc);
        });
        // 构造返回结果
        List<DocCatalogDto> catalogDtoList = catalogs.stream().map(catalog->{
            DocCatalogDto catalogDto = DocCatalogConvertor.INSTANCE.toDTO(catalog);
            List<DocEntity> docList = docEntityMap.getOrDefault(catalog.getCatalogId(), new ArrayList<>());
            catalogDto.setDocList(docList);
            return catalogDto;
        }).toList();
        return Result.success(catalogDtoList);
    }

}
