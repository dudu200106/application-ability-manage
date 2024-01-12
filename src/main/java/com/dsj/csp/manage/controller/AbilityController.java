package com.dsj.csp.manage.controller;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.dto.AbilityLoginVO;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.AbilityEntity;

import com.dsj.csp.manage.entity.ManageApplication;
import com.dsj.csp.manage.mapper.AbilityApiMapper;
import com.dsj.csp.manage.mapper.AbilityApplyMapper;
import com.dsj.csp.manage.service.AbilityService;

import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.util.Sm4;
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
public class AbilityController {

    @Autowired
    AbilityService abilityService;
    @Autowired
    ManageApplicationService manageApplicationService;
    @Autowired
    AbilityApplyMapper abilityApplyMapper;
    @Autowired
    AbilityApiMapper abilityApiMapper;


    // 能力注册/新增
    @PostMapping("/add")
    public Result<?> loginNewAbility(@RequestBody AbilityLoginVO ability){

        abilityService.saveAbility(ability);
        return Result.success("能力注册申请成功! 等待审核……");
    }

    // 查看能力详情
    @GetMapping("/ability-info")
    public Result<?> getAbilityInfoById(@RequestParam Long abilityId){

        return Result.success(abilityService.getById(abilityId));
    }

    // 查询能力列表
    @GetMapping("/queryList")
    public Result<?> queryAbilityList(){

        return Result.success(abilityService.getAllAbilityList());

    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    public Result<?> page(Page<AbilityEntity> page, AbilityEntity app) {
        return Result.success(abilityService.page(page, Wrappers.query(app)));
    }


    // 能力注册审核
    @PostMapping("/audit")
    public Result<?> auditLoginAbility(@RequestParam Long abilityId, @RequestParam Integer flag){
        // 创建更新条件构造器
        UpdateWrapper<AbilityEntity> updateWrapper = new UpdateWrapper<>();
        // 设置更新条件，这里假设要更新 id 为 1 的记录
        updateWrapper.eq("ability_id", abilityId);
        // 设置要更新的字段和值
        updateWrapper.set("STATUS", flag);
        updateWrapper.set("UPDATE_TIME", DateTime.now());
        abilityService.update(updateWrapper);

        return Result.success("审核完成!");
    }


    // 查询接口信息
    @GetMapping("/query-apiInfo")
    public Result<?> queryApiInfo(@RequestParam Long apiId){
        return Result.success(abilityApiMapper.selectById(apiId));

    }


    // 能力使用申请
    @PostMapping("/apply-use")
    public Result<?> applyAbility(@RequestBody List<AbilityApplyVO> applyVOs){
        // 建立申请
        for (AbilityApplyVO applyVO : applyVOs){
            abilityService.saveAbilityApply(applyVO);
        }
        return Result.success("能力申请完毕! 请等待耐心审核......");
    }

    // 查看申请使用的能力详情
    @GetMapping("/apply-info")
    public Result<?> getApplyInfoById(@RequestParam Long abilityApplyId){

        return Result.success(abilityApplyMapper.selectById(abilityApplyId));
    }

    // 能力申请审核
    @PostMapping("/apply-audit")
    public Result<?> auditAbilityApply(@RequestParam Long abilityApplyId, @RequestParam Long appId, @RequestParam Integer flag){
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

    // 页查询自己应用下的能力
    @PostMapping("/apply-page")
    public Result<?> queryApplyPage(Page<AbilityApplyEntity> page, AbilityApplyEntity app) {
        return Result.success(abilityApplyMapper.selectPage(page, Wrappers.query(app)));
    }



}
