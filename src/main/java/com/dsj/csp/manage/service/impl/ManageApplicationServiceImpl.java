package com.dsj.csp.manage.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.dto.ManageApplictionVo;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.mapper.ManageApplicationMapper;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.util.TimeTolong;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author DSCBooK
 * @description 针对表【MANAGE_APPLICATION(应用列表)】的数据库操作Service实现
 * @createDate 2024-01-11 10:43:10
 */
@Service
public class ManageApplicationServiceImpl extends ServiceImpl<ManageApplicationMapper, ManageApplicationEntity>
        implements ManageApplicationService {
    @Autowired
    private ManageApplicationMapper manageApplicationMapper;

    @Override
    public Long countAppUser(String appUserId) {
        LambdaUpdateWrapper<ManageApplicationEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppUserId, appUserId);
        return baseMapper.selectCount(lambdaUpdateWrapper);
    }

    @Override
    public int saveApp(ManageApplicationEntity manageApplication) {
        manageApplication.setAppCreatetime(new Date());
        manageApplication.setAppUpdatetime(new Date());
        manageApplication.setAppIsdelete(0);

        return baseMapper.insert(manageApplication);
    }

    @Override
    public int deleteApp(ManageApplicationEntity manageApplication) {
        LambdaUpdateWrapper<ManageApplicationEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppId, manageApplication.getAppId());
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppUserId, manageApplication.getAppUserId());
        return baseMapper.delete(lambdaUpdateWrapper);
    }

    @Override
    public List selectappID(String appId, String appUserId) {
        MPJLambdaWrapper<ManageApplicationEntity> wrapper = new MPJLambdaWrapper<ManageApplicationEntity>()
                .selectAll(ManageApplicationEntity.class)//查询user表全部字段
                .selectAll(UserApproveEntity.class)
                .leftJoin(UserApproveEntity.class, UserApproveEntity::getUserId, ManageApplicationEntity::getAppUserId)
        .eq(ManageApplicationEntity::getAppId, appId)
        .eq(ManageApplicationEntity::getAppUserId, appUserId);
//        List<ManageApplictionVo> userList = ;
//        userList.forEach(System.out::println);
//     userMapper.selectJoinList(UserDTO.class, wrapper);
//        ManageApplictionVo userApproveEntityPage =
//                new MPJLambdaWrapper<ManageApplicationEntity>()
//                        .selectAll(UserApproveEntity.class)
//                        .selectAll(ManageApplicationEntity.class)
//                        .leftJoin(UserApproveEntity.class, UserApproveEntity::getUserId, ManageApplicationEntity::getAppUserId));
        return baseMapper.selectJoinList(ManageApplictionVo.class, wrapper);
    }

    @Override
    public int upadataAppInfo(ManageApplicationEntity manageApplicationEntity) {
        LambdaUpdateWrapper<ManageApplicationEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppId, manageApplicationEntity.getAppId());
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppUserId, manageApplicationEntity.getAppUserId());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppUpdatetime, new Date());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppIsdelete, 0);
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppName, manageApplicationEntity.getAppName());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppSynopsis, manageApplicationEntity.getAppSynopsis());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppUserId, manageApplicationEntity.getAppUserId());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppIconpath, manageApplicationEntity.getAppIconpath());
        return baseMapper.update(lambdaUpdateWrapper);

    }

    @Override
    public Page<ManageApplictionVo> selectPage(String appUserId, String keyword, Date startTime, Date endTime, int pages, int size) {

        Page<ManageApplictionVo> userApproveEntityPage = manageApplicationMapper.selectJoinPage(new Page<>(pages, size), ManageApplictionVo.class,
                new MPJLambdaWrapper<ManageApplicationEntity>()
                        .eq(!StringUtils.isEmpty(appUserId), ManageApplicationEntity::getAppUserId, appUserId)
                        .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), ManageApplicationEntity::getAppCreatetime, startTime, endTime)
                        .like(!StringUtils.isEmpty(keyword), ManageApplicationEntity::getAppName, keyword)
                        .or().like(!StringUtils.isEmpty(keyword), ManageApplicationEntity::getAppName, keyword)
                        .selectAll(UserApproveEntity.class)
                        .selectAll(ManageApplicationEntity.class)
                        .leftJoin(UserApproveEntity.class, UserApproveEntity::getUserId, ManageApplicationEntity::getAppUserId)
                        .orderByDesc(ManageApplicationEntity::getAppCreatetime)
        );
        userApproveEntityPage.getRecords().forEach(data -> {
//            long time = data.getAppCreatetime().getTime();
//            data.setApptime(time);
            data.setApptime(TimeTolong.timetolong(data.getAppCreatetime()));
        });
        return userApproveEntityPage;
    }


}