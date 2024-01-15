package com.dsj.csp.manage.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.dto.AbilityLoginVO;
import com.dsj.csp.manage.dto.PageQueryForm;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.AbilityEntity;

import com.dsj.csp.manage.entity.AppEntity;
import com.dsj.csp.manage.entity.ManageApplication;
import com.dsj.csp.manage.mapper.AbilityApiMapper;
import com.dsj.csp.manage.mapper.AbilityApplyMapper;
import com.dsj.csp.manage.service.AbilityService;

import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.util.Sm4;
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
    private ManageApplicationService manageApplicationService;

    @Autowired
    private AbilityApplyMapper abilityApplyMapper;

    @Autowired
    private AbilityApiMapper abilityApiMapper;

    @Operation(summary = "能力注册", description = "注册一个新的能力")
    @PostMapping("/add")
    public Result<?> addAbility(@RequestBody AbilityLoginVO ability) {
        abilityService.saveAbility(ability);
        return Result.success("能力注册申请成功！等待审核...");
    }

    @Operation(summary = "获取能力详情", description = "获取特定能力的详细信息")
    @GetMapping("/ability-info")
    public Result<?> getAbilityInfoById(
            @Parameter(description = "能力ID") @RequestParam Long abilityId) {
        return Result.success(abilityService.getById(abilityId));
    }

    @Operation(summary = "查询全部能力列表", description = "获取能力列表")
    @GetMapping("/queryList")
    public Result<?> queryAbilityList() {
        return Result.success(abilityService.getAllAbilityList());
    }

    @Operation(summary = "分页查询注册能力列表", description = "分页查询注册能力列表")
    @PostMapping ("/page-login")
    public Result<?> pageAnother(
            @Valid @RequestBody PageQueryForm<AbilityEntity> pageQueryForm) {
        return Result.success(abilityService.page(pageQueryForm.toPage(), pageQueryForm.toQueryWrappers()));
    }

    @Operation(summary = "能力注册审核", description = "审核能力注册申请")
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

    @Operation(summary = "查询接口信息", description = "查询特定接口的信息")
    @GetMapping("/query-apiInfo")
    public Result<?> queryApiInfo(@Parameter(description = "接口ID") @RequestParam Long apiId) {
        return Result.success(abilityApiMapper.selectById(apiId));
    }

    @Operation(summary = "能力使用申请", description = "申请使用能力")
    @PostMapping("/apply-use")
    public Result<?> applyAbility(@RequestBody List<AbilityApplyVO> applyVOs) {
        for (AbilityApplyVO applyVO : applyVOs) {
            abilityService.saveAbilityApply(applyVO);
        }
        return Result.success("能力申请完毕！请等待审核...");
    }

    @Operation(summary = "查看申请使用的能力详情", description = "获取特定申请的能力详细信息")
    @GetMapping("/apply-info")
    public Result<?> getApplyInfoById(@Parameter(
            description = "能力申请ID") @RequestParam Long abilityApplyId) {
        return Result.success(abilityApplyMapper.selectById(abilityApplyId));
    }

    @Operation(summary = "能力申请审核", description = "审核能力使用申请")
    @PostMapping("/apply-audit")
    public Result<?> auditAbilityApply(
            @Parameter(description = "能力申请ID") @RequestParam Long abilityApplyId,
            @Parameter(description = "申请试用的应用ID") @RequestParam Long appId,
            @Parameter(description = "审核标志")  @RequestParam Integer flag){
        // 创建更新条件构造器
        UpdateWrapper<AbilityApplyEntity> updateWrapper = new UpdateWrapper<>();
        // 设置更新条件，这里假设要更新 id 为 1 的记录
        updateWrapper.eq("ability_apply_id", abilityApplyId);
        // 设置要更新的字段和值
        updateWrapper.set("STATUS", flag);
        updateWrapper.set("UPDATE_TIME", DateTime.now());
        abilityApplyMapper.update(updateWrapper);

        // 判断是否生成APP Key 和 Secret Key
        String appSecretKey =  manageApplicationService.getById(appId).getAppSecret();
        String appAppKey =  manageApplicationService.getById(appId).getAppSecret();
        if (flag==1
                && (appSecretKey==null || "".equals(appSecretKey))
                && (appAppKey==null || "".equals(appAppKey))){
            String appKey = Sm4.sm();
            String secretKey = Sm4.sm();
            UpdateWrapper<ManageApplication> appUpdateWrapper = new UpdateWrapper<>();
            // 设置更新条件，这里假设要更新 id 为 1 的记录
            appUpdateWrapper.eq("APP_ID", appId);
            // 设置要更新的字段和值
            appUpdateWrapper.set("APP_SECRET", secretKey);
            appUpdateWrapper.set("APP_KEY", appKey);
            manageApplicationService.update(appUpdateWrapper);
        }
        return Result.success("审核完成!");
    }

    @Operation(summary = "分页查询申请能力列表", description = "分页查询申请能力列表")
    @PostMapping("/apply-page")
    public Result<?> queryApplyPage(@RequestBody PageQueryForm<AbilityApplyEntity> applyQuery ) {
        return Result.success(abilityApplyMapper.selectPage(applyQuery.toPage(), applyQuery.toQueryWrappers()));
    }

}
