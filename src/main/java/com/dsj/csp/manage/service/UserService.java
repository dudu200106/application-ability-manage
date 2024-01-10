package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;

/**
* @author Stars
* @description 针对表【MANAGE_USER(用户信息表（个人信息，认证信息）)】的数据库操作Service
* @createDate 2024-01-09 14:16:26
*/
public interface UserService extends IService<UserEntity> {
    /**
     * 用户模块
     */
    //根据id查看个人中心
    UserEntity personCenter(String userId);
    //修改用户信息
    void updateUser(UserEntity user);
    //用户申请实名认证
    void approve(UserEntity user);
    //上传图片
    String handleFileUpload(MultipartFile file);

    /**
     * 管理员实名认证审核模块
     */
    //条件查询实名认证审核申请
    List<UserEntity> select(UserEntity user, Date startTime,Date endTime);
    //分页查询实名认证审核申请
    Page<UserEntity> search(int page,int size);
    //查看实名认证申请详情
    UserEntity find(String userId);
    //实名认证审核通过
    void approveSuccess(UserEntity user);
    //实名认证审核未通过
    void approveFail(UserEntity user);

}
