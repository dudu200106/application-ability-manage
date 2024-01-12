package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.dto.AbilityListDTO;
import com.dsj.csp.manage.dto.AbilityLoginVO;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.mapper.AbilityApiMapper;
import com.dsj.csp.manage.mapper.AbilityApplyMapper;
import com.dsj.csp.manage.mapper.AbilityMapper;
import com.dsj.csp.manage.service.AbilityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Service
public class AbilityServiceImpl extends ServiceImpl<AbilityMapper, AbilityEntity>
        implements AbilityService  {

    @Autowired
    AbilityMapper abilityMapper;
    @Autowired
    AbilityApiMapper abilityApiMapper;
    @Autowired
    AbilityApplyMapper abilityApplyMapper;

    @Override
    public List<AbilityListDTO> getAllAbilityList() {
//        QueryWrapper<AbilityEntity> queryWrapper = new QueryWrapper<>();
//
//        // 指定要查询的字段列表
//        queryWrapper.select("ABILITY_NAME", "ABILITY_ID");
//
//        List<AbilityListDTO> list = new ArrayList<>();
//
//        List<AbilityEntity> abilitys = abilityMapper.selectList(queryWrapper);
//        for (AbilityEntity ability : abilitys){
//            AbilityListDTO abilityListDTO = new AbilityListDTO();
//            BeanUtils.copyProperties(ability, abilityListDTO);
//            QueryWrapper<AbilityApiEntity> apiQueryWrapper = new QueryWrapper<>();
//            apiQueryWrapper.eq("ABILITY_ID", ability.getAbilityId());
//              abilityApi = abilityApiMapper.selectById(apiQueryWrapper);
//        }
        return abilityMapper.getAbilityList();
    }

    @Override
    public void saveAbility(AbilityLoginVO abilityLoginVO) {
        // 1.插入能力基本信息
        AbilityEntity ability = new AbilityEntity();
        BeanUtils.copyProperties(abilityLoginVO, ability);
        abilityMapper.insert(ability);
        Long abilityId = ability.getAbilityId();

        // 2.插入接口
        for (AbilityApiEntity abilityApi : abilityLoginVO.getApiList()){
            abilityApi.setAbilityId(1L);
            abilityApiMapper.insert(abilityApi);
        }
    }

    @Override
    public void saveAbilityApply(AbilityApplyVO applyVO) {
        AbilityApplyEntity applyEntity = new AbilityApplyEntity();
        BeanUtils.copyProperties(applyVO, applyEntity);
        abilityApplyMapper.insert(applyEntity);

    }
}
