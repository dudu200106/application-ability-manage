package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.service.UserApproveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@Tag(name = "用户提交实名认证申请")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserApproveController {
    private final UserApproveService userApproveService;

    @Operation(summary = "个人中心")
    @GetMapping("/personCenter")
    public Result<?> personCenter(){
        return Result.success(userApproveService.callRemoteService());
    }
    /**
     * 实名认证申请
     * @param user
     * @return
     */
    @Operation(summary = "实名认证申请")
    @PostMapping("/approve")
    public Result<?> approve(@RequestBody UserApproveEntity user){
        userApproveService.approve(user);
        return Result.success("实名认证审核中");
    }
}