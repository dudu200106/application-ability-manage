package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.CspApplication;

import java.util.List;

/**
* @author DSCBooK
* @description 针对表【CSP_APPLICATION(应用列表)】的数据库操作Service
* @createDate 2024-01-09 15:34:55
*/
public interface CspApplicationService extends IService<CspApplication> {

    List<CspApplication> selectappID(Integer appId , String appUserId );
}
