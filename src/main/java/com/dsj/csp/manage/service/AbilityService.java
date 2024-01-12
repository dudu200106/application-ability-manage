package com.dsj.csp.manage.service;

import com.dsj.csp.manage.entity.AbilityEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Stars
* @description 针对表【MANAGE_ABILITY(能力表)】的数据库操作Service
* @createDate 2023-12-29 16:45:50
*/
public interface AbilityService extends IService<AbilityEntity> {

    public void update(AbilityEntity ability);
}
