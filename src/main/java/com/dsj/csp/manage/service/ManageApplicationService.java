package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.ManageApplication;

import java.util.List;

/**
* @author DSCBooK
* @description 针对表【MANAGE_APPLICATION(应用列表)】的数据库操作Service
* @createDate 2024-01-11 10:43:10
*/
public interface ManageApplicationService extends IService<ManageApplication> {

    List<ManageApplication> selectappID(Long appId , String appUserId );
    int contAll();
    List<ManageApplication>selectUserApp(String appUserId );
    boolean updateSecret(Long appId);
    boolean upadataAppList(Long appId,String appName,String appSynopsis,String appIconpath,String appUserId);
}
