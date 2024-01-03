package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.service.AbilityService;
import com.dsj.csp.manage.mapper.AbilityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Stars
* @description 针对表【MANAGE_ABILITY(能力表)】的数据库操作Service实现
* @createDate 2023-12-29 16:45:50
*/
@Service
public class AbilityServiceImpl extends ServiceImpl<AbilityMapper, AbilityEntity> implements AbilityService{
    @Autowired
    private AbilityMapper abilityMapper;
    @Override
    public void update(AbilityEntity ability) {
        abilityMapper.updateById(ability);
    }
}




