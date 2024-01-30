package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.BusinessException;
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
        long cnt =abilityApplyService.count(Wrappers.lambdaQuery(AbilityApplyEntity.class)
                .eq(AbilityApplyEntity::getAppId, applyVO.getAppId())
                .eq(AbilityApplyEntity::getAbilityId,applyVO.getAbilityId())
                .eq(AbilityApplyEntity::getStatus, 0));
        if (cnt!=0){
            throw new BusinessException("应用已提交对该能力的申请,请耐心等待或撤销申请审核...");
        }
        AbilityApplyEntity applyEntity = new AbilityApplyEntity();
        BeanUtil.copyProperties(applyVO, applyEntity, true);
        // TODO 因实名认证页面还没做, 暂时取消一下记录操作, 直接存储绑定记录
        // 以下信息直接存入能力申请记录信息数据库, 方便查询
        // 从应用表中获取应用名称/用户ID; 从用户表中获取企业/政府名称; 从能力表获取能力名称/能力类型
        ManageApplicationEntity app = manageApplicationService.getById(applyVO.getAppId());
        UserApproveEntity userApproveEntity = userApproveService.getById(applyVO.getUserId());
        AbilityEntity ability = abilityService.getById(applyVO.getAbilityId());

        if (app==null || userApproveEntity==null || ability==null){
            throw new BusinessException("申请能力异常! 请确保相关的用户实名认证、应用、能力数据信息正常!");
        }
        applyEntity.setAppName(app.getAppName());
        applyEntity.setUserId(Long.parseLong(app.getAppUserId()));
        applyEntity.setCompanyName(userApproveEntity.getCompanyName());
        applyEntity.setGovName(userApproveEntity.getGovName());
        applyEntity.setAbilityName(ability.getAbilityName());
        applyEntity.setAbilityType(ability.getAbilityName());
        abilityApplyService.save(applyEntity);
    }


    @Override
    public AbilityApplyDTO getApplyInfo(Long abilityApplyId) {
        AbilityApplyEntity apply = abilityApplyService.getById(abilityApplyId);
        if (apply==null){
            throw new BusinessException("能力申请记录不存在!!请核实你的能力申请");
        }
        // 查询申请的api接口列表
        String apiIds = abilityApplyService.getById(abilityApplyId).getApiIds();
        List<Long> idList = Arrays.asList(apiIds.split(",")).stream().map(e->Long.parseLong(e)).toList();
        List<AbilityApiEntity> apis = abilityApiService.listByIds(idList);
        // 查询需要返回的申请的能力、用户、应用的其他信息
        AbilityEntity ability = abilityService.getById(apply.getAbilityId());
//        UserApproveEntity userApprove = userApproveService.getById(apply.getUserId());
//        ManageApplicationEntity app = manageApplicationService.getById(apply.getAppId());
        //构造返回能力申请信息DTO
        AbilityApplyDTO resApply = new AbilityApplyDTO();
        BeanUtil.copyProperties(apply, resApply,true);
        resApply.setAbilityDesc(ability==null ? null : ability.getAbilityDesc());
        resApply.setApiList(apis);
        return resApply;
    }

    public String auditApply(AbilityApplyAuditVO auditVO) {
        AbilityApplyEntity apply = abilityApplyService.getById(auditVO.getAbilityApplyId());
        if (apply==null){
            throw new BusinessException("审核失败! 请刷新页面后重试...");
        }
        // 审核流程限制: 状态(-1待提交 0待审核 1审核通过 2审核不通过 3已停用 )
        if ((auditVO.getFlag() == -1 && apply.getStatus() != 0)
                || (auditVO.getFlag() == 0 && apply.getStatus() != -1)
                || (auditVO.getFlag() == 1 && apply.getStatus() != 0)
                || (auditVO.getFlag() == 2 && apply.getStatus() != 0)
                || (auditVO.getFlag() == 3 && apply.getStatus() != 1)) {
            throw new BusinessException("审核失败! 请刷新页面后重试...");
        }
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
            throw new BusinessException("审核失败! 申请能力的appId不存在!");
        }
        String appSecretKey =  app.getAppSecret();
        String appAppKey =  app.getAppSecret();
        // 如果审核结果不通过 或者应用的公钥私钥有一个不为空, 就不用生成密钥了
        if (auditVO.getFlag() == 1
                && (appSecretKey==null || "".equals(appSecretKey))
                && (appAppKey==null || "".equals(appAppKey))){
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
        // 审核反馈信息
        String auditMsg = auditVO.getFlag()==-1 ? "审核撤回完毕!" :
                auditVO.getFlag()==0 ? "审核提交完毕, 等待审核..." :
                        auditVO.getFlag()==1 ? "审核通过完毕!" :
                                auditVO.getFlag()==2 ? "审核不通过完毕!." :
                                        "停用完毕!";
        return auditMsg;
    }

    @Override
    public Page pageApply(AbilityApplyQueryVO applyQueryVO) {
        // 分页获取基本申请信息, 后续会继续查询、插入必要的关联信息
        Page prePage = abilityApplyService.page(applyQueryVO.toPage(), applyQueryVO.getQueryWrapper());
        // 数据条数为空, 直接返回, 避免空指针
        if (prePage.getTotal()==0){
            return prePage;
        }
        List<AbilityApplyEntity> records = prePage.getRecords();
//        // 1.用户表 过userId查出企业/政府名称
//        Set<Long> userIds = records.stream().map(e->e.getUserId()).collect(Collectors.toSet());
//        List<UserApproveEntity> users = userApproveService.list(Wrappers.lambdaQuery(UserApproveEntity.class)
//                .select(UserApproveEntity::getUserId, UserApproveEntity::getCompanyName, UserApproveEntity::getGovName)
//                .in(UserApproveEntity::getUserId, userIds));
//        // 将ID映射到数据上, 方便查找使用
//        Map<String, UserApproveEntity> userMap = users.stream().collect(Collectors.toMap(user -> user.getUserId(), user -> user));
//        // 2.应用表 通过appId查出应用名称
//        Set<Long> appIds = records.stream().map(e  -> e.getAppId()).collect(Collectors.toSet());
//        List<ManageApplicationEntity> apps = manageApplicationService.list(Wrappers.lambdaQuery(ManageApplicationEntity.class)
//                .select(ManageApplicationEntity::getAppId, ManageApplicationEntity::getAppName)
//                .in(ManageApplicationEntity::getAppId, appIds));
//        Map<String, String> appMap = apps.stream().collect(Collectors.toMap(app -> app.getAppId(), app ->app.getAppName()));
        // 3.能力表 abilityId查出能力名称和类型
        Set<Long> abilityIds = records.stream().map(e  -> e.getAbilityId()).collect(Collectors.toSet());
        List<AbilityEntity> abilitys = abilityService.list( Wrappers.lambdaQuery(AbilityEntity.class)
                .select(AbilityEntity::getAbilityId, AbilityEntity::getAbilityDesc)
                .in(AbilityEntity::getAbilityId, abilityIds));
        Map<Long, AbilityEntity> abilityMap = abilitys.stream()
                .collect(Collectors.toMap(ability -> ability.getAbilityId(), ability -> ability));

        // 返回的分页res
        Page newPage = new Page<>(prePage.getCurrent(),  prePage.getSize(), prePage.getTotal());
        List<AbilityApplyDTO> resRecords = records.stream().map(apply ->{
            AbilityApplyDTO applyDTO = new AbilityApplyDTO();
            BeanUtil.copyProperties(apply, applyDTO, true);
            applyDTO.setAbilityDesc(abilityMap.get(apply.getAbilityId())==null ? null : abilityMap.get(apply.getAbilityId()).getAbilityDesc());
            return applyDTO;
        }).toList();
        newPage.setRecords(resRecords);
        return newPage;
    }
}
