package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.bean.BeanUtil;
import com.dsj.csp.manage.biz.AbilityBizService;
import com.dsj.csp.manage.dto.AbilityApplyDTO;
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

    @Override
    public AbilityApplyDTO getAbilityInfo(Long abilityId) {
//        AbilityEntity apply = abilityService.getById(abilityId);
//        AbilityApplyDTO resApply = new AbilityApplyDTO();
//        BeanUtil.copyProperties(apply, resApply);
//        String apiIds = abilityApplyService.getById(abilityId).getApiIds();
//        List<Long> idList = Arrays.asList(apiIds.split(",")).stream().map(e->Long.parseLong(e)).toList();
//        List<AbilityApiEntity> apis = abilityApiService.listByIds(idList);
//        resApply.setApiList(apis);
        return null;
    }
}
