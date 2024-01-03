package com.dsj.csp.manage.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 功能说明：
 *
 * @author 蔡云
 * @date 2023/12/29
 */
@Data
public class PageQueryForm<T> {

    // 查询实体
    private T entity;
    // 分页信息
    protected long size;
    protected long current;

    /**
     * 转为 Wrappers 查询条件
     */
    public QueryWrapper<T> toQueryWrappers() {
        return Wrappers.query(entity);
    }

    /**
     * 转为 MybatisPlus 分页查询条件
     */
    public Page<T> toPage() {
        return new Page<>(current, size);
    }
}
