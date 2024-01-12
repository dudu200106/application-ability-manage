package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.common.enums.UserStatusEnum;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.mapper.UserApproveMapper;
import com.dsj.csp.manage.service.UserApproveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
public class UserApproveApproveServiceImpl extends ServiceImpl<UserApproveMapper, UserApproveEntity> implements UserApproveService {
    @Autowired
    private UserApproveMapper userApproveMapper;
    /**
     * 用户实名认证申请模块
     */
    @Override
    public UserApproveEntity personCenter(String userId) {
        return userApproveMapper.selectById(userId);
    }

    @Override
    public void updateUser(UserApproveEntity user) {

    }

    @Override
    public void approve(UserApproveEntity user) {
        UserApproveEntity userApproveEntity = userApproveMapper.selectById(user);
        Integer status = userApproveEntity.getStatus();
        if (status.equals(0)||status.equals(3)) {
            user.setStatus(UserStatusEnum.WAIT.getStatus());
            user.setCreateTime(new Date());
            userApproveMapper.updateById(user);
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
    public List<UserApproveEntity> select(UserApproveEntity user, Date startTime, Date endTime) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(user.getStatus()), UserApproveEntity::getStatus, user.getStatus())
                .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), UserApproveEntity::getCreateTime, startTime, endTime)
                .like(StringUtils.isNotBlank(user.getGovName()), UserApproveEntity::getGovName, user.getGovName())
                .like(StringUtils.isNotBlank(user.getCompanyName()), UserApproveEntity::getCompanyName, user.getCompanyName())
                .list();
    }

    @Override
    public Page<UserApproveEntity> search(int page, int size) {
        return userApproveMapper.selectPage(new Page(page,size),null);
    }

    @Override
    public UserApproveEntity find(String userId) {
        return userApproveMapper.selectById(userId);
    }

    @Override
    public void approveSuccess(UserApproveEntity user) {
        boolean updateResult = this.lambdaUpdate()
                .eq(Objects.nonNull(user.getStatus()), UserApproveEntity::getStatus, UserStatusEnum.WAIT.getStatus())
                .eq(UserApproveEntity::getUserId, user.getUserId())
                .set(UserApproveEntity::getStatus, UserStatusEnum.SUCCESS.getStatus())
                .set(UserApproveEntity::getNote, "实名认证已完成")
                .update();
        if(!updateResult){
        log.error("更新失败");
            throw new BusinessException("更新失败");
        }
//          FIXME  return updateResult; 在controller做判断并进行不同的响应
        // TODO 为每一次的sql结果负责
    }

    @Override
    public void approveFail(UserApproveEntity user) {
        boolean updateResult = this.lambdaUpdate()
                .eq(Objects.nonNull(user.getStatus()), UserApproveEntity::getStatus, UserStatusEnum.WAIT.getStatus())
                .eq(UserApproveEntity::getUserId, user.getUserId())
                .set(UserApproveEntity::getStatus, UserStatusEnum.FAIL.getStatus())
                .set(UserApproveEntity::getNote, user.getNote())
                .update();
        if(!updateResult){
            log.error("更新失败");
            throw new BusinessException("更新失败");
        }
    }

    @Override
    public Long userCount() {
        return userApproveMapper.selectCount(null);
    }
}




