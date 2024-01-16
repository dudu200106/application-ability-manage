package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import org.springframework.stereotype.Service;

public interface AbilityApiService extends IService<AbilityApiEntity> {

    // 统计接口数量, 直接调用mybatis plus的提供的接口
    // long count();
}
