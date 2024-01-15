package com.dsj.csp.manage.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.dto.AbilityLoginVO;
import com.dsj.csp.manage.dto.PageQueryForm;
import com.dsj.csp.manage.entity.*;

import com.dsj.csp.manage.mapper.AbilityApiMapper;
import com.dsj.csp.manage.mapper.AbilityApplyMapper;
import com.dsj.csp.manage.service.AbilityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@RestController
@RequestMapping("/ability")
@Tag(name = "能力管理", description = "用于管理能力的API")
public class AbilityController {

    @Autowired
    private AbilityService abilityService;


    @Autowired
    private AbilityApplyMapper abilityApplyMapper;

    @Autowired
    private AbilityApiMapper abilityApiMapper;

    @Operation(summary = "能力注册", description = "注册一个新的能力")
    @PostMapping("/add-login")
    public Result<?> addAbility(@RequestBody AbilityLoginVO ability) {
        abilityService.saveAbility(ability);
        return Result.success("能力注册申请成功！等待审核...");
    }

    @Operation(summary = "获取能力详情", description = "获取特定能力的详细信息")
    @GetMapping("/info-ability")
    public Result<?> getAbilityInfoById(
            @Parameter(description = "能力ID") @RequestParam Long abilityId) {
        return Result.success(abilityService.getById(abilityId));
    }

    @Operation(summary = "查询全部注册能力列表", description = "获取能力列表")
    @GetMapping("/query-all-list")
    public Result<?> queryAbilityList() {
        return Result.success(abilityService.getAllAbilityList());

    }

    @Operation(summary = "分页查询注册能力列表", description = "分页查询注册能力列表")
    @PostMapping ("/page-login")
    public Result<?> pageAnother(
            @Valid @RequestBody PageQueryForm<AbilityEntity> pageQueryForm) {
        return Result.success(abilityService.page(pageQueryForm.toPage(), pageQueryForm.toQueryWrappers()));
    }

    @Operation(summary = "审核能力注册", description = "审核能力注册申请")
    @PostMapping("/audit")
    public Result<?> auditAbility(
            @Parameter(description = "能力ID") @RequestParam Long abilityId,
            @Parameter(description = "审核标志") @RequestParam Integer flag) {
        UpdateWrapper<AbilityEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("ability_id", abilityId);
        updateWrapper.set("STATUS", flag);
        updateWrapper.set("UPDATE_TIME", DateTime.now());
        abilityService.update(updateWrapper);
        return Result.success("审核完成！");
    }


    @Operation(summary = "编辑注册的能力")
    @PostMapping("edit-login")
    public Result<?> updateAbilityLogin(@RequestBody AbilityLoginVO abilityLogin){
        abilityService.updateAbilityLogin(abilityLogin);
        return Result.success("编辑注册能力成功!");
    }


    @Operation(summary = "查询接口信息", description = "查询特定接口的信息")
    @GetMapping("/query-api-info")
    public Result<?> queryApiInfo(@Parameter(description = "接口ID") @RequestParam Long apiId) {
        return Result.success(abilityApiMapper.selectById(apiId));
    }

    @Operation(summary = "新增能力使用申请", description = "申请使用能力")
    @PostMapping("/add-apply")
    public Result<?> applyAbility(@RequestBody List<AbilityApplyVO> applyVOs) {
        for (AbilityApplyVO applyVO : applyVOs) {
            abilityService.saveAbilityApply(applyVO);
        }
        return Result.success("能力申请完毕！请等待审核...");
    }

    @Operation(summary = "查看能力申请详情", description = "获取特定申请的能力详细信息")
    @GetMapping("/info-apply")
    public Result<?> getApplyInfoById(@Parameter(
            description = "能力申请ID") @RequestParam Long abilityApplyId) {
        return Result.success(abilityApplyMapper.selectById(abilityApplyId));
    }

    @Operation(summary = "审核能力使用申请", description = "审核能力使用申请")
    @PostMapping("/audit-apply")
    public Result<?> auditAbilityApply(
            @Parameter(description = "能力申请ID") @RequestParam Long abilityApplyId,
            @Parameter(description = "申请试用的应用ID") @RequestParam Long appId,
            @Parameter(description = "审核标志")  @RequestParam Integer flag){

        abilityService.auditApply(abilityApplyId, appId, flag);
        return Result.success("审核完成!");
    }

    @Operation(summary = "分页查询申请能力列表", description = "分页查询申请能力列表")
    @PostMapping("/page-apply")
    public Result<?> queryApplyPage(
            @Valid @RequestBody PageQueryForm<AbilityApplyEntity> applyQuery ) {
        return Result.success(abilityApplyMapper.selectPage(applyQuery.toPage(), applyQuery.toQueryWrappers()));
    }

    @Operation(summary = "编辑能力使用申请", description = "编辑能力使用申请")
    @PostMapping("/edit-apply")
    public Result<?> editAbilityApply(@RequestBody AbilityApplyEntity abilityApply){
        UpdateWrapper<AbilityApplyEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("ability_apply_id", abilityApply.getAbilityApplyId());
        return Result.success(abilityApplyMapper.update(abilityApply, updateWrapper));
    }
}
