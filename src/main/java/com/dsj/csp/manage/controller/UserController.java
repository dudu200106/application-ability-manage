package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.entity.UserEntity;
import com.dsj.csp.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/personCenter")
    public Result<?> personCenter(String userId){
        return Result.success(userService.personCenter(userId));
    }
    /**
     * 实名认证审核申请
     * @param user
     * @return
     */
    @PostMapping("/approve")
    public Result<?> approve(@RequestBody UserEntity user){
        userService.approve(user);
        return Result.success("实名认证审核中");
    }

    /**
     * 上传图片接口
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String path= userService.handleFileUpload(file);
        return Result.success(path);
        // 图片保存，返回路径
        // 数据表中保存路径
    }
}
