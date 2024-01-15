package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.service.UserApproveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@Tag(name = "用户提交实名认证申请")
@RestController
@RequestMapping("/user")
public class UserApproveController {
    @Autowired
    private UserApproveService userApproveService;

    @Operation(summary = "个人中心‘待完善’")
    @GetMapping("/personCenter")
    public Result<?> personCenter(String userId){
        return Result.success(userApproveService.personCenter(userId));
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
