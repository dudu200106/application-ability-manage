package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.biz.AbilityApiApplyBizService;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.dto.AbilityDeleteDTO;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/apiApply")
@RequiredArgsConstructor
public class AbilityApiApplyController {

    private final AbilityApiApplyService abilityApiApplyService;
    private final AbilityApiApplyBizService abilityApiApplyBizService;

    @Operation(summary = "新增接口使用申请", description = "新增接口使用申请")
    @PostMapping("/add-api-apply")
    public Result<?> applyApi(@RequestBody AbilityApiApplyEntity apply) {
        abilityApiApplyBizService.saveApiApply(apply);
        return Result.success("能力申请完毕！请等待审核...");
    }

    @Operation(summary = "查看接口申请详情", description = "获取接口申请详情")
    @GetMapping("/info-api-apply")
    public Result<?> getApiApplyInfo(@Parameter(
            description = "能力申请ID") @RequestParam Long apiApplyId) {
        return Result.success(abilityApiApplyBizService.getApplyInfo(apiApplyId));
    }

    @Operation(summary = "分页查询接口申请列表", description = "分页查询接口申请列表")
    @GetMapping("/page-api-apply")
    public Result<?> pageApiApply(@Parameter(description = "是否屏蔽'未提交'状态申请") Boolean onlySubmitted,
                                  @Parameter(description = "用户ID") Long userId,
                                  @Parameter(description = "应用ID") Long appId,
                                  @Parameter(description = "能力ID") Long abilityId,
                                  @Parameter(description = "分页条数") int size,
                                  @Parameter(description = "当前页数") int current,
                                  @Parameter(description = "搜索关键字") String keyword,
                                  @Parameter(description = "状态") Integer status,
                                  @Parameter(description = "开始时间") Date startTime,
                                  @Parameter(description = "结束时间") Date endTime) {
        return Result.success(abilityApiApplyBizService.pageApiApply(onlySubmitted, appId, userId, abilityId, keyword, status, startTime, endTime, current, size));
    }

    @Operation(summary = "审核能力使用申请", description = "审核能力使用申请")
    @PostMapping("/audit-api-apply")
    public Result<?> auditApiApply(@RequestBody AbilityAuditVO auditVO){
        String  msg = abilityApiApplyBizService.auditApply(auditVO);
        return Result.success(msg);
    }

    @Operation(summary = "编辑接口使用申请", description = "编辑接口使用申请")
    @PostMapping("/edit-api-apply")
    public Result<?> editApiApply(@RequestBody AbilityApiApplyEntity apiApplyEntity){
        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate(AbilityApiApplyEntity.class)
                .eq(AbilityApiApplyEntity::getApiApplyId, apiApplyEntity.getApiApplyId());
        return Result.success(abilityApiApplyService.update(apiApplyEntity, updateWrapper));
    }

    @Operation(summary = "删除接口申请")
    @PostMapping("/delete-api-apply")
    public Result<?> removeApiApply(@RequestBody AbilityDeleteDTO deleteDTO){
        String apiApplyIds = deleteDTO.getApiApplyIds();
        List<Long> ids = Arrays.asList(apiApplyIds.split(",")).stream().map(id -> Long.parseLong(id)).toList();
        Boolean delFlag = abilityApiApplyService.removeBatchByIds(ids);
        return Result.success("删除能力申请完成! ", delFlag);
    }


    @Operation(summary = "统计用户申请能力数量")
    @GetMapping("/count-user-ability")
    public Result<?> countuserAbility(String userId){
        return Result.success(abilityApiApplyService.countUserAbility(userId));
    }

    @Operation(summary = "统计用户申请接口数量")
    @GetMapping("/count-user-api")
    public Result<?> countUserApi(String userId){
        return Result.success(abilityApiApplyService.countUserApi(userId));
    }

}
