package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.mapper.AbilityMapper;
import com.dsj.csp.manage.service.AbilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityServiceImpl extends ServiceImpl<AbilityMapper, AbilityEntity> implements AbilityService  {

    @Override
    public long countAbility(Integer status) {
        QueryWrapper<AbilityEntity> qw = new QueryWrapper<>();
        qw.lambda().eq(AbilityEntity::getStatus, status);
        return this.getBaseMapper().selectCount(qw);
    }

    @Override
    public long countAvailAbility() {
        QueryWrapper<AbilityEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(AbilityEntity::getStatus, 3)
                .or()
                .eq(AbilityEntity::getStatus, 4);
        return this.getBaseMapper().selectCount(queryWrapper);
    }

}
