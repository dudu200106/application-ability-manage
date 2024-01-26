package com.dsj.csp.manage.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.biz.AbilityApplyBizService;
import com.dsj.csp.manage.biz.AbilityBizService;
import com.dsj.csp.manage.dto.AbilityApplyVO;
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
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
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
    public Result<?> addAbility(@RequestBody AbilityEntity ability) {
        Boolean saveAbility = abilityService.save(ability);
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
            @Valid @RequestBody AbilityQueryVO abilityQuery) {
        return Result.success(abilityService.page(abilityQuery.toPage(), abilityQuery.getQueryWrapper()));
    }

    @Operation(summary = "分页查询可调用能力列表", description = "分页查询可调用能力列表")
    @PostMapping ("/page-available-ability")
    public Result<?> queryAvailablePage(
            @Valid @RequestBody AbilityQueryVO abilityQuery) {
        LambdaQueryWrapper abilityQW = abilityQuery.getQueryWrapper().lambda().in(AbilityEntity::getStatus, 3,4);
        return Result.success(abilityService.page(abilityQuery.toPage(), abilityQW));
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
    public Result<?> updateAbilityLogin(@RequestBody AbilityEntity ability){
        Boolean editFlag = abilityService.updateById(ability);
        return Result.success("编辑注册能力成功!", editFlag);
    }

    @Operation(summary = "新增接口")
    @PostMapping("add-api")
    public Result<?> addApiA(@RequestBody AbilityApiVO apiVO){
        abilityApiBizService.saveApi(apiVO);
        return Result.success("添加接口成功!");
    }

    @Operation(summary = "查询接口信息", description = "查询特定接口的信息")
    @GetMapping("/query-api-info")
    public Result<?> queryApiInfo(@Parameter(description = "接口ID") @RequestParam Long apiId) {
        return Result.success(abilityApiBizService.getApiInfo(apiId));
    }

    @Operation(summary = "查询申请的接口列表", description = "查询申请的接口列表")
    @GetMapping("/query-apply-api")
    public Result<?> queryApplyApiList(@Parameter(description = "能力申请ID") @RequestParam Long abilityApplyId) {
        return Result.success(abilityApiBizService.getApplyApiList(abilityApplyId));
    }

    @Operation(summary = "查询能力的接口列表", description = "查询能力的接口列表")
    @GetMapping("/query-api-list")
    public Result<?> queryApiList(@Parameter(description = "能力ID") @RequestParam Long abilityId) {
        return Result.success(abilityApiBizService.getAbilityApiList(abilityId));
    }

    @Operation(summary = "分页查询接口列表", description = "查询接口分页列表")
    @PostMapping("/page-api")
    public Result<?> pageApi(
            @Valid @RequestBody AbilityApiQueryVO apiQueryVO ){
        return Result.success(abilityApiBizService.pageApi(apiQueryVO));
//        return Result.success(abilityApiService.page(apiQueryVO.toPage(), apiQueryVO.getQueryWrapper()));
    }


    @Operation(summary = "更新接口")
    @PostMapping("edit-api")
    public Result<?> editApi(@RequestBody AbilityApiVO apiVO){
        Boolean editApiflag = abilityApiBizService.updateApi(apiVO);
        return Result.success("已修改接口完毕! ", editApiflag);
    }


    @Operation(summary = "新增能力使用申请", description = "申请使用能力")
    @PostMapping("/add-apply")
    public Result<?> applyAbility(@RequestBody AbilityApplyVO applyVO) {
        abilityApplyBizService.saveAbilityApply(applyVO);
        return Result.success("能力申请完毕！请等待审核...");
    }

    @Operation(summary = "查看能力申请详情", description = "获取特定申请的能力详细信息")
    @GetMapping("/info-apply")
    public Result<?> getApplyInfoById(@Parameter(
            description = "能力申请ID") @RequestParam Long abilityApplyId) {
//        return Result.success(abilityApplyService.getById(abilityApplyId));
        return Result.success(abilityApplyBizService.getApplyInfo(abilityApplyId));
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

    @Operation(summary = "统计能力数")
    @GetMapping("/count-avail-ability")
    public Result<?> countAvailableAbility(){
        return Result.success(abilityService.count());
    }

    @Operation(summary = "统计接口数量")
    @GetMapping("/count-api")
    public Result<?> countApi(){
        return Result.success(abilityApiService.count());
    }

    @Operation(summary = "统计用户能力数量")
    @GetMapping("/count-user-ability")
    public Result<?> countuserAbility(String userId){
        return Result.success(abilityBizService.countUserApplyAbility(userId));
    }

    @Operation(summary = "统计用户接口数量")
    @GetMapping("/count-user-api")
    public Result<?> countUserApi(String userId){
        return Result.success(abilityApiBizService.countUserApplyApi(userId));
    }


//    @Operation(summary = "统计能力数量")
//    @GetMapping("/count-ability")
//    public Result<?> countAbility(@Parameter(description = "能力状态") @RequestParam Integer status){
//        QueryWrapper<AbilityEntity> abilityQW = new QueryWrapper<>();
//        // 4:已发布能力
//        abilityQW.lambda().eq(AbilityEntity::getStatus, status);
//        return Result.success(abilityService.count(abilityQW));
//
//    }



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

    @Operation(summary = "删除能力")
    @PostMapping("/delete-ability-api")
    public Result<?> removeAbility(@Parameter(description = "能力id列表") @RequestBody AbilityDeleteDTO deleteDTO){
        String abilityIds = deleteDTO.getAbilityIds();
        boolean delFlag = abilityBizService.removeAbilityByIds(abilityIds);
        return Result.success("删除能力及其接口完成! ", delFlag);
    }

    @Operation(summary = "删除能力申请")
    @PostMapping("/delete-apply")
    public Result<?> removeApply(@RequestBody AbilityDeleteDTO deleteDTO){
        String applyIds = deleteDTO.getAbilityApplyIds();
        List<Long> ids = Arrays.asList(applyIds.split(",")).stream().map(id -> Long.parseLong(id)).toList();
        Boolean delFlag = abilityApplyService.removeBatchByIds(ids);
        return Result.success("删除能力申请完成! ", delFlag);
    }

    @Operation(summary = "删除能力接口")
    @PostMapping("/delete-api")
    public Result<?> removeApi(@RequestBody AbilityDeleteDTO deleteDTO){
        String apiIds = deleteDTO.getApiIds();
        List<Long> ids = Arrays.asList(apiIds.split(",")).stream().map(id -> Long.parseLong(id)).toList();
        Boolean delFlag = abilityApiService.removeBatchByIds(ids);
        return Result.success("删除能力完成! ", delFlag);
    }

}
