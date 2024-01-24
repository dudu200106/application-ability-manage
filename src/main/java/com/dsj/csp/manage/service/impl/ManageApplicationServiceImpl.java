package com.dsj.csp.manage.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.mapper.ManageApplicationMapper;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.util.Sm4;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author DSCBooK
 * @description 针对表【MANAGE_APPLICATION(应用列表)】的数据库操作Service实现
 * @createDate 2024-01-11 10:43:10
 */
@Service
public class ManageApplicationServiceImpl extends ServiceImpl<ManageApplicationMapper, ManageApplicationEntity>
        implements ManageApplicationService {
//    @Autowired
//    ManageApplicationMapper manageApplicationMapper;

    @Override
    public List<ManageApplicationEntity> selectappID(Long appId, String appUserId) {
        return baseMapper.selectappID(appId, appUserId);
    }

    /**
     *
     * @author Du Shun Chang
     * @date 2024/1/23 14:51
     * @return 统计应用总数
     */
    @Override
    public int contAll() {
        return baseMapper.contAll();
    }

//    @Override
//    public List<ManageApplicationEntity> selectUserApp(String appUserId) {
//        return baseMapper.selectUserApp(appUserId);
//    }


//    @Override
//    public boolean updateSecret(Long appId) {
//        String appKey = Sm4.sm();
//        String appSecret = Sm4.sm();
//        return baseMapper.updateSecret(appId, appKey, appSecret);
//    }
    /**
     *
     * @author Du Shun Chang
     * @date 2024/1/23 14:51
     * @return 更新应用
     */

    @Override
    public boolean upadataAppList(Long appId, String appName, String appSynopsis, String appIconpath, String appUserId) {
        return baseMapper.upadataAppList(appId, appName, appSynopsis, appIconpath, appUserId);
    }
/**
 *
 * @author Du Shun Chang
 * @date 2024/1/23 14:52
 * @param appId
 * @param appUserId
 * @return 逻辑删除
 */
    @Override
    public boolean updateIsdetele(String appId, String appUserId) {
        return baseMapper.updateIsdetele(appId, appUserId);
    }




}




