package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.dsj.csp.manage.entity.DocCatalogEntity;
import com.dsj.csp.manage.mapper.DocCatalogMapper;
import com.dsj.csp.manage.service.DocCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-06
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class DocCatalogServiceImpl extends ServiceImpl<DocCatalogMapper, DocCatalogEntity> implements DocCatalogService {
    @Override
    public List<Long> matchCatalogIdList(String keyword) {
        return this.list(Wrappers.lambdaQuery(DocCatalogEntity.class)
                        .select(DocCatalogEntity::getCatalogId)
                        .like(DocCatalogEntity::getCatalogName, keyword))
                .stream().map(DocCatalogEntity::getCatalogId).toList();
    }
}
