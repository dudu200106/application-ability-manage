package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.entity.DocCatalogEntity;

import java.util.List;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-06
 */
public interface DocCatalogService extends IService<DocCatalogEntity> {

    /**
     * 根据关键字匹配查询目录id列表
     * @param keyword
     * @return
     */
    List<Long> matchCatalogIdList(String keyword);
}
