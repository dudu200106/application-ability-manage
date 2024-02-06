package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.common.dto.BusinessException;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.biz.AbilityApiApplyBizService;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.biz.AbilityBizService;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.dto.AbilityDeleteDTO;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityService;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
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
    private final AbilityBizService abilityBizService;
    private final AbilityApiBizService abilityApiBizService;
    private final AbilityApiApplyService abilityApiApplyService;
    private final AbilityApiApplyBizService abilityApiApplyBizService;

    @Operation(summary = "新增能力", description = "新增一个新的能力")
    @PostMapping("/add-login")
    public Result<?> addAbility(@RequestBody AbilityEntity ability) {
        long cnt = abilityService.count(Wrappers.lambdaQuery(AbilityEntity.class)
                .eq(AbilityEntity::getAbilityName, ability.getAbilityName()));
        if (cnt>0){
            throw new BusinessException("新增能力名称已存在! ");
        }
        Boolean saveAbility = abilityService.save(ability);
        return Result.success("能力新增成功!", saveAbility);
    }

    @Operation(summary = "获取能力详情", description = "获取特定能力的详细信息")
    @GetMapping("/info-ability")
    public Result<?> getAbilityInfoById(
            @Parameter(description = "能力ID") @RequestParam Long abilityId) {
        return Result.success(abilityService.getById(abilityId));
    }

    @Operation(summary = "分页查询能力目录列表", description = "分页查询能力目录列表")
    @GetMapping ("/page-ability-catalog")
    public Result<?> queryAbilityCatalog(@Parameter(description = "用户ID") Long userId,
                                         @Parameter(description = "分页条数", required = true) int size,
                                         @Parameter(description = "当前页数", required = true) int current,
                                         @Parameter(description = "搜索关键字") String keyword,
                                         @JsonFormat(pattern = "yyyy/MM/dd",timezone="GMT+8")
                                             @Parameter(description = "开始时间") Date startTime,
                                         @JsonFormat(pattern = "yyyy/MM/dd",timezone="GMT+8")
                                             @Parameter(description = "结束时间") Date endTime) {
        return Result.success(abilityService.pageAbilitys(userId, keyword, startTime, endTime, current, size));
    }

    @Operation(summary = "编辑能力")
    @PostMapping("edit-login")
    public Result<?> updateAbility(@RequestBody AbilityEntity ability){
        ability.setUpdateTime(new Date());
        Boolean editFlag = abilityService.updateById(ability);
        return Result.success("编辑注册能力成功!", editFlag);
    }

    @Operation(summary = "新增接口")
    @PostMapping("add-api")
    public Result<?> addApi(@RequestBody AbilityApiVO apiVO, @RequestHeader("accessToken") String accessToken){
        abilityApiBizService.saveApi(apiVO, accessToken);
        return Result.success("添加接口成功!");
    }

    @Operation(summary = "审核接口注册", description = "审核接口注册")
    @PostMapping("/audit-api")
    public Result<?> auditApi(@RequestBody AbilityAuditVO auditVO){
        String  msg = abilityApiBizService.auditApi(auditVO);
        return Result.success(msg);
    }

    @Operation(summary = "查询接口信息", description = "查询特定接口的信息")
    @GetMapping("/query-api-info")
    public Result<?> queryApiInfo(@Parameter(description = "接口ID") @RequestParam Long apiId) {
        return Result.success(abilityApiBizService.getApiInfo(apiId));
    }

    @Operation(summary = "更新接口")
    @PostMapping("edit-api")
    public Result<?> editApi(@RequestBody AbilityApiVO apiVO){
        Boolean editApiflag = abilityApiBizService.updateApi(apiVO);
        return Result.success("已修改接口完毕! ", editApiflag);
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

    @Operation(summary = "删除能力")
    @PostMapping("/delete-ability-api")
    public Result<?> removeAbility(@Parameter(description = "能力id列表") @RequestBody AbilityDeleteDTO deleteDTO){
        String abilityIds = deleteDTO.getAbilityIds();
        boolean delFlag = abilityBizService.removeAbilityByIds(abilityIds);
        return Result.success("删除能力及其接口完成! ", delFlag);
    }


    @Operation(summary = "删除能力接口")
    @PostMapping("/delete-api")
    public Result<?> removeApi(@RequestBody AbilityDeleteDTO deleteDTO){
        String apiIds = deleteDTO.getApiIds();
        List<Long> ids = Arrays.asList(apiIds.split(",")).stream().map(id -> Long.parseLong(id)).toList();
        Boolean delFlag = abilityApiService.removeBatchByIds(ids);
        return Result.success("删除能力完成! ", delFlag);
    }


    @Operation(summary = "查询能力的接口列表", description = "查询能力的接口列表")
    @GetMapping("/query-api-list")
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

    @Operation(summary = "新增接口使用申请", description = "申请使用接口")
    @PostMapping("/add-api-apply")
    public Result<?> applyApi(@RequestBody AbilityApiApplyEntity apply, @RequestHeader("accessToken") String accessToken) {
        abilityApiApplyBizService.saveApiApply(apply, accessToken);
        return Result.success("能力申请完毕！请等待审核...");
    }

    @Operation(summary = "查看接口申请详情", description = "获取接口申请详情")
    @GetMapping("/info-api-apply")
    public Result<?> getApiApplyInfo(@Parameter(description = "能力申请ID") @RequestParam Long apiApplyId) {
        return Result.success(abilityApiApplyBizService.getApplyInfo(apiApplyId));
    }

    @Operation(summary = "审核能力使用申请", description = "审核能力使用申请")
    @PostMapping("/audit-api-apply")
    public Result<?> auditApiApply(@RequestBody AbilityAuditVO auditVO){
        String  msg = abilityApiApplyBizService.auditApply(auditVO);
        return Result.success(msg);
    }


    @Operation(summary = "分页查询api目录列表")
    @GetMapping("page-api-catalog")
    public Result<?> pageApiList(@Parameter(description = "是否过滤未发布的接口", required = true) Boolean onlyPublished,
                                 @Parameter(description = "请求方式") String reqMethod,
                                 @Parameter(description = "状态") Integer status,
                                 @Parameter(description = "用户ID") Long userId,
                                 @Parameter(description = "能力ID") Long abilityId,
                                 @Parameter(description = "分页条数", required = true) int size,
                                 @Parameter(description = "当前页数", required = true) int current,
                                 @Parameter(description = "搜索关键字") String keyword,
                                 @JsonFormat(pattern = "yyyy/MM/dd",timezone="GMT+8")
                                     @Parameter(description = "开始时间") Date startTime,
                                 @JsonFormat(pattern = "yyyy/MM/dd",timezone="GMT+8")
                                     @Parameter(description = "结束时间") Date endTime) {
        return Result.success(abilityApiBizService.pageApiCatalog(onlyPublished, reqMethod, status, userId, abilityId, keyword, current, size, startTime, endTime));
    }

    @Operation(summary = "分页查询接口申请列表", description = "分页查询接口申请列表")
    @GetMapping("/page-api-apply")
    public Result<?> pageApiApply(@Parameter(description = "是否屏蔽'未提交'状态申请", required = true) Boolean onlySubmitted,
                                  @Parameter(description = "用户ID") Long userId,
                                  @Parameter(description = "应用ID") Long appId,
                                  @Parameter(description = "能力ID") Long abilityId,
                                  @Parameter(description = "分页条数", required = true) int size,
                                  @Parameter(description = "当前页数", required = true) int current,
                                  @Parameter(description = "搜索关键字") String keyword,
                                  @Parameter(description = "状态") Integer status,
                                  @JsonFormat(pattern = "yyyy/MM/dd",timezone="GMT+8")
                                      @Parameter(description = "开始时间") Date startTime,
                                  @JsonFormat(pattern = "yyyy/MM/dd",timezone="GMT+8")
                                      @Parameter(description = "结束时间") Date endTime) {
        return Result.success(abilityApiApplyBizService.pageApiApply(onlySubmitted, appId, userId, abilityId, keyword, status, startTime, endTime, current, size));
    }

    @Operation(summary = "分页查询申请通过的接口列表", description = "分页查询申请通过的接口列表")
    @GetMapping("/page-passed-apis")
    public Result<?> pagePassedApi(@Parameter(description = "用户ID") Long userId,
                                  @Parameter(description = "应用ID") Long appId,
                                  @Parameter(description = "能力ID") Long abilityId,
                                  @Parameter(description = "分页条数", required = true) int size,
                                  @Parameter(description = "当前页数", required = true) int current,
                                  @Parameter(description = "搜索关键字(匹配接口名称/描述/路径)") String keyword,
                                  @Parameter(description = "开始时间") Date startTime,
                                  @Parameter(description = "结束时间") Date endTime) {
        return Result.success(abilityApiBizService.pagePassedApis(userId, appId, abilityId, keyword, current, size, startTime, endTime));
    }

    @Operation(summary = "编辑接口使用申请", description = "编辑接口使用申请")
    @PostMapping("/edit-api-apply")
    public Result<?> editApiApply(@RequestBody AbilityApiApplyEntity apiApplyEntity){
        apiApplyEntity.setUpdateTime(new Date());
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


    @Operation(summary = "获取能力简单信息目录")
    @GetMapping("/get-ability-catalog")
    public Result<?> getAbilityCatalog(){
        List<AbilityEntity> abilityIds = abilityService.list(Wrappers.lambdaQuery(AbilityEntity.class)
                        .select(AbilityEntity::getAbilityId ,AbilityEntity::getAbilityName));
        return Result.success(abilityIds);
    }


}
