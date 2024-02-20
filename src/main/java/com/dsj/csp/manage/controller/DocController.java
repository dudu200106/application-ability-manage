package com.dsj.csp.manage.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.dsj.common.dto.BusinessException;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.dto.DocDto;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.DocCatalogEntity;
import com.dsj.csp.manage.entity.DocEntity;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.DocCatalogService;
import com.dsj.csp.manage.service.DocService;
import com.dsj.csp.manage.service.UserApproveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    private final DocCatalogService docCatalogService;
    private final UserApproveService userApproveService;
    private final AbilityApiService abilityApiService;

    @AopLogger(describe = "新增文档", operateType = LogEnum.INSERT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "新增文档")
    @PostMapping("/add")
    public Result<?> add(@RequestBody DocEntity doc){
        // 是否该目录下已存在同名文档
        long cntSameDoc = docService.count(Wrappers.lambdaQuery(DocEntity.class)
                .eq(DocEntity::getDocName, doc.getDocName())
                .eq(DocEntity::getCatalogId, doc.getCatalogId()));
        if (cntSameDoc>0){
            throw new BusinessException("文档名称在该目录下已存在!");
        }
        if (doc.getApiId()!=null){
            // Api接口是否多次关联文档
            long cntMultipleApi = docService.count(Wrappers.lambdaQuery(DocEntity.class)
                    .eq(DocEntity::getApiId, doc.getApiId()));
            if (cntMultipleApi>0){
                throw new BusinessException("该文档关联的Api已被其他文档关联!");
            }
        }
        boolean addFlag = docService.save(doc);
        return Result.success("文档保存" +(addFlag ? "成功!" : "失败!"));
    }

    @AopLogger(describe = "查看文档", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查看文档")
    @GetMapping("/info")
    public Result<?> info(Long docId){
        DocEntity doc = docService.getById(docId);
        DocDto docDto = new DocDto();
        BeanUtil.copyProperties(doc, docDto);
        DocCatalogEntity catalog = docCatalogService.getById(doc.getCatalogId());
        AbilityApiEntity api = abilityApiService.getById(doc.getApiId());
        docDto.setCatalogName(catalog.getCatalogName());
        docDto.setApiName(api.getApiName());
        return Result.success(docDto);
    }

    @AopLogger(describe = "分页查询文档", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "分页查询文档")
    @GetMapping("/page")
    public Result<?> page(@Parameter(description = "是否过滤未上线的文档") boolean onlyOnline,
                          @Parameter(description = "接口ID") Long apiId,
                          @Parameter(description = "目录ID") Long catalogId,
                          @Parameter(description = "接口ID") Integer status,
                          @Parameter(description = "当前页数", required = true) int current,
                          @Parameter(description = "分页条数", required = true) int size,
                          @Parameter(description = "开始时间") Date startTime,
                          @Parameter(description = "结束时间") Date endTime) {
        // 构造分页条件
        LambdaQueryWrapper<DocEntity> queryWrapper = Wrappers.lambdaQuery(DocEntity.class)
                .eq(apiId!=null, DocEntity::getApiId, apiId)
                .eq(catalogId!=null, DocEntity::getCatalogId, catalogId)
                .eq(status!=null, DocEntity::getStatus, status)
                .ge(Objects.nonNull(startTime), DocEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), DocEntity::getCreateTime, endTime)
                .notIn(onlyOnline, DocEntity::getStatus, 5)
                // 排序
                .orderByDesc(DocEntity::getCreateTime)
                .orderByAsc(DocEntity::getStatus);
        // 主表分页查询
        Page<DocEntity> page = docService.page(new Page<>(current, size), queryWrapper);
        List<DocEntity> records = page.getRecords();
        Set<Long> catalogIds = records.stream().map(DocEntity::getCatalogId).collect(Collectors.toSet());
        Set<Long> apiIds = records.stream().map(DocEntity::getApiId).collect(Collectors.toSet());
        Map<Long, DocCatalogEntity> catalogMap= SimpleQuery.keyMap(Wrappers.lambdaQuery(DocCatalogEntity.class)
                .in(DocCatalogEntity::getCatalogId, catalogIds), DocCatalogEntity::getCatalogId);
        Map<Long, AbilityApiEntity> apiMap= SimpleQuery.keyMap(Wrappers.lambdaQuery(AbilityApiEntity.class)
                .in(AbilityApiEntity::getApiId, apiIds), AbilityApiEntity::getApiId);
        List<DocDto> resRecords = records.stream().map(doc -> {
            DocDto docDto = new DocDto();
            BeanUtil.copyProperties(doc, docDto);
            docDto.setCatalogName(catalogMap.get(doc.getCatalogId())==null ? null : catalogMap.get(doc.getCatalogId()).getCatalogName());
            docDto.setApiName(apiMap.get(doc.getApiId())==null ? null : apiMap.get(doc.getApiId()).getApiName());
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
    public Result<?> auditSubmit(@RequestBody DocEntity doc, @RequestHeader("accessToken") String accessToken){
        // Token验证
        userApproveService.identify(accessToken).getUserName();
        docService.auditSubmit(doc.getDocId());
        return Result.success("文档提交完成!");
    }

    @AopLogger(describe = "撤回文档", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "撤回文档")
    @PostMapping("/audit-withdraw")
    public Result<?> auditWithdraw(@RequestBody DocEntity doc, @RequestHeader("accessToken") String accessToken){
        // Token验证
        userApproveService.identify(accessToken).getUserName();
        docService.auditWithdraw(doc.getDocId());
        return Result.success("文档撤回完成!");
    }

    @AopLogger(describe = "文档审核通过", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "文档审核通过")
    @PostMapping("/audit-pass")
    public Result<?> auditPass(@RequestBody DocEntity doc, @RequestHeader("accessToken") String accessToken){
        String operatorName = userApproveService.identify(accessToken).getUserName();
        docService.auditPass(doc.getDocId(), doc.getNote(), operatorName);
        return Result.success("文档审核通过!");
    }

    @AopLogger(describe = "文档审核不通过", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "文档审核不通过")
    @PostMapping("/audit-not-pass")
    public Result<?> auditNotPass(@RequestBody DocEntity doc, @RequestHeader("accessToken") String accessToken){
        String operatorName = userApproveService.identify(accessToken).getUserName();
        docService.auditNotPass(doc.getDocId(), doc.getNote(), operatorName);
        return Result.success("文档审核不通过!");
    }


    @AopLogger(describe = "发布文档", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "发布文档")
    @PostMapping("/audit-publish")
    public Result<?> auditPublish(@RequestBody DocEntity doc, @RequestHeader("accessToken") String accessToken){
        String operatorName = userApproveService.identify(accessToken).getUserName();
        docService.auditPublish(doc.getDocId(), null, operatorName);
        return Result.success("文档发布成功!");
    }

    @AopLogger(describe = "上线文档", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "上线文档")
    @PostMapping("/audit-online")
    public Result<?> auditOnline(@RequestBody DocEntity doc, @RequestHeader("accessToken") String accessToken){
        String operatorName = userApproveService.identify(accessToken).getUserName();
        docService.auditOnline(doc.getDocId(), doc.getNote(), operatorName);
        return Result.success("文档上线成功!");
    }

    @AopLogger(describe = "下线文档", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "下线文档")
    @PostMapping("/audit-offline")
    public Result<?> abortPublish(@RequestBody DocEntity doc, @RequestHeader("accessToken") String accessToken){
        String operatorName = userApproveService.identify(accessToken).getUserName();
        docService.auditOffline(doc.getDocId(), doc.getNote(), operatorName);
        return Result.success("文档下线成功!");
    }

    @AopLogger(describe = "编辑文档", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "编辑文档")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody DocEntity docEntity){
        docEntity.setUpdateTime(new Date());
        boolean editFlag = docService.updateById(docEntity);
        return Result.success("文档编辑完成!", editFlag );
    }

    @AopLogger(describe = "删除文档", operateType = LogEnum.DELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "删除文档")
    @PostMapping("/delete")
    public Result<?> delete(@RequestBody DocEntity docEntity){
        boolean deleteFlag = docService.removeById(docEntity);
        return Result.success("文档删除完成!", deleteFlag);
    }

}
