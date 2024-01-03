package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.converter.SupportConverter;
import com.dsj.csp.manage.dto.SupportDto;
import com.dsj.csp.manage.dto.request.*;
import com.dsj.csp.manage.dto.response.SupportCommunicationHistoryResponse;
import com.dsj.csp.manage.dto.response.SupportQueryResponse;
import com.dsj.csp.manage.entity.SupportEntity;
import com.dsj.csp.manage.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/26 15:44
 */
@Tag(name = "工单管理")
@RestController
@RequestMapping("/support")
public class SupportController {
    @Autowired
    private SupportService supportService;

    @Operation(summary = "获取工单列表")
    @PostMapping("/list")
    public Result<SupportQueryResponse> list(@RequestBody SupportQueryRequest request) {
        Page<SupportEntity> list = supportService.selectSupportList(request);
        List<SupportDto> dtoList = SupportConverter.toSupportDtoList(list.getRecords());
        return Result.success(
                new SupportQueryResponse()
                        .setPageNum(request.getPageNum())
                        .setPageSize(request.getPageSize())
                        .setTotal(list.getTotal())
                        .setList(dtoList)
        );
    }

    @Operation(summary = "获取工单详情")
    @GetMapping(value = "/{supportId}")
    public Result<SupportDto> getInfo(@Parameter(description = "工单ID") @PathVariable("supportId") Long supportId) {
        return Result.success(SupportConverter.toSupportDto(supportService.selectSupportById(supportId)));
    }

    @Operation(summary = "受理工单")
    @PostMapping(value = "/{supportId}/accept")
    public Result<SupportDto> accept(@Parameter(description = "工单ID") @PathVariable Long supportId, @RequestBody SupportAcceptRequest request) {
        return Result.success(SupportConverter.toSupportDto(supportService.acceptSupport(supportId, request)));
    }

    @Operation(summary = "创建工单")
    @PostMapping
    public Result<SupportDto> create(@RequestBody SupportCreateRequest request) {
        return Result.success("创建成功", SupportConverter.toSupportDto(supportService.createSupport(request)));
    }

    @Operation(summary = "更新工单")
    @PutMapping(value = "/{supportId}")
    public Result<SupportDto> update(@Parameter(description = "工单ID") @PathVariable Long supportId, @RequestBody SupportUpdateRequest request) {
        return Result.success("更新成功", SupportConverter.toSupportDto(supportService.updateSupport(supportId, request)));
    }

    @Operation(summary = "删除工单")
    @DeleteMapping("/{supportId}")
    public Result<Void> remove(@Parameter(description = "工单ID") @PathVariable Long supportId) {
        supportService.deleteSupportById(supportId);
        return Result.success("删除成功", null);
    }

    @Operation(summary = "完成工单")
    @PostMapping("/{supportId}/finish")
    public Result<SupportDto> supportFinish(@Parameter(description = "工单ID") @PathVariable Long supportId) {
        return Result.success(SupportConverter.toSupportDto(supportService.supportFinish(supportId)));
    }

    @Operation(summary = "回复工单")
    @PostMapping(value = "/{supportId}/reply")
    public Result<SupportCommunicationHistoryResponse> reply(@Parameter(description = "工单ID") @PathVariable Long supportId, @RequestBody SupportReplyRequest request) {
        return Result.success(supportService.replySupport(supportId, request));
    }

    @Operation(summary = "获取工单沟通记录")
    @GetMapping(value = "/{supportId}/communication")
    public Result<SupportCommunicationHistoryResponse> getCommunicationInfo(@Parameter(description = "工单ID") @PathVariable("supportId") Long supportId,
                                                                            @Parameter(description = "最后一条记录ID") @RequestParam(value = "lastCommunicationId", required = false) Long lastCommunicationId) {
        return Result.success(supportService.getCommunicationBySupportId(supportId, lastCommunicationId));
    }
}
