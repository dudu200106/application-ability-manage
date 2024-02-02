package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.dto.response.UserApproveResponse;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.service.UserApproveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@Tag(name = "用户提交实名认证申请")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserApproveController {
    private final UserApproveService userApproveService;

    /**
     * 实名认证申请
     * @param user
     * @return
     */
    @Operation(summary = "实名认证申请")
    @PostMapping("/approve")
    public Result<?> approve(@RequestBody @Valid UserApproveEntity user, @RequestHeader("accessToken") String accessToken){
        return Result.success(userApproveService.approve(user,accessToken));
    }

    @Operation(summary = "根据token获取ID回显用户信息/根据token查看用户实名信息")
    @GetMapping("/echo")
    public Result<?> echo(@RequestHeader("accessToken") String accessToken){
        return Result.success(userApproveService.echo(accessToken));
    }

    @Operation(summary = "用户修改密码")
    @PostMapping("/updatePassword")
    public Result<?> updatePassword(@RequestBody @Valid UserApproveResponse userApproveResponse, @RequestHeader("accessToken") String accessToken){
        return userApproveService.updatePassword(userApproveResponse,accessToken);
    }
}