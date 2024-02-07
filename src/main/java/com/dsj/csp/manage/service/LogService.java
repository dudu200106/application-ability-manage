package com.dsj.csp.manage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dsj.csp.manage.dto.ManageApplictionVo;
import com.dsj.csp.manage.entity.LogEntity;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.Date;

public interface LogService extends IService<LogEntity> {
    void removeAll();

    Page<LogEntity> select(String keyword, Date startTime, Date endTime, int page, int size,String operateType);
}
