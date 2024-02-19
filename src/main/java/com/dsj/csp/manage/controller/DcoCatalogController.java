package com.dsj.csp.manage.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.dsj.common.dto.BusinessException;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.dto.DocCatalogDto;
import com.dsj.csp.manage.entity.DocCatalogEntity;
import com.dsj.csp.manage.entity.DocEntity;
import com.dsj.csp.manage.service.DocCatalogService;
import com.dsj.csp.manage.service.DocService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-06
 */
@RestController
@RequestMapping("/doc-catalog")
@Tag(name = "文档目录管理", description = "")
@RequiredArgsConstructor
public class DcoCatalogController {

    private final DocCatalogService docCatalogService;
    private final DocService docService;

    @AopLogger(describe = "新增目录", operateType = LogEnum.INSERT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "新增目录")
    @PostMapping("/add")
    public Result<?> add(@RequestBody DocCatalogEntity catalogEntity){
        Long cntCatalog = docCatalogService.count(Wrappers.lambdaQuery(DocCatalogEntity.class)
                .eq(DocCatalogEntity::getCatalogName, catalogEntity.getCatalogName()));
        if (cntCatalog>0){
            throw new BusinessException("目录名称已存在!");
        }
        Boolean addFlag = docCatalogService.save(catalogEntity);
        return Result.success("添加文档目录" + (addFlag ? "成功!" : "失败!"), addFlag);
    }

    @AopLogger(describe = "查看目录详情", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查看目录详情")
    @GetMapping("/info")
    public Result<?> queryInfo(@Parameter(description = "目录Id") Long catalogId){
        return Result.success(docCatalogService.getById(catalogId));
    }

    @AopLogger(describe = "分页查询目录列表", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "分页查询目录列表")
    @GetMapping("/page")
    public Result<?> page(@Parameter(description = "当前页数") Integer current, @Parameter(description = "分页页数") Integer size){
        return Result.success(docCatalogService.page(new Page<>(current, size), Wrappers.lambdaQuery()));
    }

    @AopLogger(describe = "编辑目录", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "编辑目录")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody DocCatalogEntity catalogEntity){
        boolean editFlag = docCatalogService.updateById(catalogEntity);
        return Result.success("编辑目录" + (editFlag ? "成功!" : "失败!"), editFlag);
    }

    @AopLogger(describe = "删除目录", operateType = LogEnum.DELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "删除目录")
    @PostMapping("/delete")
    public Result<?> delete(@RequestBody DocCatalogEntity catalogEntity){
        // 删除目录下的所有文档
        docService.remove(Wrappers.lambdaQuery(DocEntity.class).eq(DocEntity::getCatalogId, catalogEntity.getCatalogId()));
        // 删除目录
        boolean delFlag = docCatalogService.removeById(catalogEntity.getCatalogId());
        return Result.success("删除目录" + (delFlag ? "成功!" : "失败!"), delFlag);
    }

    @AopLogger(describe = "查询所有目录及其文档列表", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查询所有目录及其文档列表")
    @GetMapping("/doc-list")
    public Result<?> queryAllCatalogAndDoc(){
        // 查出所有目录列表
        List<DocCatalogEntity> catalogs = docCatalogService.list();
        // 查出目录列表下的所有文档列表(一次性查出, 减少DB查询次数)
        Set<Long> catalogIds = catalogs.stream().map(DocCatalogEntity::getCatalogId).collect(Collectors.toSet());
        List<DocEntity> docs = docService.list(Wrappers.lambdaQuery(DocEntity.class)
                .select(DocEntity::getDocId, DocEntity::getCatalogId, DocEntity::getDocName)
                .in(DocEntity::getCatalogId, catalogIds));
        Map<Long, List<DocEntity>> docEntityMap = new HashMap<>();
        docs.forEach(doc -> {
            // 如果key不存在则初始化一个空列表
            docEntityMap.putIfAbsent(doc.getCatalogId(), new ArrayList<>());
            docEntityMap.get(doc.getCatalogId()).add(doc);
        });
        // 构造返回结果
        List<DocCatalogDto> catalogDtoList = catalogs.stream().map(catalog->{
            DocCatalogDto catalogDto = new DocCatalogDto();
            BeanUtil.copyProperties(catalog, catalogDto);
            List<DocEntity> docList = docEntityMap.getOrDefault(catalog.getCatalogId(), new ArrayList<>());
            catalogDto.setDocList(docList);
            return catalogDto;
        }).toList();
        return Result.success(catalogDtoList);
    }

}
