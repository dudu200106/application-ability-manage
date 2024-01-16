package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.dto.AbilityApplyVO;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.mapper.AbilityApplyMapper;
import com.dsj.csp.manage.service.AbilityApplyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityApplyServiceImpl extends ServiceImpl<AbilityApplyMapper, AbilityApplyEntity> implements AbilityApplyService {

    @Autowired
    AbilityApplyMapper abilityApplyMapper;

    @Override
    public void saveAbilityApply(AbilityApplyVO applyVO) {
        AbilityApplyEntity applyEntity = new AbilityApplyEntity();
        BeanUtils.copyProperties(applyVO, applyEntity);
        abilityApplyMapper.insert(applyEntity);

    }

}
