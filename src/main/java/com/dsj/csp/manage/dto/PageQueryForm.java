package com.dsj.csp.manage.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 功能说明：
 *
 * @author 蔡云
 * 2023/12/29
 */
@Data
    public class PageQueryForm<T> {

    // 查询实体
    private T entity;
    // 分页信息
    @Min(value = 1, message = "当前页码必须大于0")
    protected long size;

    @Min(value = 1, message = "每页条数必须大于0")
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
