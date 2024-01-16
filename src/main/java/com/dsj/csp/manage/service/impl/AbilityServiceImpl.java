package com.dsj.csp.manage.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.dto.AbilityListDTO;
import com.dsj.csp.manage.dto.AbilityLoginVO;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.entity.ManageApplication;
import com.dsj.csp.manage.mapper.AbilityApiMapper;
import com.dsj.csp.manage.mapper.AbilityApplyMapper;
import com.dsj.csp.manage.mapper.AbilityMapper;
import com.dsj.csp.manage.service.AbilityService;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.util.Sm4;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityServiceImpl extends ServiceImpl<AbilityMapper, AbilityEntity>
        implements AbilityService  {

    @Autowired
    AbilityMapper abilityMapper;
    @Autowired
    AbilityApiMapper abilityApiMapper;
    @Autowired
    AbilityApplyMapper abilityApplyMapper;
    @Autowired
    private ManageApplicationService manageApplicationService;


    @Override
    public List<AbilityListDTO> getAllAbilityList() {
        return abilityMapper.getAbilityList();
    }

    @Override
    public void saveAbility(AbilityLoginVO abilityLoginVO) {
        // 1.插入能力基本信息
        AbilityEntity ability = new AbilityEntity();
        BeanUtils.copyProperties(abilityLoginVO, ability);
        abilityMapper.insert(ability);
        Long abilityId = ability.getAbilityId();

//        this.saveBatch()

        // 2.插入接口
        for (AbilityApiEntity abilityApi : abilityLoginVO.getApiList()){
            abilityApi.setAbilityId(abilityId);
            abilityApiMapper.insert(abilityApi);
        }
    }


    @Override
    public void updateAbilityLogin(AbilityLoginVO abilityLogin) {

        AbilityEntity ability = new AbilityEntity();
        BeanUtils.copyProperties(abilityLogin, ability);
        Long abilityId = abilityLogin.getAbilityId();
        abilityMapper.updateById(ability);

        // 更新能力接口列表
        for(AbilityApiEntity api : abilityLogin.getApiList()){
            // 是否为新增的接口
            if (api.getApiId() == null || "".equals(api.getApiId())){
                api.setAbilityId(abilityId);
                abilityApiMapper.insert(api);
            }
            else {
                UpdateWrapper<AbilityApiEntity> apiUW = new UpdateWrapper<>();
                apiUW.eq("api_id", api.getApiId());
                abilityApiMapper.update(api, apiUW);
            }
        }
    }

    @Override
    public void auditApply(Long abilityApplyId, Long appId, Integer flag) {
        // 创建更新条件构造器
        UpdateWrapper<AbilityApplyEntity> updateWrapper = new UpdateWrapper<>();
        // 设置更新条件，这里假设要更新 id 为 1 的记录
        updateWrapper.eq("ability_apply_id", abilityApplyId);
        // 设置要更新的字段和值
        updateWrapper.set("STATUS", flag);
        updateWrapper.set("UPDATE_TIME", DateTime.now());
        abilityApplyMapper.update(updateWrapper);

        // 判断是否生成APP Key 和 Secret Key
        String appSecretKey =  manageApplicationService.getById(appId).getAppSecret();
        String appAppKey =  manageApplicationService.getById(appId).getAppSecret();
        if (flag==1
                && (appSecretKey==null || "".equals(appSecretKey))
                && (appAppKey==null || "".equals(appAppKey))){
            String appKey = Sm4.sm();
            String secretKey = Sm4.sm();
            UpdateWrapper<ManageApplication> appUpdateWrapper = new UpdateWrapper<>();
            // 设置更新条件，这里假设要更新 id 为 1 的记录
            appUpdateWrapper.eq("APP_ID", appId);
            // 设置要更新的字段和值
            appUpdateWrapper.set("APP_SECRET", secretKey);
            appUpdateWrapper.set("APP_KEY", appKey);
            manageApplicationService.update(appUpdateWrapper);
        }
    }

    @Override
    public long countAbility(Integer status) {
        QueryWrapper<AbilityEntity> qw = new QueryWrapper<>();
        qw.eq("STATUS", status);
        return abilityMapper.selectCount(qw);
    }

    @Override
    public long countAvailAbility() {
        QueryWrapper<AbilityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Status", 3)
                .or()
                .eq("status", 4);
        return abilityMapper.selectCount(queryWrapper);
    }

}
