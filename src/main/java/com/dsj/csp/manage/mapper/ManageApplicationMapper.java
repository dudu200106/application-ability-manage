package com.dsj.csp.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.entity.ManageApplication;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.Date;
import java.util.List;

/**
 * @author DSCBooK
 * @description 针对表【MANAGE_APPLICATION(应用列表)】的数据库操作Mapper
 * @createDate 2024-01-11 10:43:10
 * @Entity generator.entity.ManageApplication
 */
public interface ManageApplicationMapper extends MPJBaseMapper<ManageApplication> {

    List<ManageApplication> selectappID(Long appId, String appUserId);

    int contAll();

    List<ManageApplication> selectUserApp(String appUserId);

    boolean updateSecret(Long appId, String appKey, String appSecret);

    boolean upadataAppList(Long appId, String appName, String appSynopsis, String appIconpath, String appUserId);

    boolean updateIsdetele(Long appId, String appUserId);


//    List<ManageApplication> selectPages(LambdaQueryWrapper<ManageApplication> wrapper,String appUserId, String keyword, Date startTime, Date endTime, int page, int size);

//    Page<ManageApplication> selectPages(Page page,String appUserId, String keyword, Date startTime, Date endTime);

//    Page<ManageApplication> selectPages(String appUserId, String keyword, Date startTime, Date endTime, int page, int size);
//
//    IPage<ManageApplication> selectJoinPage(Page<Object> objectPage, Class<ManageApplication> manageApplicationClass, MPJLambdaWrapper<Object> eq);
}




