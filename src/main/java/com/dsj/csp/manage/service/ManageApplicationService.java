package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.dto.ManageApplictionVo;
import com.dsj.csp.manage.entity.ManageApplicationEntity;

import java.util.Date;
import java.util.List;

/**
* @author DSCBooK
* @description 针对表【MANAGE_APPLICATION(应用列表)】的数据库操作Service
* @createDate 2024-01-11 10:43:10
*/
public interface ManageApplicationService extends IService<ManageApplicationEntity> {




//查询用户下应用
    Long countAppUser(String  appUserId);
    int saveApp(ManageApplicationEntity manageApplication);

    int deleteApp(ManageApplicationEntity manageApplication);

  List<ManageApplicationEntity> selectappID(String appId, String appUserId);


    int upadataAppInfo(ManageApplicationEntity manageApplicationEntity);


    Page<ManageApplictionVo>  selectPage(String appUserId, String keyword, Date startTime, Date endTime, int pages, int size);
//    Page<ManageApplictionVo> selectPage(String appUserId, String keyword, Date startTime, Date endTime, int page, int size);
//    IPage<ManageApplication> selectJoinPage(Page<Object> objectPage, Class<ManageApplication> manageApplicationClass, MPJLambdaWrapper<Object> eq);

//    List<ManageApplication> selectPages(LambdaQueryWrapper<ManageApplication> wrapper, String appUserId, String keyword, Date startTime, Date endTime, int page, int size);

}
