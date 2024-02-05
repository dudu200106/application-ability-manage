package com.dsj.csp.manage.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.common.enums.CodeEnum;
import com.dsj.csp.common.exception.FlowException;
import com.dsj.csp.manage.dto.ManageApplictionVo;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.dsj.csp.manage.mapper.ManageApplicationMapper;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.util.Sm2;
import com.dsj.csp.manage.util.TimeTolong;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author DSCBooK
 * @description 针对表【MANAGE_APPLICATION(应用列表)】的数据库操作Service实现
 * @createDate 2024-01-11 10:43:10
 */
@Service
@RequiredArgsConstructor
//@RequiredArgsConstructor
public class ManageApplicationServiceImpl extends ServiceImpl<ManageApplicationMapper, ManageApplicationEntity>
        implements ManageApplicationService {
    @Autowired
    private ManageApplicationMapper manageApplicationMapper;
    @Autowired
    AbilityApiApplyService abilityApiApplyService;


    @Override
    public Long countAppUser(String appUserId) {
        LambdaUpdateWrapper<ManageApplicationEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppUserId, appUserId);
        return baseMapper.selectCount(lambdaUpdateWrapper);
    }

    @Override

    public int saveApp(ManageApplicationEntity manageApplication) {
        LambdaQueryWrapper<ManageApplicationEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ManageApplicationEntity::getAppIsdelete, 0);
        queryWrapper.eq(ManageApplicationEntity::getAppName, manageApplication.getAppName());
        queryWrapper.eq(ManageApplicationEntity::getAppUserId, manageApplication.getAppUserId());
//        if (manageApplication.getAppId() != null) {
//            queryWrapper.ne(ManageApplicationEntity::getAppName, manageApplication.getAppName());
//          )
//        }
        long count = baseMapper.selectCount(queryWrapper);
        /*对用户名是否重复进行判断，同理其他的也可以这样判断*/
        if (count > 0) {
            throw new FlowException(CodeEnum.APPNAME);
        } else {
            manageApplication.setAppCreatetime(new Date());
            manageApplication.setAppUpdatetime(new Date());
            manageApplication.setAppIsdelete(0);
            return baseMapper.insert(manageApplication);
        }

    }


    @Override
    public int deleteApp(ManageApplicationEntity manageApplicationEntity) {
        LambdaUpdateWrapper<ManageApplicationEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppId, manageApplicationEntity.getAppId());
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppUserId, manageApplicationEntity.getAppUserId());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppUpdatetime, new Date());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppUpdatename, manageApplicationEntity.getAppUserId());
        abilityApiApplyService.deleteApiApplyByAppId(Long.valueOf(manageApplicationEntity.getAppId()));
        return baseMapper.delete(lambdaUpdateWrapper);
    }

    @Override
    public List selectappID(String appId, String appUserId) {
        MPJLambdaWrapper<ManageApplicationEntity> wrapper = new MPJLambdaWrapper<ManageApplicationEntity>()
                .selectAll(ManageApplicationEntity.class)//查询user表全部字段
                .selectAll(UserApproveEntity.class)
                .leftJoin(UserApproveEntity.class, UserApproveEntity::getUserId, ManageApplicationEntity::getAppUserId)
                .eq(ManageApplicationEntity::getAppId, appId)
                .eq(ManageApplicationEntity::getAppUserId, appUserId);
        return baseMapper.selectJoinList(ManageApplictionVo.class, wrapper);
    }

    @Override
    public int upadataAppInfo(ManageApplicationEntity manageApplicationEntity) {
        LambdaUpdateWrapper<ManageApplicationEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppId, manageApplicationEntity.getAppId());
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppUserId, manageApplicationEntity.getAppUserId());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppUpdatetime, new Date());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppUpdatename, manageApplicationEntity.getAppUserId());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppIsdelete, 0);
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppName, manageApplicationEntity.getAppName());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppSynopsis, manageApplicationEntity.getAppSynopsis());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppUserId, manageApplicationEntity.getAppUserId());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppIconpath, manageApplicationEntity.getAppIconpath());
        return baseMapper.update(lambdaUpdateWrapper);

    }

    @Override
    public int upadataAppKey(ManageApplicationEntity manageApplicationEntity) {
        LambdaUpdateWrapper<ManageApplicationEntity> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppId, manageApplicationEntity.getAppId());
        lambdaUpdateWrapper.eq(ManageApplicationEntity::getAppUserId, manageApplicationEntity.getAppUserId());
        Map<String, String> sm2Map = Sm2.sm2Test();
//        应用key
        String appKey = sm2Map.get("publicEncode");
        String secretKey = sm2Map.get("privateEncode");
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppKey, appKey);
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppSecret, secretKey);
//网关key
        Map<String, String> sm2Map2 = Sm2.sm2Test();
        String wgKey = sm2Map2.get("publicEncode");
        String wgSecre = sm2Map2.get("privateEncode");
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppWgKey, wgKey);
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppWgSecret, wgSecre);
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppUpdatetime, new Date());
        lambdaUpdateWrapper.set(ManageApplicationEntity::getAppUpdatename, manageApplicationEntity.getAppUserId());
        return baseMapper.update(lambdaUpdateWrapper);
    }

    @Override
    public Page<ManageApplictionVo> selectPage(String appUserId, String keyword, Date startTime, Date endTime, int pages, int size) {
        Page<ManageApplictionVo> userApproveEntityPage = manageApplicationMapper.selectJoinPage(new Page<>(pages, size), ManageApplictionVo.class,
                new MPJLambdaWrapper<ManageApplicationEntity>()
                        .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), ManageApplicationEntity::getAppCreatetime, startTime, endTime)
                        .eq(!StringUtils.isEmpty(appUserId), ManageApplicationEntity::getAppUserId, appUserId)
                        .like(!StringUtils.isEmpty(keyword), ManageApplicationEntity::getAppName, keyword)//应用名称
                        .or().like(!StringUtils.isEmpty(keyword), UserApproveEntity::getUserId, keyword)//用户id
                        .or().like(!StringUtils.isEmpty(keyword), UserApproveEntity::getCompanyName, keyword)//企业名称
                        .or().like(!StringUtils.isEmpty(keyword), UserApproveEntity::getGovName, keyword)//  政府部门名称
                        .or().like(!StringUtils.isEmpty(keyword), ManageApplicationEntity::getAppId, keyword)//AppID
                        .or().like(!StringUtils.isEmpty(keyword), UserApproveEntity::getUserName, keyword)//用户Name
                        .selectAll(UserApproveEntity.class)
                        .selectAll(ManageApplicationEntity.class)
                        .leftJoin(UserApproveEntity.class, UserApproveEntity::getUserId, ManageApplicationEntity::getAppUserId)
                        .orderByDesc(ManageApplicationEntity::getAppCreatetime)
        );
        userApproveEntityPage.getRecords().forEach(data -> {
//            long time = data.getAppCreatetime().getTime();
//            data.setApptime(time);
            data.setApptime(TimeTolong.timetolong(data.getAppCreatetime()));
        });
        return userApproveEntityPage;
    }


}
