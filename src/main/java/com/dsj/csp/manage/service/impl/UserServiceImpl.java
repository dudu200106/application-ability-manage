package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.enums.UserStatusEnum;
import com.dsj.csp.manage.entity.UserEntity;
import com.dsj.csp.manage.mapper.UserMapper;
import com.dsj.csp.manage.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Stars
 * @description 针对表【MANAGE_USER(用户信息表（个人信息，认证信息）)】的数据库操作Service实现
 * @createDate 2024-01-09 14:16:26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    @Autowired
    private UserMapper userMapper;
    /**
     * 用户实名认证申请模块
     */
    @Override
    public UserEntity personCenter(String userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public void updateUser(UserEntity user) {

    }

    @Override
    public void approve(UserEntity user) {
        UserEntity userEntity = userMapper.selectById(user);
        Integer status = userEntity.getStatus();
        if (status.equals(0)||status.equals(3)) {
            user.setStatus(UserStatusEnum.WAIT.getStatus());
            user.setCreateTime(new Date());
            userMapper.updateById(user);
        }
    }

    @Override
    public String handleFileUpload(MultipartFile file){
        // 图片保存，返回路径
        // 数据表中保存路径
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的字节数组
            byte[] bytes = file.getBytes();
            // 构建文件路径
            Path path = Paths.get("D:/picture/" + fileName);
            // 将文件保存到本地
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传图片失败";
    }


    /**
     * 管理员实名认证审核模块
     */
    @Override
    public List<UserEntity> select(UserEntity user, Date startTime, Date endTime) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(user.getStatus()), UserEntity::getStatus, user.getStatus())
                .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), UserEntity::getCreateTime, startTime, endTime)
                .like(StringUtils.isNotBlank(user.getGovName()), UserEntity::getGovName, user.getGovName())
                .like(StringUtils.isNotBlank(user.getCompanyName()), UserEntity::getCompanyName, user.getCompanyName())
                .list();
    }

    @Override
    public Page<UserEntity> search(int page, int size) {
        return userMapper.selectPage(new Page(page,size),null);
    }

    @Override
    public UserEntity find(String userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public void approveSuccess(UserEntity user) {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Objects.nonNull(userMapper.selectById(user).getStatus()),UserEntity::getStatus,UserStatusEnum.WAIT.getStatus());
        user.setStatus(UserStatusEnum.SUCCESS.getStatus());
        user.setNote("实名认证已完成");
        userMapper.update(user,wrapper);
    }

    @Override
    public void approveFail(UserEntity user) {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Objects.nonNull(userMapper.selectById(user).getStatus()),UserEntity::getStatus,UserStatusEnum.WAIT.getStatus());
        user.setStatus(UserStatusEnum.FAIL.getStatus());
        user.setNote(user.getNote());
        userMapper.update(user,wrapper);
    }
}




