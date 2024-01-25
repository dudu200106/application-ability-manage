package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dsj.csp.manage.biz.AbilityBizService;
import com.dsj.csp.manage.dto.AbilityApplyDTO;
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
}
