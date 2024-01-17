package com.dsj.csp.manage.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.ManageApplication;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.mapper.ManageApplicationMapper;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.util.Sm4;
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
public class ManageApplicationServiceImpl extends ServiceImpl<ManageApplicationMapper, ManageApplication>
        implements ManageApplicationService {
//    @Autowired
//    ManageApplicationMapper manageApplicationMapper;

    @Override
    public List<ManageApplication> selectappID(Long appId, String appUserId) {
        return baseMapper.selectappID(appId, appUserId);
    }

    @Override
    public int contAll() {
        return baseMapper.contAll();
    }

    @Override
    public List<ManageApplication> selectUserApp(String appUserId) {
        return baseMapper.selectUserApp(appUserId);
    }


//    @Override
//    public boolean updateSecret(Long appId) {
//        String appKey = Sm4.sm();
//        String appSecret = Sm4.sm();
//        return baseMapper.updateSecret(appId, appKey, appSecret);
//    }

    @Override
    public boolean upadataAppList(Long appId, String appName, String appSynopsis, String appIconpath, String appUserId) {
        return baseMapper.upadataAppList(appId, appName, appSynopsis, appIconpath, appUserId);
    }

    @Override
    public boolean updateIsdetele(Long appId, String appUserId) {
        return baseMapper.updateIsdetele(appId, appUserId);
    }

//    @Override
//    public IPage<ManageApplication> selectJoinPage(Page<Object> objectPage, Class<ManageApplication> manageApplicationClass, MPJLambdaWrapper<Object> eq) {
//      return   manageApplicationMapper.selectJoinPage(new Page<>(page, size), ManageApplication.class,
//                new MPJLambdaWrapper<>()
//                        .selectAll(ManageApplication.class)
//                        .selectAll(UserApproveEntity.class)
//                        .leftJoin(ManageApplication.class, ManageApplication::getAppUserId, UserApproveEntity::getUserId)
//                        .eq(ManageApplication::getAppUserId, appUserId));
//    }

    //    @Override
//    public Page<ManageApplication> selectPage(String appUserId, String keyword, Date startTime, Date endTime, int page, int size) {
//
//    manageApplicationMapper.selectJoinPage(new Page<>(page, size), ManageApplication.class,
//                new MPJLambdaWrapper<>()
//                        .selectAll(ManageApplication.class)
//                        .selectAll(UserApproveEntity.class)
//                        .leftJoin(ManageApplication.class, ManageApplication::getAppUserId, UserApproveEntity::getUserId)
//                        .eq(ManageApplication::getAppUserId, appUserId));
////        System.out.println(keyword);
////        if (!StringUtils.isEmpty(keyword)) {
////            wrapper
////                    .or().like(ManageApplication::getAppName, keyword)
////                    .or().like(ManageApplication::getAppCode, keyword)
////                    .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), ManageApplication::getAppCreatetime, startTime, endTime);
////        } else {
////            wrapper.between(Objects.nonNull(startTime) && Objects.nonNull(endTime), ManageApplication::getAppCreatetime, startTime, endTime)
////                    .inSql(ManageApplication ::getAppUserId, "");
////        }
//
//
////        return manageApplicationMapper.selectPages(new Page(page,size),null);
//        return manageApplicationMapper.selectPages(appUserId, keyword, startTime, endTime, page, size);
//    }


}




