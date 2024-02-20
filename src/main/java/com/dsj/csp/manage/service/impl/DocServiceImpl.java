package com.dsj.csp.manage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.dsj.csp.manage.entity.DocEntity;
import com.dsj.csp.manage.mapper.DocMapper;
import com.dsj.csp.manage.service.DocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-06
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DocServiceImpl extends ServiceImpl<DocMapper, DocEntity> implements DocService {
}
