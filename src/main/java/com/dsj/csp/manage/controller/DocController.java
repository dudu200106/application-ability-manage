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
import com.dsj.csp.manage.entity.DocCatalogEntity;
import com.dsj.csp.manage.entity.DocEntity;
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
@Tag(name = "文档管理", description = "用于管理卡法文档")
@RequiredArgsConstructor
public class DocController {

    private final DocService docService;
    private final UserApproveService userApproveService;

    @AopLogger(describe = "新增文档", operateType = LogEnum.INSERT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "新增文档")
    @PostMapping("/add")
    public Result<?> add(@RequestBody DocEntity doc){
        long cnt = docService.count(Wrappers.lambdaQuery(DocEntity.class)
                .eq(DocEntity::getDocName, doc.getDocName())
                .eq(DocEntity::getCatalogId, doc.getCatalogId()));
        if (cnt>0){
            throw new BusinessException("文档名称在该目录下已存在");
        }
        Boolean addFlag = docService.save(doc);
        return Result.success("文档保存" +(addFlag ? "成功!" : "失败!"));
    }

    @AopLogger(describe = "查看文档", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查看文档")
    @GetMapping("/info")
    public Result<?> info(Long docId){
        return Result.success(docService.getById(docId));
    }

    @AopLogger(describe = "分页查询文档", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "分页查询文档")
    @GetMapping("/page")
    public Result<?> page(@Parameter(description = "是否过滤未上线的文档") boolean onlyOnline,
                          @Parameter(description = "接口ID") Long apiId,
                          @Parameter(description = "当前页数", required = true) int current,
                          @Parameter(description = "分页条数", required = true) int size,
                          @Parameter(description = "开始时间") Date startTime,
                          @Parameter(description = "结束时间") Date endTime) {
        // 构造分页条件
        LambdaQueryWrapper<DocEntity> queryWrapper = Wrappers.lambdaQuery(DocEntity.class)
                .eq(apiId!=null, DocEntity::getApiId, apiId)
                .ge(Objects.nonNull(startTime), DocEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), DocEntity::getCreateTime, endTime)
                .notIn(onlyOnline, DocEntity::getStatus, 5)
                // 排序
                .orderByDesc(DocEntity::getCreateTime)
                .orderByAsc(DocEntity::getStatus);
        // 主表分页查询
        Page page = docService.page(new Page<>(current, size), queryWrapper);
        List<DocEntity> records = page.getRecords();
        Set<Long> catalogIds = records.stream().map(doc->doc.getCatalogId()).collect(Collectors.toSet());
        Map<Long, DocCatalogEntity> catalogMap= SimpleQuery.keyMap(Wrappers.lambdaQuery(DocCatalogEntity.class)
                .in(DocCatalogEntity::getCatalogId, catalogIds), DocCatalogEntity::getCatalogId);
        List<DocDto> resRecords = records.stream().map(doc -> {
            DocDto docDto = new DocDto();
            BeanUtil.copyProperties(doc, docDto);
            docDto.setCatalogName(catalogMap.get(doc.getCatalogId()).getCatalogName());
            return docDto;
        }).toList();
        page.setRecords(resRecords);
        return Result.success(page);
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
