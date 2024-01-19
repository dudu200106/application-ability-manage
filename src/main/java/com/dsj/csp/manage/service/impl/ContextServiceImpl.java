package com.dsj.csp.manage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsj.common.dto.BusinessException;
import com.dsj.csp.common.enums.StatusEnum;
import com.dsj.csp.common.enums.UsableStatusEnum;
import com.dsj.csp.manage.dto.request.ContextRequest;
import com.dsj.csp.manage.entity.ContextEntity;
import com.dsj.csp.manage.service.ContextService;
import com.dsj.csp.manage.mapper.ContextMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
* @author Huawei
* @description 针对表【GXYYZC_YYCJ】的数据库操作Service实现
* @createDate 2024-01-18 19:32:36
*/
@Service
public class ContextServiceImpl extends ServiceImpl<ContextMapper, ContextEntity> implements ContextService {
    @Override
    public void add(ContextEntity context) {
        context.setCreateTime(new Date());
        baseMapper.insert(context);
    }

    @Override
    public void update(ContextRequest context) {
        boolean update = this.lambdaUpdate()
                .eq(ContextEntity::getId,context.getId())
                .set(Objects.nonNull(context.getIsUsable()),ContextEntity::getIsUsable, context.getIsUsable())
                .set(StringUtils.isNotBlank(context.getTitle()),ContextEntity::getTitle,context.getTitle())
                .set(StringUtils.isNotBlank(context.getDescribe()),ContextEntity::getDescribe,context.getDescribe())
                .set(StringUtils.isNotBlank(context.getUrl()),ContextEntity::getUrl,context.getUrl())
                .set(ContextEntity::getUpdateTime,new Date())
                .update();
        if (!update) {
            log.error("更新失败");
            throw new BusinessException("更新失败");
        }
    }

    @Override
    public void delete(ContextRequest context) {
        boolean update = this.lambdaUpdate()
                .eq(ContextEntity::getId,context.getId())
                .eq(ContextEntity::getIsDelete, StatusEnum.NORMAL.getStatus())
                .set(ContextEntity::getIsDelete, StatusEnum.DEL.getStatus())
                .set(ContextEntity::getIsUsable, UsableStatusEnum.BAN.getStatus())
                .set(ContextEntity::getUpdateTime,new Date())
                .update();
        if (!update) {
            log.error("删除失败");
            throw new BusinessException("删除失败");
        }
    }

    @Override
    public Page<ContextEntity> search(String keyword,Integer isUsable,int page,int size) {
        QueryWrapper<ContextEntity> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(ContextEntity::getIsDelete,StatusEnum.NORMAL.getStatus())
                .eq(Objects.nonNull(isUsable),ContextEntity::getIsUsable,isUsable)
                .and(StringUtils.isNotBlank(keyword), lambdaQuery->{
                    lambdaQuery
                            .like(ContextEntity::getTitle, keyword)
                            .or()
                            .like(ContextEntity::getDescribe, keyword);
                });
        return baseMapper.selectPage(new Page(page,size),wrapper);
    }
}




