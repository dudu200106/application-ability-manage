package com.dsj.csp.manage.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.service.UserApproveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
@Tag(name = "管理员实名认证审核")
@RestController
@RequiredArgsConstructor
@RequestMapping("/adminApprove")
public class AdminApproveController {
    private final UserApproveService userApproveService;
    /**
     * 按条件分页查询实名认证申请
     * @param status 用户实名状态
     * @param keyword 名称关键字
     * @param startTime 开始时间
     * @param endTime 最后时间
     * @param page
     * @param size
     * @return
     */
    @Operation(summary = "按条件分页查询实名认证申请")
    @GetMapping("/select")
    @AopLogger(describe = "按条件分页查询实名认证申请",operateType = LogEnum.SELECT,logType = LogEnum.OPERATETYPE)
    public Result<Page<UserApproveEntity>> select(@Parameter(description = "实名状态（可为空）")String status, @Parameter(description = "名称关键字（可为空）")String keyword, @Parameter(description = "开始时间（可为空）") Date startTime, @Parameter(description = "最后时间（可为空）")Date endTime, int page, int size){
        return Result.success(userApproveService.select(status,keyword, startTime, endTime,page,size));
    }

    /**
     * 查看实名申请详情
     * @param userId
     * @return
     */
    @Operation(summary = "查看实名申请详情")
    @GetMapping("/find")
    @AopLogger(describe = "查看实名申请详情",operateType = LogEnum.SELECT,logType = LogEnum.OPERATETYPE)
    public Result<UserApproveEntity> find(@Parameter(description = "用户ID")String userId){
        return Result.success(userApproveService.find(userId));
    }

    /**
     * 实名认证审核通过
     * @param user
     * @return
     */
    @Operation(summary = "实名认证审核通过")
    @PostMapping("/approveSuccess")
    @AopLogger(describe = "实名认证审核通过",operateType = LogEnum.UPDATE,logType = LogEnum.OPERATETYPE)
    public Result<String> approveSuccess(@RequestBody UserApproveRequest user){
        userApproveService.approveSuccess(user);
        return Result.success("审核通过");
    }
//
//    @PostMapping(value = "/{userId}/edit")
//    public Result<String> editUser(@PathVariable(value = "userId") String userId, @RequestBody UserIdRequest request) {
//        return Result.success();
//    }

    /**
     * 实名认证审核未通过
     * @param user
     * @return
     */
    @Operation(summary = "实名认证审核未通过")
    @PostMapping("/approveFail")
    @AopLogger(describe = "实名认证审核未通过",operateType = LogEnum.UPDATE,logType = LogEnum.OPERATETYPE)
    public Result<?> approveFail(@RequestBody UserApproveRequest user){
        userApproveService.approveFail(user);
        return Result.success("审核未通过");
    }

    /**
     * 按条件分页查询用户信息
     * @param keyword
     * @param startTime
     * @param endTime
     * @param page
     * @param size
     * @return
     */
    @Operation(summary = "按条件分页查询用户")
    @GetMapping("/selectUser")
    @AopLogger(describe = "按条件分页查询用户",operateType = LogEnum.SELECT,logType = LogEnum.OPERATETYPE)
    public Result<Page<UserApproveEntity>> selectUser(@Parameter(description = "名称关键字（可为空）")String keyword, @Parameter(description = "开始时间（可为空）") Date startTime, @Parameter(description = "最后时间（可为空）")Date endTime, int page, int size){
        return Result.success(userApproveService.selectUser(keyword, startTime, endTime,page,size));
    }
}
