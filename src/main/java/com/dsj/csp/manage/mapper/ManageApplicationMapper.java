package com.dsj.csp.manage.mapper;

import com.dsj.csp.manage.entity.CspApplication;
import com.dsj.csp.manage.entity.ManageApplication;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author DSCBooK
* @description 针对表【MANAGE_APPLICATION(应用列表)】的数据库操作Mapper
* @createDate 2024-01-11 10:43:10
* @Entity generator.entity.ManageApplication
*/
public interface ManageApplicationMapper extends BaseMapper<ManageApplication> {

    List<ManageApplication> selectappID(Long appId , String appUserId );
    int contAll();
}




