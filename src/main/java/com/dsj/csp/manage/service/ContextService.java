package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.request.ContextRequest;
import com.dsj.csp.manage.entity.ContextEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Huawei
* @description 针对表【GXYYZC_YYCJ】的数据库操作Service
* @createDate 2024-01-18 19:32:36
*/
public interface ContextService extends IService<ContextEntity> {
    /**
     * 新增应用场景
     */
    void add(ContextEntity context);

    /**
     * 修改应用场景
     */
    void update(ContextRequest context);

    /**
     * 删除应用场景
     */
    void delete(ContextRequest context);
    /**
     * 按条件分页查询可用应用场景
     */
    Page<ContextEntity> search(String keyword,Integer isUsable,int page,int size);
}
