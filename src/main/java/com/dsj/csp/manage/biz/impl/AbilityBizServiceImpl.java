package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.biz.AbilityBizService;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.dto.AbilityDTO;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityApplyService;
import com.dsj.csp.manage.service.AbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 功能说明：
 *
 * @author 蔡云
 * 2024/1/17
 */
@RequiredArgsConstructor
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityBizServiceImpl implements AbilityBizService {

    private final AbilityService abilityService;
    private final AbilityApiService abilityApiService;
    private final AbilityApplyService abilityApplyService;

    @Override
    public AbilityDTO getAbilityInfo(Long abilityId) {
        AbilityEntity ability = abilityService.getById(abilityId);
        AbilityDTO abilityDTO = new AbilityDTO();
        BeanUtil.copyProperties(ability, abilityDTO, true);
        List<AbilityApiEntity> apis = abilityApiService.list(
                Wrappers.lambdaQuery(AbilityApiEntity.class).eq(AbilityApiEntity::getAbilityId, abilityId)
        );
        abilityDTO.setApiList(apis);
        return abilityDTO;
    }

    @Override
    public Boolean removeAbilityByIds(String abilityIds) {
        List<Long> ids = Arrays.asList(abilityIds.split(",")).stream().map(id -> Long.parseLong(id.trim())).toList();
        long countRelateApply = abilityApplyService.count(Wrappers.lambdaUpdate(AbilityApplyEntity.class).
                in(AbilityApplyEntity::getAbilityId, ids)
                .and(apply->apply.eq(AbilityApplyEntity::getIsDelete, 0)));
        if (countRelateApply>0){
            throw new BusinessException("删除能力失败:能力还在被应用使用!");
        }
        Boolean delFlag = abilityService.removeBatchByIds(ids);
        Boolean apiDelFlag = abilityApiService.remove(Wrappers.lambdaUpdate(AbilityApiEntity.class).in(AbilityApiEntity::getAbilityId, ids));
        return delFlag && apiDelFlag;
    }




}
