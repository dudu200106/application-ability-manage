package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.csp.manage.entity.LogEntity;
import com.dsj.csp.manage.mapper.LogMapper;
import com.dsj.csp.manage.service.LogService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogEntity> implements LogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public void removeAll() {
        logMapper.removeAll();
    }

    @Override
    public Page<LogEntity> select(String keyword, Date startTime, Date endTime, int page, int size, String operateType) {
        QueryWrapper<LogEntity> wrapper = new QueryWrapper();
        wrapper.lambda().orderByDesc(LogEntity::getCreateTime);
        wrapper.lambda()
                .between(Objects.nonNull(startTime) && Objects.nonNull(endTime), LogEntity::getCreateTime, startTime, endTime)
                .and(StringUtils.isNotBlank(operateType), lambdaQuery -> {
                    lambdaQuery
                            .like(StringUtils.isNotBlank(operateType), LogEntity::getOperateType, operateType)
                    ;
                })
                .and(StringUtils.isNotBlank(keyword), lambdaQuery -> {
                    lambdaQuery
                            .like(StringUtils.isNotBlank(keyword), LogEntity::getLogContent, keyword)
                            .or()
                            .like(StringUtils.isNotBlank(keyword), LogEntity::getUsername, keyword)
                    ;
                });
        return baseMapper.selectPage(new Page(page, size), wrapper);

    }
}
