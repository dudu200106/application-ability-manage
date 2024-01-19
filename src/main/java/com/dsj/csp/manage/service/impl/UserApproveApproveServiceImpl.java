package com.dsj.csp.manage.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.common.enums.UserStatusEnum;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.mapper.UserApproveMapper;
import com.dsj.csp.manage.service.UserApproveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

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

    /**
     * 用户实名认证申请模块
     */
    @Override
    public UserApproveEntity personCenter(String userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public void approve(UserApproveEntity user) {
        UserApproveEntity userApproveEntity = baseMapper.selectById(user);
        Integer status = userApproveEntity.getStatus();
        if (status.equals(UserStatusEnum.NOAPPROVE) || status.equals(UserStatusEnum.FAIL)) {
            user.setStatus(UserStatusEnum.WAIT.getStatus());
            user.setNote(null);
            user.setCreateTime(new Date());
            baseMapper.updateById(user);
        }
//        this.lambdaUpdate()
//                .eq(UserApproveEntity::getStatus,UserStatusEnum.NOAPPROVE)
//                .or()
//                .eq(UserApproveEntity::getStatus,UserStatusEnum.FAIL)
//                .set(UserApproveEntity::getStatus,UserStatusEnum.WAIT)
//                .set(UserApproveEntity::getNote,null)
//                .update();
    }

    /**
     * 管理员实名认证审核模块
     */
    @Override
    public Page<UserApproveEntity> select(String status,String keyword, Date startTime, Date endTime, int page, int size) {
        QueryWrapper<UserApproveEntity> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(Objects.nonNull(status), UserApproveEntity::getStatus, status)
                .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), UserApproveEntity::getCreateTime, startTime, endTime)
                .and(StringUtils.isNotBlank(keyword),lambdaQuery->{
                    lambdaQuery
                            .like(UserApproveEntity::getGovName, keyword)
                            .or()
                            .like(UserApproveEntity::getCompanyName, keyword);
        });
        return baseMapper.selectPage(new Page(page, size), wrapper);
    }

    @Override
    public UserApproveEntity find(String userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public void approveSuccess(UserApproveRequest user) {
        UserApproveEntity userApproveEntity = baseMapper.selectById(user.getUserId());
        boolean updateResult = this.lambdaUpdate()
                .eq(Objects.nonNull(userApproveEntity.getStatus()), UserApproveEntity::getStatus, UserStatusEnum.WAIT.getStatus())
                .eq(UserApproveEntity::getUserId, user.getUserId())
                .set(UserApproveEntity::getStatus, UserStatusEnum.SUCCESS.getStatus())
                .set(UserApproveEntity::getNote, "实名认证已完成")
                .update();
        if (!updateResult) {
            log.error("更新失败");
            throw new BusinessException("更新失败");
        }
//          FIXME  return updateResult; 在controller做判断并进行不同的响应
        // TODO 为每一次的sql结果负责
    }

    @Override
    public void approveFail(UserApproveRequest user) {
        UserApproveEntity userApproveEntity = baseMapper.selectById(user.getUserId());
        boolean updateResult = this.lambdaUpdate()
                .eq(Objects.nonNull(userApproveEntity.getStatus()), UserApproveEntity::getStatus, UserStatusEnum.WAIT.getStatus())
                .eq(UserApproveEntity::getUserId, user.getUserId())
                .set(UserApproveEntity::getStatus, UserStatusEnum.FAIL.getStatus())
                .set(UserApproveEntity::getNote, user.getNote())
                .set(UserApproveEntity::getUserType, 0)
                .update();
        if (!updateResult) {
            log.error("更新失败");
            throw new BusinessException("更新失败");
        }
    }

    @Override
    public Long userCount() {
        return baseMapper.selectCount(null);
    }
}