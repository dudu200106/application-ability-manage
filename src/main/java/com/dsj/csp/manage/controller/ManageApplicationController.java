package com.dsj.csp.manage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.manage.dto.ManageApplictionVo;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.dsj.csp.manage.service.ManageApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/1/9 0009 16:00
 * @Todo:
 */
@Tag(name = "应用管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/magapplication")
public class ManageApplicationController {
    //    @Resource
    private final ManageApplicationService manageApplicationService;
    //    @Autowired


    /**
     * 分页查询
     */
    @Operation(summary = "查询所有数据")
    @PostMapping("/lists")
    public Result<List<ManageApplicationEntity>> list() {
        return Result.success(manageApplicationService.list());
    }

    /**
     * 分页查询
     */
    @AopLogger(describe = "分页查询应用",operateType =1,logType=1)
    @Operation(summary = "分页查询")
    @GetMapping("/selectPage")
    public Result<Page<ManageApplictionVo>> selectPage(@Parameter(description = "用户id") String appUserId, @Parameter(description = "查询关键字 Id或名称") String keyword, @Parameter(description = "开始时间") Date startTime, @Parameter(description = "结束时间") Date endTime, @Parameter int size, @Parameter int pages) {

        return Result.success(manageApplicationService.selectPage(appUserId, keyword, startTime, endTime, pages, size));

    }

    /**
     * 新增应用
     */
    @Operation(summary = "添加应用")
    @PostMapping("/addInfo")
    @AopLogger(describe = "添加应用",operateType =2,logType=1)
    public Result<?> add(@RequestBody ManageApplicationEntity manageApplication) {
        return Result.success(manageApplicationService.saveApp(manageApplication));
    }

    @Operation(summary = "删除应用")
    @PostMapping("/deleteApp")
    @AopLogger(describe = "删除应用",operateType =4,logType=1)
    public Result<?> deleteApp(@RequestBody ManageApplicationEntity manageApplication) {
        return Result.success(manageApplicationService.deleteApp(manageApplication));
    }

    //查询appid和name
    @Operation(summary = "查询应用")
    @GetMapping("/selectappID")
    @AopLogger(describe = "查询应用",operateType =1,logType=1)
    public Result selectappID(@Parameter(description = "appID") String appId, @Parameter(description = "用户Id") String appUserId) {
        return Result.success(manageApplicationService.selectappID(appId, appUserId));
    }


    //修改应用信息
    @Operation(summary = "修改应用")
    @PostMapping("/upadataAppInfo")
    @AopLogger(describe = "修改应用",operateType =3,logType=1)
    public Result<?> upadataAppInfo(@RequestBody ManageApplicationEntity manageApplication) {
        return Result.success(manageApplicationService.upadataAppInfo(manageApplication));
    }

    //统计个人应用总数
    @Operation(summary = "统计个人应用总数")
    @GetMapping("/countAppUser")
    @AopLogger(describe = "统计个人应用总数",operateType =1,logType=1)
    public Result<?> countAppUser(@Parameter String appUserId) {
        return Result.success(manageApplicationService.countAppUser(appUserId));
    }
    @Operation(summary = "修改应用的key")
    @PostMapping("/upadataAppKey")
    @AopLogger(describe = "修改应用的key",operateType =3,logType=1)
    public Result<?> upadataAppKey(@RequestBody ManageApplicationEntity manageApplication) {
        return Result.success(manageApplicationService.upadataAppKey(manageApplication));
    }


}