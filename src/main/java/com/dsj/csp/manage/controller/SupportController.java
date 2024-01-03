package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.converter.SupportConverter;
import com.dsj.csp.manage.dto.SupportDto;
import com.dsj.csp.manage.dto.request.SupportAcceptRequest;
import com.dsj.csp.manage.dto.request.SupportQueryRequest;
import com.dsj.csp.manage.dto.request.SupportReplyRequest;
import com.dsj.csp.manage.dto.request.SupportUpdateRequest;
import com.dsj.csp.manage.dto.response.SupportCommunicationHistoryResponse;
import com.dsj.csp.manage.dto.response.SupportQueryResponse;
import com.dsj.csp.manage.entity.SupportEntity;
import com.dsj.csp.manage.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/26 15:44
 */
@RestController
@RequestMapping("/system/support")
public class SupportController {
    @Autowired
    private SupportService supportService;

    @GetMapping("/list")
    public Result<SupportQueryResponse> list(SupportQueryRequest request) {
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

    @GetMapping(value = "/{supportId}")
    public Result<SupportDto> getInfo(@PathVariable("supportId") Long supportId) {
        return Result.success(SupportConverter.toSupportDto(supportService.selectSupportById(supportId)));
    }

    @PostMapping(value = "/{supportId}/accept")
    public Result<SupportDto> accept(@PathVariable Long supportId, @RequestBody SupportAcceptRequest request) {
        return Result.success(SupportConverter.toSupportDto(supportService.acceptSupport(supportId, request)));
    }

    @PostMapping(value = "/reply")
    public Result<SupportCommunicationHistoryResponse> reply(@RequestBody SupportReplyRequest request) {
        return Result.success(supportService.replySupport(request));
    }

    @GetMapping(value = "/{supportId}/communication")
    public Result<SupportCommunicationHistoryResponse> getCommunicationInfo(@PathVariable("supportId") Long supportId,
                                                                            @RequestParam(value = "lastCommunicationId", required = false) Long lastCommunicationId) {
        return Result.success(supportService.getCommunicationBySupportId(supportId, lastCommunicationId));
    }

    @PutMapping(value = "/{supportId}")
    public Result<SupportDto> update(@PathVariable Long supportId, @RequestBody SupportUpdateRequest request) {
        return Result.success("更新成功", SupportConverter.toSupportDto(supportService.updateSupport(supportId, request)));
    }

    @DeleteMapping("/{supportId}")
    public Result<Void> remove(@PathVariable Long supportId) {
        supportService.deleteSupportById(supportId);
        return Result.success("删除成功", null);
    }

    @PostMapping("/{supportId}/finish")
    public Result<SupportDto> supportFinish(@PathVariable Long supportId) {
        return Result.success(SupportConverter.toSupportDto(supportService.supportFinish(supportId)));
    }
}
