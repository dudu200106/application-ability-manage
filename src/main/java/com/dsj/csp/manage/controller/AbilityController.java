package com.dsj.csp.manage.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.common.dto.BusinessException;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.aop.annotation.LoginAuthentication;
import com.dsj.csp.common.consts.GatewayCryptKeyConst;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.biz.*;
import com.dsj.csp.manage.dto.AbilityApiVO;
import com.dsj.csp.manage.dto.gateway.ApiHandleVO;
import com.dsj.csp.manage.dto.gateway.CryptJsonBody;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityService;
import com.dsj.csp.manage.service.ApiFeignService;
import com.dsj.csp.manage.util.IdentifyUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
@Slf4j
public class AbilityController {
    private final AbilityService abilityService;
    private final AbilityBizService abilityBizService;
    private final AbilityApiService abilityApiService;
    private final AbilityApiBizService abilityApiBizService;
    private final AbilityApiApplyService abilityApiApplyService;
    private final AbilityApiApplyBizService abilityApiApplyBizService;

    @AopLogger(describe = "新增能力", operateType = LogEnum.INSERT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "新增能力", description = "新增一个新的能力")
    @PostMapping("/add-login")
    @LoginAuthentication
    public Result<?> addAbility(@RequestBody AbilityEntity ability) {
        long cnt = abilityService.count(Wrappers.lambdaQuery(AbilityEntity.class)
                .eq(AbilityEntity::getAbilityName, ability.getAbilityName()));
        if (cnt>0){
            throw new BusinessException("新增能力名称已存在! ");
        }
        Boolean saveAbility = abilityService.save(ability);
        return Result.success("能力新增成功!", saveAbility);
    }

//    @AopLogger(describe = "获取能力详情", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "获取能力详情", description = "获取特定能力的详细信息")
    @GetMapping("/info-ability")
    public Result<?> getAbilityInfoById(
            @Parameter(description = "能力ID") @RequestParam Long abilityId) {
        return Result.success(abilityService.getById(abilityId));
    }

//    @AopLogger(describe = "分页查询能力目录列表", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "分页查询能力目录列表", description = "分页查询能力目录列表")
    @GetMapping ("/page-ability-catalog")
    public Result<?> queryAbilityCatalog(@Parameter(description = "用户ID") Long userId,
                                         @Parameter(description = "分页条数", required = true) int size,
                                         @Parameter(description = "当前页数", required = true) int current,
                                         @Parameter(description = "搜索关键字") String keyword,
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", fallbackPatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"})
                                         @Parameter(description = "开始时间") Date startTime,
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", fallbackPatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"})
                                         @Parameter(description = "结束时间") Date endTime) {
        return Result.success(abilityService.pageAbilitys(userId, keyword, startTime, endTime, current, size));
    }

    @AopLogger(describe = "编辑能力", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "编辑能力")
    @PostMapping("edit-login")
    @LoginAuthentication
    public Result<?> updateAbility(@RequestBody AbilityEntity ability){
        ability.setUpdateTime(new Date());
        Boolean editFlag = abilityService.updateById(ability);
        return Result.success("编辑注册能力成功!", editFlag);
    }


    @AopLogger(describe = "批量删除能力", operateType = LogEnum.DELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "批量删除能力")
    @PostMapping("/delete-Batch")
    @LoginAuthentication
    public Result<?> removeAbility(@Parameter(description = "能力id列表") @RequestBody List<AbilityEntity> abilityList){
        boolean delFlag = abilityBizService.removeAbilityBatch(abilityList);
        return Result.success("批量删除能力及其接口完成! ", delFlag);
    }

    @AopLogger(describe = "删除能力", operateType = LogEnum.DELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "删除能力")
    @PostMapping("/delete")
    @LoginAuthentication
    public Result<?> removeAbility(@RequestBody AbilityEntity ability){
        boolean delFlag = abilityBizService.removeAbility(ability);
        return Result.success("删除能力及其接口完成! ", delFlag);
    }

    //    @AopLogger(describe = "获取能力简单信息目录", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "获取能力简单信息目录")
    @GetMapping("/get-ability-catalog")
    public Result<?> getAbilityCatalog(){
        List<AbilityEntity> abilityIds = abilityService.list(Wrappers.lambdaQuery(AbilityEntity.class)
                .select(AbilityEntity::getAbilityId ,AbilityEntity::getAbilityName));
        return Result.success(abilityIds);
    }


    private final ApiFeignService apiProxyBizService;

    @AopLogger(describe = "新增接口", operateType = LogEnum.INSERT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "新增接口")
    @PostMapping("add-api")
    @LoginAuthentication
    @Transactional
    public Result<?> addApi(@RequestBody AbilityApiVO apiVO){
        UserApproveRequest userApproveRequest = IdentifyUser.getUserInfo();
        boolean flag = abilityApiBizService.saveApi(apiVO, userApproveRequest);
        return Result.success(flag+"", "添加接口成功!");
    }

//    @AopLogger(describe = "查询接口信息", operateType = LogEnum.INSERT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查询接口信息", description = "查询特定接口的信息")
    @GetMapping("/query-api-info")
    public Result<?> queryApiInfo(@Parameter(description = "接口ID") @RequestParam Long apiId) {
        return Result.success(abilityApiBizService.getApiInfo(apiId));
    }

    @AopLogger(describe = "更新接口", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "更新接口")
    @PostMapping("edit-api")
    @LoginAuthentication
    public Result<?> editApi(@RequestBody AbilityApiVO apiVO){
        Boolean editApiFlag = abilityApiBizService.updateApi(apiVO);
        return Result.success("已修改接口完毕! ", editApiFlag);
    }

    //    @AopLogger(describe = "分页查询api目录列表", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "分页查询api目录列表")
    @GetMapping("page-api-catalog")
    @Cacheable(keyGenerator = "selfKeyGenerate", cacheNames = "Api", cacheManager = "caffeineCacheManager")
    public Result<?> pageApiList(@Parameter(description = "是否过滤未发布的接口") boolean onlyPublished,
                                 @Parameter(description = "请求方式") String reqMethod,
                                 @Parameter(description = "状态") Integer status,
                                 @Parameter(description = "用户ID") Long userId,
                                 @Parameter(description = "能力ID") Long abilityId,
                                 @Parameter(description = "分页条数", required = true) int size,
                                 @Parameter(description = "当前页数", required = true) int current,
                                 @Parameter(description = "搜索关键字") String keyword,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", fallbackPatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"})
                                 @Parameter(description = "开始时间") Date startTime,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", fallbackPatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"})
                                 @Parameter(description = "结束时间") Date endTime) {
        return Result.success(abilityApiBizService.pageApiCatalog(onlyPublished, reqMethod, status, userId, abilityId, keyword, current, size, startTime, endTime));
    }

    //    @AopLogger(describe = "分页查询申请通过的接口列表", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "分页查询申请通过的接口列表", description = "分页查询申请通过的接口列表")
    @GetMapping("/page-passed-apis")
    public Result<?> pagePassedApi(@Parameter(description = "用户ID") Long userId,
                                   @Parameter(description = "应用ID") Long appId,
                                   @Parameter(description = "能力ID") Long abilityId,
                                   @Parameter(description = "分页条数", required = true) int size,
                                   @Parameter(description = "当前页数", required = true) int current,
                                   @Parameter(description = "搜索关键字(匹配接口名称/描述/路径)") String keyword,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", fallbackPatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"})
                                   @Parameter(description = "开始时间") Date startTime,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", fallbackPatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"})
                                   @Parameter(description = "结束时间") Date endTime) {
        return Result.success(abilityApiBizService.pagePassedApis(userId, appId, abilityId, keyword, current, size, startTime, endTime));
    }



//    @AopLogger(describe = "统计能力数", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "统计能力数")
    @GetMapping("/count-avail-ability")
    public Result<?> countAvailableAbility(){
        return Result.success(abilityService.count());
    }

//    @AopLogger(describe = "统计接口数量", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "统计接口数量")
    @GetMapping("/count-api")
    public Result<?> countApi(){
        return Result.success(abilityApiService.count());
    }

//    @AopLogger(describe = "统计用户申请能力数量", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "统计用户申请能力数量")
    @GetMapping("/count-user-ability")
    public Result<?> countuserAbility(String userId){
        return Result.success(abilityApiApplyService.countUserAbility(userId));
    }

//    @AopLogger(describe = "统计用户申请接口数量", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "统计用户申请接口数量")
    @GetMapping("/count-user-api")
    public Result<?> countUserApi(String userId){
        return Result.success(abilityApiApplyService.countUserApi(userId));
    }




//    @AopLogger(describe = "查询能力的接口列表", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查询能力的接口列表", description = "查询能力的接口列表")
    @GetMapping("/query-api-list")
    public Result<?> queryApiList(@Parameter(description = "能力ID") @RequestParam Long abilityId) {
        return Result.success(abilityApiBizService.getAbilityApiList(abilityId));
    }

//    @AopLogger(describe = "查询用户申请到的api列表", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查询用户申请到的api列表")
    @GetMapping("/query-user-apis")
    public Result<?> queryUserApis(@Parameter(description = "用户ID") @RequestParam Long userId) {
        return Result.success(abilityApiBizService.getUserApiList(userId));
    }

//    @AopLogger(describe = "查询应用申请到的api列表", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查询应用申请到的api列表")
    @GetMapping("/query-app-apis")
    public Result<?> queryAppApis(@Parameter(description = "应用ID") @RequestParam Long appId) {
        return Result.success(abilityApiBizService.getAppApiList(appId));
    }



    @AopLogger(describe = "新增接口使用申请", operateType = LogEnum.INSERT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "新增接口使用申请", description = "申请使用接口")
    @PostMapping("/add-api-apply")
    @LoginAuthentication
    public Result<?> applyApi(@RequestBody AbilityApiApplyEntity apply) {
        UserApproveRequest userApproveRequest = IdentifyUser.getUserInfo();
        abilityApiApplyBizService.saveApiApply(apply, userApproveRequest);
        return Result.success("能力申请完毕！请等待审核...");
    }

//    @AopLogger(describe = "查看接口申请详情", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "查看接口申请详情", description = "获取接口申请详情")
    @GetMapping("/info-api-apply")
    public Result<?> getApiApplyInfo(@Parameter(description = "能力申请ID") @RequestParam Long apiApplyId) {
        return Result.success(abilityApiApplyBizService.getApplyInfo(apiApplyId));
    }

//    @AopLogger(describe = "分页查询接口申请列表", operateType = LogEnum.SELECT, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "分页查询接口申请列表", description = "分页查询接口申请列表")
    @GetMapping("/page-api-apply")
    public Result<?> pageApiApply(@Parameter(description = "是否屏蔽'未提交'状态申请") boolean onlySubmitted,
                                  @Parameter(description = "用户ID") Long userId,
                                  @Parameter(description = "应用ID") Long appId,
                                  @Parameter(description = "能力ID") Long abilityId,
                                  @Parameter(description = "分页条数", required = true) int size,
                                  @Parameter(description = "当前页数", required = true) int current,
                                  @Parameter(description = "搜索关键字") String keyword,
                                  @Parameter(description = "状态") Integer status,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", fallbackPatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"})
                                  @Parameter(description = "开始时间") Date startTime,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", fallbackPatterns = {"yyyy-MM-dd", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss"})
                                  @Parameter(description = "结束时间") Date endTime) {
        return Result.success(abilityApiApplyBizService.pageApiApply(onlySubmitted, appId, userId, abilityId, keyword, status, startTime, endTime, current, size));
    }



    @AopLogger(describe = "编辑接口使用申请", operateType = LogEnum.UPDATE, logType = LogEnum.OPERATETYPE)
    @Operation(summary = "编辑接口使用申请", description = "编辑接口使用申请")
    @PostMapping("/edit-api-apply")
    @LoginAuthentication
    public Result<?> editApiApply(@RequestBody AbilityApiApplyEntity apiApplyEntity){
        apiApplyEntity.setUpdateTime(new Date());
        boolean editFlag = abilityApiApplyService.updateById(apiApplyEntity);
        return Result.success(editFlag+"", "编辑接口使用申请完毕!");
    }


}
