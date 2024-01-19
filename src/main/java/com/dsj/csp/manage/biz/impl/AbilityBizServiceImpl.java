package com.dsj.csp.manage.biz.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
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

import java.security.KeyPair;
import java.util.HashMap;
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
public class AbilityBizServiceImpl implements AbilityBizService {

    private final AbilityService abilityService;
    private final AbilityApiService abilityApiService;


    public Boolean saveAbility(AbilityLoginVO abilityLoginVO) {
        // 1.插入能力基本信息
        AbilityEntity ability = new AbilityEntity();
        BeanUtils.copyProperties(abilityLoginVO, ability);
        abilityService.save(ability);
        Long abilityId = ability.getAbilityId();

        for (AbilityApiEntity api : abilityLoginVO.getApiList()){
            api.setAbilityId(abilityId);
            // 生成公钥和私钥
            Map<String,String> SM2Key = Sm2.sm2Test();
            api.setSecretKey(SM2Key.get("privateEncode"));
            api.setPublicKey(SM2Key.get("publicEncode"));
            abilityApiService.save(api);
        }
        return true;

//        List<AbilityApiEntity> abilityApiEntityList = abilityLoginVO.getApiList()
//                .stream().peek(abilityApi -> abilityApi.setAbilityId(abilityId)).toList();
//        return abilityApiService.saveBatch(abilityApiEntityList);
    }



}