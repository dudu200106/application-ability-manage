package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.entity.UserEntity;
import com.dsj.csp.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/adminApprove")
public class AdminApproveController {
    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public Result<?> search(int page,int size){
        return Result.success(userService.search(page, size));
    }

    /**
     * 按条件查询实名认证申请
     * @param user
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/select")
    public Result<?> select(UserEntity user, Date startTime, Date endTime){
        return Result.success(userService.select(user, startTime, endTime));
    }

    /**
     * 查看实名申请详情
     * @param userId
     * @return
     */
    @GetMapping("/find")
    public Result<?> find(String userId){
        return Result.success(userService.find(userId));
    }

    /**
     * 实名认证审核通过
     * @param user
     * @return
     */
    @PostMapping("/approveSuccess")
    public Result<?> approveSuccess(@RequestBody UserEntity user){
        userService.approveSuccess(user);
        return Result.success("审核通过");
    }

    /**
     * 实名认证审核未通过
     * @param user
     * @return
     */
    @PostMapping("/approveFail")
    public Result<?> approveFail(@RequestBody UserEntity user){
        userService.approveFail(user);
        return Result.success("审核未通过");
    }

}
