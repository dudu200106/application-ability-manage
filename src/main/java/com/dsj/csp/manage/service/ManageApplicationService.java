package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.Date;
import java.util.List;

/**
* @author DSCBooK
* @description 针对表【MANAGE_APPLICATION(应用列表)】的数据库操作Service
* @createDate 2024-01-11 10:43:10
*/
public interface ManageApplicationService extends IService<ManageApplicationEntity> {

    List<ManageApplicationEntity> selectappID(Long appId , String appUserId );
    int contAll();
//    boolean updateSecret(Long appId);
    boolean upadataAppList(Long appId,String appName,String appSynopsis,String appIconpath,String appUserId);
    boolean updateIsdetele(Long appId,String appUserId);
//    IPage<ManageApplication> selectJoinPage(Page<Object> objectPage, Class<ManageApplication> manageApplicationClass, MPJLambdaWrapper<Object> eq);

//    List<ManageApplication> selectPages(LambdaQueryWrapper<ManageApplication> wrapper, String appUserId, String keyword, Date startTime, Date endTime, int page, int size);

}
