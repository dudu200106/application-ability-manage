package com.dsj.csp.manage.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.enums.StatusEnum;
import com.dsj.csp.manage.dto.ManageApplictionVo;
import com.dsj.csp.manage.dto.PageQueryForm;
import com.dsj.csp.manage.entity.ManageApplication;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.mapper.UserApproveMapper;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.service.UserApproveService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    /**
     * 分页查询
     */
    @Operation(summary = "查询所有数据")
    @PostMapping("/lists")
    public Result<List<ManageApplication>> list() {
        return Result.success(manageApplicationService.list());
    }

    /**
     * 分页查询
     */
    @Operation(summary = "分页查询")
    @GetMapping("/selectPage")
    public Result<Page<ManageApplictionVo>> selectPage(@Parameter(description = "用户id") String appUserId, @Parameter(description = "查询关键字 Id或名称") String keyword, @Parameter(description = "开始时间") Date startTime, @Parameter(description = "结束时间") Date endTime, @Parameter int size, @Parameter int pages) {
        if (!StringUtils.isEmpty(keyword)) {
            System.out.println(startTime);
            System.out.println(endTime);
            Page<ManageApplictionVo> userApproveEntityPage = userApproveMapper.selectJoinPage(new Page<>(pages, size), ManageApplictionVo.class,
                    new MPJLambdaWrapper<UserApproveEntity>()
                            .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), ManageApplication::getAppCreatetime, startTime, endTime)
                            .like(ManageApplication::getAppName, keyword)
                            .or().like(ManageApplication::getAppCode, keyword)
                            .selectAll(UserApproveEntity.class)
                            .selectAll(ManageApplication.class)
                            .leftJoin(ManageApplication.class, ManageApplication::getAppUserId, UserApproveEntity::getUserId));
            return Result.success(userApproveEntityPage);
        } else {
            Page<ManageApplictionVo> userApproveEntityPage = userApproveMapper.selectJoinPage(new Page<>(pages, size), ManageApplictionVo.class,
                    new MPJLambdaWrapper<UserApproveEntity>()
                            .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), ManageApplication::getAppCreatetime, startTime, endTime)
                            .selectAll(UserApproveEntity.class)
                            .selectAll(ManageApplication.class)
                            .leftJoin(ManageApplication.class, ManageApplication::getAppUserId, UserApproveEntity::getUserId));
            return Result.success(userApproveEntityPage);
        }
    }


    /**
     * 新增应用
     */
    @Operation(summary = "添加应用")
    @PostMapping("/addInfo")
    public Result<?> add(@Parameter(description = "应用图片路径") String appIconpath, @Parameter(description = "应用名字") String appName, @Parameter(description = "应用简介") String appSynopsis, @Parameter(description = "用户Id") String userId) {
        ManageApplication manageApplication = new ManageApplication();
        manageApplication.setAppName(appName);

        manageApplication.setAppUserId(userId);
        manageApplication.setAppSynopsis(appSynopsis);

        manageApplication.setAppCode(generateNumber(8));//生成appid
        manageApplication.setAppIconpath(appIconpath);//应用路径
//            状态
        manageApplication.setAppStatus(StatusEnum.NORMAL.getStatus());
//            逻辑删除
        manageApplication.setAppIsdelete(0);
        manageApplication.setAppCreatetime(new Date());
        manageApplication.setAppUpdatetime(new Date());
        return Result.success(manageApplicationService.save(manageApplication));
    }

    @Operation(summary = "删除应用")
    @PostMapping("/delete")
    public Result<?> delete(@Parameter(description = "appID") Long appId, @Parameter(description = "用户Id") String appUserId) {
        return Result.success(manageApplicationService.updateIsdetele(appId, appUserId));
    }

    //查询appid和name
    @Operation(summary = "查询应用")
    @PostMapping("/selectappID")
    public Result selectappID(@Parameter(description = "appID") Long appId, @Parameter(description = "用户Id") String appUserId) {
        return Result.success(manageApplicationService.selectappID(appId, appUserId));
    }

    //统计应用次数
    @Operation(summary = "统计应用总数")
    @GetMapping("/allTotal")
    public Result countAll() {
        return Result.success(manageApplicationService.count());
    }

    //用户关联应用查询
    @Operation(summary = "用户下的应用")
    @PostMapping("/selectUserApp")
    public Result selectUserApp(@Parameter(description = "用户Id") String appUserId) {
        return Result.success(manageApplicationService.selectUserApp(appUserId));
    }

    //修改应用信息
    @Operation(summary = "修改应用")
    @PostMapping("/upadataAppInfo")
    public Result<?> upadataAppList(@Parameter(description = "应用图片路径") String appIconpath, @Parameter(description = "id") Long appId, @Parameter(description = "名称") String appName, @Parameter(description = "简介") String appSynopsis, @Parameter(description = "用户id") String appUserId) throws IOException {
        return Result.success(manageApplicationService.upadataAppList(appId, appName, appSynopsis, appIconpath, appUserId));
    }


}