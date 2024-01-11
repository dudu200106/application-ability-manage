package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.ManageApplication;
import com.dsj.csp.manage.mapper.ManageApplicationMapper;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.util.Sm4;
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

    @Override
    public List<ManageApplication> selectUserApp(String userId) {
        return manageApplicationMapper.selectUserApp(userId);
    }


    @Override
    public boolean updateSecret(Long appId) {
        String appKey= Sm4.sm();
        String appSecret =Sm4.sm();
        return manageApplicationMapper.updateSecret(appId,appKey,appSecret);
    }

    @Override
    public boolean upadataAppList(Long appId, String appName, String appSynopsis, String appIconpath) {
        return manageApplicationMapper.upadataAppList(appId,appName,appSynopsis,appIconpath);
    }


}




