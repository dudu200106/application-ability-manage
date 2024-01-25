package com.dsj.csp.manage.controller;

import cn.hutool.json.JSONObject;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.enums.StatusEnum;
import com.dsj.csp.manage.dto.ManageApplictionDto;
import com.dsj.csp.manage.dto.ManageApplictionVo;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.mapper.AbilityApiMapper;
import com.dsj.csp.manage.mapper.AbilityApplyMapper;
import com.dsj.csp.manage.mapper.ManageApplicationMapper;
import com.dsj.csp.manage.mapper.UserApproveMapper;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.util.TimeTolong;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.dsj.csp.manage.util.RandomNumberGenerator.generateNumber;

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
//    private ManageApplicationService manageApplicationService;
    private final ManageApplicationService manageApplicationService;


    @Resource
    private UserApproveMapper userApproveMapper;

    @Resource
    private AbilityApplyMapper abilityApplyMapper;

    @Resource
    private AbilityApiMapper abilityApiMapper;

    @Resource
    private ManageApplicationMapper manageApplicationMapper;


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
    @Operation(summary = "分页查询")
    @GetMapping("/selectPage")
    public Result<Page<ManageApplictionVo>> selectPage(@Parameter(description = "用户id") String appUserId, @Parameter(description = "查询关键字 Id或名称") String keyword, @Parameter(description = "开始时间") Date startTime, @Parameter(description = "结束时间") Date endTime, @Parameter int size, @Parameter int pages) {
        Page<ManageApplictionVo> userApproveEntityPage = manageApplicationMapper.selectJoinPage(new Page<>(pages, size), ManageApplictionVo.class,
                new MPJLambdaWrapper<ManageApplicationEntity>()
                        .eq(!StringUtils.isEmpty(appUserId), ManageApplicationEntity::getAppUserId, appUserId)
                        .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), ManageApplicationEntity::getAppCreatetime, startTime, endTime)
                        .like(!StringUtils.isEmpty(keyword), ManageApplicationEntity::getAppName, keyword)
                        .or().like(!StringUtils.isEmpty(keyword), ManageApplicationEntity::getAppName, keyword)
                        .selectAll(UserApproveEntity.class)
                        .selectAll(ManageApplicationEntity.class)
                        .leftJoin(UserApproveEntity.class, UserApproveEntity::getUserId, ManageApplicationEntity::getAppUserId)
                        .orderByDesc(ManageApplicationEntity::getAppCreatetime)
        );
        userApproveEntityPage.getRecords().forEach(data-> {
//            long time = data.getAppCreatetime().getTime();
//            data.setApptime(time);
            data.setApptime(TimeTolong.timetolong(data.getAppCreatetime()));
        });

        return Result.success(userApproveEntityPage);

    }

    /**
     * 新增应用
     */
    @Operation(summary = "添加应用")
    @PostMapping("/addInfo")
    public Result<?> add(@RequestBody ManageApplicationEntity manageApplication) {
        manageApplication.setAppCreatetime(new Date());
        manageApplication.setAppUpdatetime(new Date());
        manageApplication.setAppIsdelete(0);
        manageApplicationService.save(manageApplication);
        return Result.success();
    }

    @Operation(summary = "删除应用")
    @PostMapping("/deleteApp")
    public Result<?> delete(@RequestBody ManageApplicationEntity manageApplication) {
        LambdaUpdateWrapper<ManageApplicationEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppId, manageApplication.getAppId());
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppUserId, manageApplication.getAppUserId());
        return Result.success(manageApplicationService.remove(lambdaUpdateWrapper));
    }

    //查询appid和name
    @Operation(summary = "查询应用")
    @GetMapping("/selectappID")
    public Result selectappID(@Parameter(description = "appID") String appId, @Parameter(description = "用户Id") String appUserId) {

        QueryWrapper<ManageApplicationEntity> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(Objects.nonNull(appId), ManageApplicationEntity::getAppId, appId)
                .eq(Objects.nonNull(appUserId), ManageApplicationEntity::getAppUserId, appUserId);


        return Result.success(manageApplicationMapper.selectList(wrapper));
    }



    //修改应用信息
    @Operation(summary = "修改应用")
    @PostMapping("/upadataAppInfo")
    public Result<?> upadataAppList(@RequestBody ManageApplicationEntity manageApplication){
        LambdaUpdateWrapper<ManageApplicationEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppId, manageApplication.getAppId());
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppUserId, manageApplication.getAppUserId());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppUpdatetime, new Date());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppIsdelete, 0);
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppName, manageApplication.getAppName());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppSynopsis, manageApplication.getAppSynopsis());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppUserId, manageApplication.getAppUserId());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppIconpath, manageApplication.getAppIconpath());
        return Result.success(manageApplicationMapper.update(lambdaUpdateWrapper));
    }




}