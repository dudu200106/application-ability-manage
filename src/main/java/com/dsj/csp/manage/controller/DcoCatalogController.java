package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.BusinessException;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.entity.DocCatalogEntity;
import com.dsj.csp.manage.entity.DocEntity;
import com.dsj.csp.manage.service.DocCatalogService;
import com.dsj.csp.manage.service.DocService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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

}
