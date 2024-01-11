package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.CspApplication;
import com.dsj.csp.manage.entity.ManageApplication;
import com.dsj.csp.manage.mapper.CspApplicationMapper;
import com.dsj.csp.manage.mapper.ManageApplicationMapper;
import com.dsj.csp.manage.service.ManageApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author DSCBooK
* @description 针对表【MANAGE_APPLICATION(应用列表)】的数据库操作Service实现
* @createDate 2024-01-11 10:43:10
*/
@Service
public class ManageApplicationServiceImpl extends ServiceImpl<ManageApplicationMapper, ManageApplication>
    implements ManageApplicationService {
    @Autowired
    ManageApplicationMapper manageApplicationMapper;
    @Override
    public List<ManageApplication> selectappID(Long appId, String appUserId) {
        return manageApplicationMapper.selectappID(appId,appUserId);
    }

    @Override
    public int contAll() {
        return manageApplicationMapper.contAll();
    }
}




