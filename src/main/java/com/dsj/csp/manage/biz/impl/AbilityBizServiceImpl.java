package com.dsj.csp.manage.biz.impl;

import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.biz.AbilityApiBizService;
import com.dsj.csp.manage.biz.AbilityBizService;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
@Slf4j
public class AbilityBizServiceImpl implements AbilityBizService {

    private final AbilityService abilityService;
    private final AbilityApiService abilityApiService;
    private final AbilityApiBizService abilityApiBizService;

    @Override
    public boolean removeAbilityBatch(List<AbilityEntity> abilityList) {
        List<Long> abilityIds = abilityList.stream().map(AbilityEntity::getAbilityId).toList();
        List<AbilityApiEntity> apiList = abilityApiService.lambdaQuery()
                .in(AbilityApiEntity::getAbilityId, abilityIds)
                .select(AbilityApiEntity::getApiId)
                .list();
        try{
            abilityApiBizService.deleteApiBatch(apiList);
        }catch (BusinessException e){
            log.info(e.getMessage());
            throw new BusinessException("批量删除能力失败, 能力下的接口还有应用在使用!");
        }
        return abilityService.removeBatchByIds(abilityList);
    }

    @Override
    public boolean removeAbility(AbilityEntity ability) {
        List<AbilityApiEntity> apiList = abilityApiService.lambdaQuery()
                .eq(AbilityApiEntity::getAbilityId, ability.getAbilityId())
                .select(AbilityApiEntity::getApiId)
                .list();
        // 删除该能力及其下面的所有接口
        try{
            abilityApiBizService.deleteApiBatch(apiList);
        }catch (BusinessException e){
            log.info(e.getMessage());
            throw new BusinessException("删除能力失败, 能力下的接口还有应用在使用!");
        }
        return abilityService.removeById(ability);
    }


}
