package com.dsj.csp.manage.biz.impl;

import com.dsj.csp.manage.biz.AbilityBizService;
import com.dsj.csp.manage.dto.AbilityLoginVO;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.service.AbilityApiService;
import com.dsj.csp.manage.service.AbilityService;
import com.dsj.csp.manage.util.Sm2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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


    public Boolean saveAbility(AbilityLoginVO abilityLoginVO) {
        // 1.插入能力基本信息
        AbilityEntity ability = new AbilityEntity();
        BeanUtils.copyProperties(abilityLoginVO, ability);
        abilityService.save(ability);
        Long abilityId = ability.getAbilityId();

//        abilityLoginVO.getApiList().forEach(e->{
//            e.setAbilityId(abilityId);
//            Map<String,String> SM2Key = Sm2.sm2Test();
//            e.setSecretKey(SM2Key.get("privateEncode"));
//            e.setPublicKey(SM2Key.get("publicEncode"));
//            abilityApiService.save(e);
//        });
//        return true;

        List<AbilityApiEntity> abilityApiEntityList = abilityLoginVO.getApiList()
                .stream()
                .peek((abilityApi) -> {
                    abilityApi.setAbilityId(abilityId);
                    Map<String,String> SM2Key = Sm2.sm2Test();
                    abilityApi.setSecretKey(SM2Key.get("privateEncode"));
                    abilityApi.setPublicKey(SM2Key.get("publicEncode"));
                }).toList();
        return abilityApiService.saveBatch(abilityApiEntityList);

    }



}
