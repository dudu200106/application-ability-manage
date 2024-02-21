package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.biz.AbilityApiApplyBizService;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.dto.AbilityDeleteDTO;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api-apply")
@RequiredArgsConstructor
@Tag(name = "能力接口申请管理", description = "用于管理接口使用申请")
public class AbilityApiApplyController {

    // TODO  之前忘记划分控制层, 之后在进行接口的划分
    private final AbilityApiApplyService abilityApiApplyService;
//    private final AbilityApiApplyBizService abilityApiApplyBizService;
//
//    @Operation(summary = "新增接口使用申请", description = "新增接口使用申请")
//    @PostMapping("/add")
//    public Result<?> applyApi(@RequestBody AbilityApiApplyEntity apply, @RequestHeader("accessToken") String accessToken) {
//        abilityApiApplyBizService.saveApiApply(apply, accessToken);
//        return Result.success("能力申请完毕！请等待审核...");
//    }
//
//    @Operation(summary = "查看接口申请详情", description = "获取接口申请详情")
//    @GetMapping("/info")
//    public Result<?> getApiApplyInfo(@Parameter(
//            description = "能力申请ID") @RequestParam Long apiApplyId) {
//        return Result.success(abilityApiApplyBizService.getApplyInfo(apiApplyId));
//    }
//
//    @Operation(summary = "分页查询接口申请列表", description = "分页查询接口申请列表")
//    @GetMapping("/page")
//    public Result<?> pageApiApply(@Parameter(description = "是否屏蔽'未提交'状态申请") Boolean onlySubmitted,
//                                  @Parameter(description = "用户ID") Long userId,
//                                  @Parameter(description = "应用ID") Long appId,
//                                  @Parameter(description = "能力ID") Long abilityId,
//                                  @Parameter(description = "分页条数") int size,
//                                  @Parameter(description = "当前页数") int current,
//                                  @Parameter(description = "搜索关键字") String keyword,
//                                  @Parameter(description = "状态") Integer status,
//                                  @Parameter(description = "开始时间") Date startTime,
//                                  @Parameter(description = "结束时间") Date endTime) {
//        return Result.success(abilityApiApplyBizService.pageApiApply(onlySubmitted, appId, userId, abilityId, keyword, status, startTime, endTime, current, size));
//    }
//
//    @Operation(summary = "审核能力使用申请", description = "审核能力使用申请")
//    @PostMapping("/audit")
//    public Result<?> auditApiApply(@RequestBody AbilityAuditVO auditVO){
//        String  msg = abilityApiApplyBizService.auditApply(auditVO);
//        return Result.success(msg);
//    }
//
//    @Operation(summary = "编辑接口使用申请", description = "编辑接口使用申请")
//    @PostMapping("/edit")
//    public Result<?> editApiApply(@RequestBody AbilityApiApplyEntity apiApplyEntity){
//        LambdaUpdateWrapper<AbilityApiApplyEntity> updateWrapper = Wrappers.lambdaUpdate(AbilityApiApplyEntity.class)
//                .eq(AbilityApiApplyEntity::getApiApplyId, apiApplyEntity.getApiApplyId());
//        return Result.success(abilityApiApplyService.update(apiApplyEntity, updateWrapper));
//    }
//

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

//
//
//    @Operation(summary = "统计用户申请能力数量")
//    @GetMapping("/count-apply-ability")
//    public Result<?> countUserAbility(String userId){
//        return Result.success(abilityApiApplyService.countUserAbility(userId));
//    }
//
//    @Operation(summary = "统计用户申请接口数量")
//    @GetMapping("/count-apply-api")
//    public Result<?> countUserApi(String userId){
//        return Result.success(abilityApiApplyService.countUserApi(userId));
//    }

}
