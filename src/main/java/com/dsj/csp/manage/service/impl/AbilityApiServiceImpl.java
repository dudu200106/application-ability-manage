package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.mapper.AbilityApiMapper;
import com.dsj.csp.manage.service.AbilityApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional(propagation = Propagation.REQUIRED)
@Service
public class AbilityApiServiceImpl extends ServiceImpl<AbilityApiMapper, AbilityApiEntity> implements AbilityApiService  {
}
