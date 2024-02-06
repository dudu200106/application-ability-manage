package com.dsj.csp.manage.service.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.dto.AbilityAuditVO;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.mapper.AbilityApiMapper;
import com.dsj.csp.manage.service.AbilityApiService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Transactional(propagation = Propagation.REQUIRED)
@Service
@RequiredArgsConstructor
public class AbilityApiServiceImpl extends ServiceImpl<AbilityApiMapper, AbilityApiEntity> implements AbilityApiService  {




    @Override
    public List<Long> getApiIds(String keyword) {
        List<AbilityApiEntity> apis = this.list(Wrappers.lambdaQuery(AbilityApiEntity.class)
                .and(ObjectUtil.isEmpty(keyword), i->i.like(AbilityApiEntity::getApiName, keyword)
                        .or().like(AbilityApiEntity::getApiDesc, keyword)
                        .or().like(AbilityApiEntity::getApiUrl, keyword))
        );
        List<Long> ids = apis.stream().map(e->e.getApiId()).toList();
        return ids;
    }
}
