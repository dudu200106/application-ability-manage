package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.biz.AbilityApplyBizService;
import com.dsj.csp.manage.dto.AbilityApplyAuditVO;
import com.dsj.csp.manage.dto.AbilityApplyDTO;
import com.dsj.csp.manage.dto.AbilityApplyQueryVO;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.entity.*;
import com.dsj.csp.manage.service.*;
import com.dsj.csp.manage.util.Sm2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityApplyBizServiceImpl implements AbilityApplyBizService {

    private final ManageApplicationService manageApplicationService;
    private final UserApproveService userApproveService;
    private final AbilityService abilityService;
    private final AbilityApplyService abilityApplyService;
    private final AbilityApiService abilityApiService;

    @Override
    public void saveAbilityApply(AbilityApplyVO applyVO) {
        AbilityApplyEntity applyEntity = new AbilityApplyEntity();
        BeanUtil.copyProperties(applyVO, applyEntity, true);
        // 1.获取应用名称/用户ID
        ManageApplicationEntity app = manageApplicationService.getById(applyVO.getAppId());
        applyEntity.setAppName(app.getAppName());
        applyEntity.setUserId(Long.parseLong(app.getAppUserId()));

        // 2.通过用户ID获取企业/政府名称
        UserApproveEntity userApproveEntity = userApproveService.getById(app.getAppUserId());
        applyEntity.setCompanyName(userApproveEntity.getCompanyName());
        applyEntity.setGovName(userApproveEntity.getGovName());

        // 3.通过能力ID获取能力名称/能力类型
        AbilityEntity ability = abilityService.getById(applyVO.getAbilityId());
        applyEntity.setAbilityName(ability.getAbilityName());
        applyEntity.setAbilityType(ability.getAbilityName());
        abilityApplyService.save(applyEntity);
    }


    @Override
    public AbilityApplyDTO getApplyInfo(Long abilityApplyId) {
        AbilityApplyEntity apply = abilityApplyService.getById(abilityApplyId);
        String apiIds = abilityApplyService.getById(abilityApplyId).getApiIds();
        List<Long> idList = Arrays.asList(apiIds.split(",")).stream().map(e->Long.parseLong(e)).toList();
        List<AbilityApiEntity> apis = abilityApiService.listByIds(idList);
        AbilityEntity ability = abilityService.getById(apply.getAbilityId());
        //构造返回能力申请信息DTO
        AbilityApplyDTO resApply = new AbilityApplyDTO();
        BeanUtil.copyProperties(apply, resApply);
        BeanUtil.copyProperties(ability, resApply);
        resApply.setApiList(apis);
        return resApply;
    }

    public void auditApply(AbilityApplyAuditVO auditVO) {
        // 创建更新条件构造器
        LambdaUpdateWrapper<AbilityApplyEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(AbilityApplyEntity::getAbilityApplyId, auditVO.getAbilityApplyId());
        updateWrapper.set(AbilityApplyEntity::getStatus, auditVO.getFlag());
        updateWrapper.set(AbilityApplyEntity::getNote, auditVO.getNote());
        updateWrapper.set(AbilityApplyEntity::getUpdateTime, DateTime.now());
        updateWrapper.set(AbilityApplyEntity::getApproveTime, DateTime.now());
        abilityApplyService.update(updateWrapper);

        // 判断是否要生成一对密钥
        Long appId = abilityApplyService.getById(auditVO.getAbilityApplyId()).getAppId();
        ManageApplicationEntity app = manageApplicationService.getById(appId);
        // 如果申请的appId不存在
        if (app == null){
            return;
        }
        String appSecretKey =  app.getAppSecret();
        String appAppKey =  app.getAppSecret();
        // 如果审核结果不通过 或者应用的公钥私钥有一个不为空, 就不用生成密钥了
        if (auditVO.getFlag() != 1
                || (appSecretKey!=null && !"".equals(appSecretKey))
                || (appAppKey!=null && !"".equals(appAppKey))){
            return;
        }
        Map<String, String> sm2Map = Sm2.sm2Test();
        String appKey = sm2Map.get("publicEncode");
        String secretKey = sm2Map.get("privateEncode");
        Map<String, String> sm2Map2 = Sm2.sm2Test();
        String wgKey = sm2Map2.get("publicEncode");
        String wgSecre = sm2Map2.get("privateEncode");
        LambdaUpdateWrapper<ManageApplicationEntity> appUpdateWrapper
                = Wrappers.lambdaUpdate(ManageApplicationEntity.class)
                .eq(ManageApplicationEntity::getAppId, appId)
                .set(ManageApplicationEntity::getAppKey, appKey)
                .set(ManageApplicationEntity::getAppSecret, secretKey)
                .set(ManageApplicationEntity::getAppWgKey, wgKey)
                .set(ManageApplicationEntity::getAppWgSecret, wgSecre);
        manageApplicationService.update(appUpdateWrapper);
    }

    @Override
    public Page pageApply(AbilityApplyQueryVO applyQueryVO) {

        Page prePage = abilityApplyService.page(applyQueryVO.toPage(), applyQueryVO.getQueryWrapper());
        List<AbilityApplyEntity> records = prePage.getRecords();

        // 1.用户表 过userId查出企业/政府名称
        Set<Long> userIds = records.stream().map(e->e.getUserId()).collect(Collectors.toSet());
        List<UserApproveEntity> users = userApproveService.list(
                Wrappers.lambdaQuery(UserApproveEntity.class)
                        .select(UserApproveEntity::getUserId, UserApproveEntity::getCompanyName, UserApproveEntity::getGovName)
                        .in(UserApproveEntity::getUserId, userIds)
        );
        // 将ID映射到数据上, 方便查找使用
        Map<String, UserApproveEntity> userMap = users.stream().collect(Collectors.toMap(user -> user.getUserId(), user -> user));
        // 2.应用表 通过appId查出应用名称
        Set<Long> appIds = records.stream().map(e  -> e.getAppId()).collect(Collectors.toSet());
        List<ManageApplicationEntity> apps = manageApplicationService.list(
                Wrappers.lambdaQuery(ManageApplicationEntity.class)
                        .select(ManageApplicationEntity::getAppId, ManageApplicationEntity::getAppName)
                        .in(ManageApplicationEntity::getAppId, appIds)
        );
        Map<String, String> appMap = apps.stream().collect(Collectors.toMap(app -> app.getAppId(), app ->app.getAppName()));
        // 3.能力表 abilityId查出能力名称和类型
        Set<Long> abilityIds = records.stream().map(e  -> e.getAbilityId()).collect(Collectors.toSet());
        LambdaQueryWrapper abilityQW = Wrappers.lambdaQuery(AbilityEntity.class)
                .select(AbilityEntity::getAbilityId, AbilityEntity::getAbilityName, AbilityEntity::getAbilityType)
                .in(AbilityEntity::getAbilityId, abilityIds);
        List<AbilityEntity> abilitys = abilityService.list(abilityQW);
        Map<Long, AbilityEntity> abilityMap = abilitys.stream().collect(Collectors.toMap(ability -> ability.getAbilityId(), ability -> ability));

        // 返回的分页res
        Page newPage = new Page<>(prePage.getCurrent(),  prePage.getSize(), prePage.getTotal());
        List<AbilityApplyDTO> resRecords = records.stream().map(apply ->{
            AbilityApplyDTO applyDTO = new AbilityApplyDTO();
            BeanUtil.copyProperties(apply, applyDTO, true);
            applyDTO.setAbilityName(abilityMap.get(apply.getAbilityId()).getAbilityName());
            applyDTO.setAbilityType(abilityMap.get(apply.getAbilityId()).getAbilityType());
            applyDTO.setAppName(appMap.get(apply.getAppId() + ""));
            applyDTO.setCompanyName(userMap.get(apply.getUserId() + "").getCompanyName());
            applyDTO.setGovName(userMap.get(apply.getUserId() + "").getGovName());
            return applyDTO;
        }).toList();
        newPage.setRecords(resRecords);
        return newPage;
    }
}
