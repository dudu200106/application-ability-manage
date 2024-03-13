package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.aop.annotation.LoginAuthentication;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.biz.GatewayAdminBizService;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
@Tag(name = "能力接口管理", description = "用于管理能力api接口")
public class AbilityApiController {

    private final AbilityApiBizService abilityApiBizService;
    private final GatewayAdminBizService gatewayAdminBizService;

//    @AopLogger(describe = "查询接口简单目录", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查询接口简单目录", description = "查询接口简单目录")
    @GetMapping("/get-api-catalog")
    public Result<?> getApiCatalog(@Parameter(description = "是否过滤未发布的接口") boolean onlyPublished,
                                   @Parameter(description = "请求方式") String reqMethod,
                                   @Parameter(description = "状态") Integer status,
                                   @Parameter(description = "用户ID") Long userId,
                                   @Parameter(description = "能力ID") Long abilityId) {
        return Result.success(abilityApiBizService.getApiCatalog(onlyPublished, reqMethod, status, userId, abilityId));
    }

    @AopLogger(describe = "删除接口", operateType = LogEnum.DELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "删除接口")
    @PostMapping("/delete-batch")
    @LoginAuthentication
    public Result<?> removeApiBatch(@RequestBody List<AbilityApiEntity> apiEntityList){
        boolean delFlag = abilityApiBizService.deleteApiBatch(apiEntityList);
        return Result.success(delFlag+"", "删除接口完成!");
    }

//    @AopLogger(describe = "批量审核接口注册", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
//    @Operation(summary = "批量审核接口注册", description = "批量审核接口注册")
//    @PostMapping("/batch-audit-api")
//    @LoginAuthentication
//    public Result<?> auditApiBatch(@RequestBody List<AbilityApiEntity> apiEntityList){
//        return Result.success();
//    }

    @AopLogger(describe = "提交接口注册", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "提交接口注册")
    @PostMapping("/audit-submit")
    @LoginAuthentication
    public Result<?> auditSubmit(@RequestBody AbilityApiEntity api){
        boolean flag =  abilityApiBizService.auditSubmit(api.getApiId(), api.getNote());
        return Result.success("接口注册提交完成!", flag);
    }

    @AopLogger(describe = "撤回接口注册", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "撤回接口注册")
    @PostMapping("/audit-withdraw")
    @LoginAuthentication
    public Result<?> auditWithdraw(@RequestBody AbilityApiEntity api){
        boolean flag = abilityApiBizService.auditWithdraw(api.getApiId(), api.getNote());
        return Result.success("撤回接口注册完成!", flag);
    }

    @AopLogger(describe = "审核接口注册通过", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "审核接口注册通过")
    @PostMapping("/audit-pass")
    @LoginAuthentication
    public Result<?> auditPass(@RequestBody AbilityApiEntity api){
        boolean flag = abilityApiBizService.auditPass(api.getApiId(), api.getNote()) &&
                gatewayAdminBizService.addGatewayApi(api);
        return Result.success("接口注册审核通过!", flag);
    }

    @AopLogger(describe = "审核接口注册不通过", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "审核接口注册不通过")
    @PostMapping("/audit-not-pass")
    @LoginAuthentication
    public Result<?> auditNotPass(@RequestBody AbilityApiEntity api){
        boolean flag = abilityApiBizService.auditNotPass(api.getApiId(), api.getNote());
        return Result.success("接口注册审核不通过!", flag);
    }

    @AopLogger(describe = "审核发布接口", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "审核发布接口")
    @LoginAuthentication
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/audit-publish")
    public Result<?> auditPublish(@RequestBody AbilityApiEntity api){
        boolean flag = abilityApiBizService.auditPublish(api.getApiId(), api.getNote()) &&
                gatewayAdminBizService.addGatewayApi(api);
        return Result.success("发布接口成功!", flag);
    }

    @AopLogger(describe = "审核下线接口", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "审核下线接口")
    @LoginAuthentication
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/audit-offline")
    public Result<?> auditOffline(@RequestBody AbilityApiEntity api){
        boolean flag = abilityApiBizService.auditOffline(api.getApiId(), api.getNote()) &&
                gatewayAdminBizService.cancelGatewayApi(api);
        return Result.success("下线接口成功!", flag);
    }

}
