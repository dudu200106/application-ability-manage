package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AbilityApiApplyEntity;
import com.dsj.csp.manage.mapper.AbilityApiApplyMapper;
import com.dsj.csp.manage.service.AbilityApiApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class AbilityApiApplyServiceImpl extends ServiceImpl<AbilityApiApplyMapper, AbilityApiApplyEntity> implements AbilityApiApplyService  {
}
