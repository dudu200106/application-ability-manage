package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.biz.AbilityBizService;
import com.dsj.csp.manage.dto.AbilityDTO;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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
    private final AbilityApiApplyService abilityApiApplyService;

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
        List<Long> ids = Arrays.stream(abilityIds.split(",")).map(id -> Long.parseLong(id.trim())).toList();
        long countRelateApply = abilityApiApplyService.count(Wrappers.lambdaUpdate(AbilityApiApplyEntity.class).
                in(AbilityApiApplyEntity::getAbilityId, ids)
                .and(apply->apply.eq(AbilityApiApplyEntity::getIsDelete, 0)));
        if (countRelateApply>0){
            throw new BusinessException("删除能力失败:该能力存在接口还在被应用使用!");
        }
        abilityApiService.remove(Wrappers.lambdaUpdate(AbilityApiEntity.class).in(AbilityApiEntity::getAbilityId, ids));
        Boolean delFlag = abilityService.removeBatchByIds(ids);
        return delFlag;
    }


}
