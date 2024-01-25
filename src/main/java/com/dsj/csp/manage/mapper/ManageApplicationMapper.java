package com.dsj.csp.manage.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dsj.csp.manage.entity.ManageApplicationEntity;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.Date;
import java.util.List;

/**
 * @author DSCBooK
 * @description 针对表【MANAGE_APPLICATION(应用列表)】的数据库操作Mapper
 * @createDate 2024-01-11 10:43:10
 * @Entity generator.entity.ManageApplication
 */
public interface ManageApplicationMapper extends MPJBaseMapper<ManageApplicationEntity> {
}




