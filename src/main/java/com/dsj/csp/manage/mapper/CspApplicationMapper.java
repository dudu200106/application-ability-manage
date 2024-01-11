package com.dsj.csp.manage.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dsj.csp.manage.entity.CspApplication;

import java.util.List;

/**
* @author DSCBooK
* @description 针对表【CSP_APPLICATION(应用列表)】的数据库操作Mapper
* @createDate 2024-01-09 15:34:55
* @Entity generator.entity.CspApplication
*/
public interface CspApplicationMapper extends BaseMapper<CspApplication> {
    List<CspApplication> selectappID(Integer appId , String appUserId );
}




