package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.biz.AbilityApiApplyBizService;
import com.dsj.csp.manage.dto.AbilityDeleteDTO;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import com.dsj.csp.manage.util.IdentifyUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api-apply")
@RequiredArgsConstructor
@Tag(name = "能力接口申请管理", description = "用于管理接口使用申请")
public class AbilityApiApplyController {

    // TODO  之前忘记划分控制层, 之后在进行接口的划分
    private final AbilityApiApplyService abilityApiApplyService;
    private final AbilityApiApplyBizService abilityApiApplyBizService;

    @AopLogger(describe = "批量申请使用接口", operateType = LogEnum.INSERT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "批量申请使用接口", description = "批量申请使用接口")
    @PostMapping("/add-batch")
    public Result<?> applyApiBatch(@RequestBody List<AbilityApiApplyEntity> applyList) {
        UserApproveRequest userApproveRequest = IdentifyUser.getUserInfo();
        abilityApiApplyBizService.saveApiApplyBatch(applyList, userApproveRequest);
        return Result.success("能力申请完毕！请等待审核...");
    }

    @AopLogger(describe = "批量删除接口申请", operateType = LogEnum.DELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "批量删除接口申请")
    @PostMapping("/delete-batch")
    public Result<?> removeApiApplyBatch(@RequestBody AbilityDeleteDTO deleteDTO){
        String apiApplyIds = deleteDTO.getApiApplyIds();
        List<Long> ids = Arrays.stream(apiApplyIds.split(",")).map(Long::parseLong).toList();
        Boolean delFlag = abilityApiApplyService.removeBatchByIds(ids);
        return Result.success("批量删除能力申请完成! ", delFlag);
    }

    @AopLogger(describe = "删除接口申请", operateType = LogEnum.DELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "删除接口申请")
    @PostMapping("/delete")
    public Result<?> removeApiApply(@RequestBody AbilityApiApplyEntity apiApplyEntity){
        Boolean delFlag = abilityApiApplyService.removeById(apiApplyEntity.getApiApplyId());
        return Result.success("删除接口申请完成! ", delFlag);
    }

}
