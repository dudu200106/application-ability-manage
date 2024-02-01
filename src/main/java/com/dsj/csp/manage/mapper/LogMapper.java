package com.dsj.csp.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.csp.manage.dto.ManageApplictionVo;
import com.dsj.csp.manage.entity.LogEntity;
import com.dsj.csp.manage.entity.UserApproveEntity;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Delete;

public interface LogMapper extends MPJBaseMapper<LogEntity> {


    @Delete("DELETE FROM GXYYZC_RZB")
    void removeAll();
}
