package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.dsj.csp.manage.entity.CspApplication;
import com.dsj.csp.manage.mapper.CspApplicationMapper;
import com.dsj.csp.manage.service.CspApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author DSCBooK
* @description 针对表【CSP_APPLICATION(应用列表)】的数据库操作Service实现
* @createDate 2024-01-09 15:34:55
*/
@Service
public class CspApplicationServiceImpl extends ServiceImpl<CspApplicationMapper, CspApplication>
    implements CspApplicationService {

    @Autowired
    CspApplicationMapper cspApplicationMapper;
    @Override
    public List<CspApplication> selectappID(Integer appId, String appUserId) {
        return cspApplicationMapper.selectappID(appId,appUserId);
    }
}




