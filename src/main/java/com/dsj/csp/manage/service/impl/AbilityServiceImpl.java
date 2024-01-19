package com.dsj.csp.manage.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.dto.AbilityLoginVO;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.mapper.AbilityApiMapper;
import com.dsj.csp.manage.mapper.AbilityApplyMapper;
import com.dsj.csp.manage.mapper.AbilityMapper;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityService;
import com.dsj.csp.manage.service.ManageApplicationService;
import com.dsj.csp.manage.util.Sm4;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityServiceImpl extends ServiceImpl<AbilityMapper, AbilityEntity>
        implements AbilityService  {

    @Autowired
    AbilityMapper abilityMapper;
    @Autowired
    AbilityApiMapper abilityApiMapper;
    @Autowired
    AbilityApiService abilityApiService;
    @Autowired
    AbilityApplyMapper abilityApplyMapper;
    @Autowired
    private ManageApplicationService manageApplicationService;


    @Override
    public void saveAbility(AbilityLoginVO abilityLoginVO) {
        // 1.插入能力基本信息
        AbilityEntity ability = new AbilityEntity();
        BeanUtils.copyProperties(abilityLoginVO, ability);
        abilityMapper.insert(ability);
        Long abilityId = ability.getAbilityId();

        // 2.插入接口
        List<AbilityApiEntity> list =
                abilityLoginVO.getApiList().stream().map(item -> {
            item.setAbilityId(abilityId);
            return item;
        }).collect(Collectors.toList());
        abilityApiService.saveBatch((Collection<AbilityApiEntity>) list);

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
                apiUW.lambda().eq(AbilityApiEntity::getApiId, api.getApiId());
                abilityApiMapper.update(api, apiUW);
            }
        }
    }


    @Override
    public long countAbility(Integer status) {
        QueryWrapper<AbilityEntity> qw = new QueryWrapper<>();
        qw.lambda().eq(AbilityEntity::getStatus, status);
        return abilityMapper.selectCount(qw);
    }

    @Override
    public long countAvailAbility() {
        QueryWrapper<AbilityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(AbilityEntity::getStatus, 3)
                .or()
                .eq(AbilityEntity::getStatus, 4);
        return abilityMapper.selectCount(queryWrapper);
    }

}
