package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AppApiBand;
import com.dsj.csp.manage.mapper.AppApiBandMapper;
import com.dsj.csp.manage.service.AppApiBandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class AppApiBandServiceImpl extends ServiceImpl<AppApiBandMapper, AppApiBand> implements AppApiBandService {


}
