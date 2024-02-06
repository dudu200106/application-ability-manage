package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.DocCatalogEntity;
import com.dsj.csp.manage.mapper.DocCatalogMapper;
import com.dsj.csp.manage.service.DocCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-06
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class DocCatalogServiceImpl extends ServiceImpl<DocCatalogMapper, DocCatalogEntity> implements DocCatalogService {
}
