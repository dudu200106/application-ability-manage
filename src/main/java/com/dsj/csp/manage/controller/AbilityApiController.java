package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.dto.AbilityDeleteDTO;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import com.dsj.csp.manage.service.AbilityApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Tag(name = "能力接口管理", description = "用于管理能力api接口")
public class AbilityApiController {

    private final AbilityApiService abilityApiService;
    private final AbilityApiBizService abilityApiBizService;
    private final AbilityApiApplyService abilityApiApplyService;

    @Operation(summary = "新增接口")
    @PostMapping("add-api")
    public Result<?> addApi(@RequestBody AbilityApiVO apiVO, @RequestHeader("accessToken") String accessToken){
        abilityApiBizService.saveApi(apiVO, accessToken);
        return Result.success("添加接口成功!");
    }

    @Operation(summary = "审核接口注册", description = "审核接口注册")
    @PostMapping("/audit")
    public Result<?> auditApi(@RequestBody AbilityAuditVO auditVO){
        String  msg = abilityApiService.auditApi(auditVO);
        return Result.success(msg);
    }

    @Operation(summary = "查询接口信息", description = "查询特定接口的信息")
    @GetMapping("/info")
    public Result<?> queryApiInfo(@Parameter(description = "接口ID") @RequestParam Long apiId) {
        return Result.success(abilityApiBizService.getApiInfo(apiId));
    }

    @Operation(summary = "更新接口")
    @PostMapping("edit")
    public Result<?> editApi(@RequestBody AbilityApiVO apiVO){
        Boolean editApiflag = abilityApiBizService.updateApi(apiVO);
        return Result.success("已修改接口完毕! ", editApiflag);
    }

    @Operation(summary = "删除能力接口")
    @PostMapping("/delete")
    public Result<?> removeApi(@RequestBody AbilityDeleteDTO deleteDTO){
        String apiIds = deleteDTO.getApiIds();
        List<Long> ids = Arrays.asList(apiIds.split(",")).stream().map(id -> Long.parseLong(id)).toList();
        Boolean delFlag = abilityApiService.removeBatchByIds(ids);
        return Result.success("删除能力完成! ", delFlag);
    }

    @Operation(summary = "统计接口数量")
    @GetMapping("/count-api")
    public Result<?> countApi(){
        return Result.success(abilityApiService.count());
    }


    @Operation(summary = "查询能力下的接口列表", description = "查询能力下的接口列表")
    @GetMapping("/query-ability-apis")
    public Result<?> queryApiList(@Parameter(description = "能力ID") @RequestParam Long abilityId) {
        return Result.success(abilityApiBizService.getAbilityApiList(abilityId));
    }

    @Operation(summary = "查询用户申请到的api列表")
    @GetMapping("/query-user-apis")
    public Result<?> queryUserApis(@Parameter(description = "用户ID") @RequestParam Long userId) {
        return Result.success(abilityApiBizService.getUserApiList(userId));
    }

    @Operation(summary = "查询应用申请到的api列表")
    @GetMapping("/query-app-apis")
    public Result<?> queryAppApis(@Parameter(description = "应用ID") @RequestParam Long appId) {
        return Result.success(abilityApiBizService.getAppApiList(appId));
    }


    @Operation(summary = "分页查询api目录列表")
    @GetMapping("page-api-catalog")
    public Result<?> pageApiList(@Parameter(description = "是否过滤未发布的接口") Boolean onlyPublished,
                                 @Parameter(description = "请求方式") String reqMethod,
                                 @Parameter(description = "状态") Integer status,
                                 @Parameter(description = "用户ID") Long userId,
                                 @Parameter(description = "能力ID") Long abilityId,
                                 @Parameter(description = "分页条数") int size,
                                 @Parameter(description = "当前页数") int current,
                                 @Parameter(description = "搜索关键字") String keyword,
                                 @Parameter(description = "开始时间") Date startTime,
                                 @Parameter(description = "结束时间") Date endTime) {
        return Result.success(abilityApiBizService.pageApiCatalog(onlyPublished, reqMethod, status, userId, abilityId, keyword, size, current, startTime, endTime));
    }

    @Operation(summary = "分页查询申请通过的接口列表", description = "分页查询申请通过的接口列表")
    @GetMapping("/page-passed-apis")
    public Result<?> pagePassedApi(@Parameter(description = "用户ID") Long userId,
                                   @Parameter(description = "应用ID") Long appId,
                                   @Parameter(description = "能力ID") Long abilityId,
                                   @Parameter(description = "分页条数", required = true) int size,
                                   @Parameter(description = "当前页数", required = true) int current,
                                   @Parameter(description = "搜索关键字") String keyword,
                                   @Parameter(description = "开始时间") Date startTime,
                                   @Parameter(description = "结束时间") Date endTime) {
        return Result.success(abilityApiBizService.pagePassedApis(userId, appId, abilityId, keyword, current, size, startTime, endTime));
    }


}
