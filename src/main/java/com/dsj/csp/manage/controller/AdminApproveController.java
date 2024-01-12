package com.dsj.csp.manage.controller;

import com.dsj.common.dto.Result;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.service.UserApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/adminApprove")
public class AdminApproveController {
    @Autowired
    private UserApproveService userApproveService;

    @GetMapping("/search")
    public Result<?> search(int page,int size){
        return Result.success(userApproveService.search(page, size));
    }

    /**
     * 按条件查询实名认证申请
     * @param user
     * @param startTime
     * @param endTime
     * @return
     */
    @GetMapping("/select")
    public Result<?> select(UserApproveEntity user, Date startTime, Date endTime){
        return Result.success(userApproveService.select(user, startTime, endTime));
    }

    /**
     * 查看实名申请详情
     * @param userId
     * @return
     */
    @GetMapping("/find")
    public Result<?> find(String userId){
        return Result.success(userApproveService.find(userId));
    }

    /**
     * 实名认证审核通过
     * @param user
     * @return
     */
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
    @PostMapping("/approveFail")
    public Result<?> approveFail(@RequestBody UserApproveEntity user){
        userApproveService.approveFail(user);
        return Result.success("审核未通过");
    }


}
