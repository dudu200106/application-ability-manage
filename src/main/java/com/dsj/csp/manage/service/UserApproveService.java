package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.UserApproveEntity;

import java.util.Date;

/**
* @author Stars
* @description 针对表【MANAGE_USER(用户信息表（个人信息，认证信息）)】的数据库操作Service
* @createDate 2024-01-09 14:16:26
*/
public interface UserApproveService extends IService<UserApproveEntity> {
    /**
     * 用户模块
     */
    //根据id查看个人中心
    UserApproveEntity personCenter(String userId);
    //用户申请实名认证
    void approve(UserApproveEntity user);

    /**
     * 管理员实名认证审核模块
     */
    //条件分页查询实名认证审核申请
    Page<UserApproveEntity> select(String status,String keyword, Date startTime, Date endTime,int page, int size);
    //查看实名认证申请详情
    UserApproveEntity find(String userId);
    //实名认证审核通过
    void approveSuccess(UserApproveRequest user);
    //实名认证审核未通过
    void approveFail(UserApproveRequest user);
    //统计用户总数
    Long userCount();

}
