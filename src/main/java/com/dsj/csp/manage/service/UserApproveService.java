package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.common.dto.Result;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.dto.response.UserApproveResponse;
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
    //用户申请实名认证
    String approve(UserApproveEntity user,String accessToken);
    //回显用户信息到前端
    UserApproveEntity echo(String accessToken);

    //用户修改实名认证信息

    //远程调用用户接口，根据token识别用户
    UserApproveRequest identify(String accessToken);
    //远程调用用户实名状态更新接口
    void approveFeign(String userId,Integer status);
    //用户修改密码
    Result<Boolean> updatePassword(UserApproveResponse userApproveResponse, String accessToken);

    /**
     * 管理员实名认证审核模块
     */
    //条件分页查询实名认证审核申请
    Page<UserApproveEntity> select(String status,String keyword, Date startTime, Date endTime,int page, int size);
    //查看实名认证申请详情
    UserApproveEntity find(String userId);
    //实名认证审核通过
    void approveSuccess(UserApproveRequest user, String accessToken);
    //实名认证审核未通过
    void approveFail(UserApproveRequest user,String accessToken);
    //统计用户总数

    Long userCount();

}

