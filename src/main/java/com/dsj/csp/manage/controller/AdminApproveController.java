package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.service.UserApproveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
@Tag(name = "管理员实名认证审核")
@RestController
@RequestMapping("/adminApprove")
public class AdminApproveController {
    @Autowired
    private UserApproveService userApproveService;


    /**
     * 按条件分页查询实名认证申请
     * @param user
     * @param startTime
     * @param endTime
     * @return
     */
    @Operation(summary = "按条件分页查询实名认证申请")
    @GetMapping("/select")
    public Result<?> select(UserApproveEntity user, @Parameter(description = "开始时间（可为空）") Date startTime, @Parameter(description = "最后时间（可为空）")Date endTime, int page, int size){
        return Result.success(userApproveService.select(user, startTime, endTime,page,size));
    }

    /**
     * 查看实名申请详情
     * @param userId
     * @return
     */
    @Operation(summary = "查看实名申请详情")
    @GetMapping("/find")
    public Result<?> find(@Parameter(description = "用户ID")String userId){
        return Result.success(userApproveService.find(userId));
    }

    /**
     * 实名认证审核通过
     * @param user
     * @return
     */
    @Operation(summary = "实名认证审核通过")
    @PostMapping("/approveSuccess")
    public Result<?> approveSuccess(@RequestBody UserApproveEntity user){
        userApproveService.approveSuccess(user);
        return Result.success("审核通过");
    }

    /**
     * 实名认证审核未通过
     * @param user
     * @return
     */
    @Operation(summary = "实名认证审核未通过")
    @PostMapping("/approveFail")
    public Result<?> approveFail(@RequestBody UserApproveEntity user){
        userApproveService.approveFail(user);
        return Result.success("审核未通过");
    }


}
