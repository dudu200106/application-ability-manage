package com.dsj.csp.manage.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.biz.AbilityApplyBizService;
import com.dsj.csp.manage.biz.AbilityBizService;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.dto.AbilityLoginVO;
import com.dsj.csp.manage.dto.*;
import com.dsj.csp.manage.entity.*;

import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityApplyService;
import com.dsj.csp.manage.service.AbilityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ability")
@Tag(name = "能力管理", description = "用于管理能力的API")
public class AbilityController {

    private final AbilityService abilityService;
    private final AbilityApiService abilityApiService;
    private final AbilityApplyService abilityApplyService;
    private final AbilityBizService abilityBizService;
    private final AbilityApiBizService abilityApiBizService;
    private final AbilityApplyBizService abilityApplyBizService;

    @Operation(summary = "能力注册", description = "注册一个新的能力")
    @PostMapping("/add-login")
    public Result<?> addAbility(@RequestBody AbilityLoginVO ability) {
        Boolean saveAbility = abilityBizService.saveAbility(ability);
        return Result.success("能力注册申请成功！等待审核...", saveAbility);
    }

    @Operation(summary = "获取能力详情", description = "获取特定能力的详细信息")
    @GetMapping("/info-ability")
    public Result<?> getAbilityInfoById(
            @Parameter(description = "能力ID") @RequestParam Long abilityId) {
        return Result.success(abilityService.getById(abilityId));
    }


    @Operation(summary = "分页查询注册能力列表", description = "分页查询注册能力列表")
    @PostMapping ("/page-login")
    public Result<?> queryLoginPage(
            @Valid @RequestBody AbilityQueryDTO abilityQuery) {
        return Result.success(abilityService.page(abilityQuery.toPage(), abilityQuery.getQueryWrapper()));
    }

    @Operation(summary = "审核能力注册", description = "审核能力注册申请")
    @PostMapping("/audit-login")
    public Result<?> auditAbility(@RequestBody AbilityAuditVO auditVO) {
        LambdaUpdateWrapper<AbilityEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityEntity::getAbilityId, auditVO.getAbilityId());
        updateWrapper.set(AbilityEntity::getStatus, auditVO.getFlag());
        updateWrapper.set(AbilityEntity::getNote, auditVO.getNote());
        updateWrapper.set(AbilityEntity::getUpdateTime, DateTime.now());
        abilityService.update(updateWrapper);
        return Result.success("审核完成！");
    }


    @Operation(summary = "编辑注册的能力")
    @PostMapping("edit-login")
    public Result<?> updateAbilityLogin(@RequestBody AbilityLoginVO abilityLogin){
        abilityBizService.updateAbilityLogin(abilityLogin);
        return Result.success("编辑注册能力成功!");
    }


    @Operation(summary = "查询接口信息", description = "查询特定接口的信息")
    @GetMapping("/query-api-info")
    public Result<?> queryApiInfo(@Parameter(description = "接口ID") @RequestParam Long apiId) {
        return Result.success(abilityApiService.getById(apiId));
    }

    @Operation(summary = "新增能力使用申请", description = "申请使用能力")
    @PostMapping("/add-apply")
    public Result<?> applyAbility(@RequestBody List<AbilityApplyVO> applyVOs) {
        for (AbilityApplyVO applyVO : applyVOs) {
            abilityApplyBizService.saveAbilityApply(applyVO);
        }
        return Result.success("能力申请完毕！请等待审核...");
    }

    @Operation(summary = "查看能力申请详情", description = "获取特定申请的能力详细信息")
    @GetMapping("/info-apply")
    public Result<?> getApplyInfoById(@Parameter(
            description = "能力申请ID") @RequestParam Long abilityApplyId) {
        return Result.success(abilityApplyService.getById(abilityApplyId));
    }

    @Operation(summary = "审核能力使用申请", description = "审核能力使用申请")
    @PostMapping("/audit-apply")
    public Result<?> auditAbilityApply(@RequestBody AbilityApplyAuditVO auditVO){

        abilityApplyBizService.auditApply(auditVO);
        return Result.success("审核完成!");
    }

    @Operation(summary = "分页查询申请能力列表", description = "分页查询申请能力列表")
    @PostMapping("/page-apply")
    public Result<?> queryApplyPage(
            @Valid @RequestBody AbilityApplyQueryVO abilityApplyQueryVO) {
        return Result.success(abilityApplyService.page(abilityApplyQueryVO.toPage(), abilityApplyQueryVO.getQueryWrapper()));
    }

    @Operation(summary = "编辑能力使用申请", description = "编辑能力使用申请")
    @PostMapping("/edit-apply")
    public Result<?> editAbilityApply(@RequestBody AbilityApplyEntity abilityApply){
        UpdateWrapper<AbilityApplyEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(AbilityApplyEntity::getAbilityApplyId, abilityApply.getAbilityApplyId());
        return Result.success(abilityApplyService.update(abilityApply, updateWrapper));
    }

    @Operation(summary = "统计可用能力数")
    @GetMapping("/count-avail-ability")
    public Result<?> countAvailableAbility(){
        return Result.success(abilityService.countAvailAbility());

    }

    @Operation(summary = "统计能力数量")
    @GetMapping("/count-ability")
    public Result<?> countAbility(@Parameter(description = "能力状态") @RequestParam Integer status){
        QueryWrapper<AbilityEntity> abilityQW = new QueryWrapper<>();
        // 4:已发布能力
        abilityQW.lambda().eq(AbilityEntity::getStatus, status);
        return Result.success(abilityService.count(abilityQW));

    }

    @Operation(summary = "统计接口数量")
    @GetMapping("/count-api")
    public Result<?> countApi(){
        return Result.success(abilityService.count());

    }

    /**
     * 根据 app-code 获取 可访问路径
     *
     * @return 可访问路径
     */
    @GetMapping("/get-auth-api")
    public Result<List<String>> getAuthApi(String appCode) {
        // 返回可访问路径
        // 最终返回的路径看你并不支持通配符，而是每一个路径都需要在管理端授权

        System.out.println("收到请求，appcode：" + appCode);

        return Result.success(abilityApiBizService.getApiList( appCode));
    }

}
