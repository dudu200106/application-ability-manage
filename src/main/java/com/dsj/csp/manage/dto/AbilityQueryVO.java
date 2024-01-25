package com.dsj.csp.manage.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.PageQuery;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Data
public class AbilityQueryVO extends PageQuery<AbilityEntity> implements Serializable {
    /**
     * 查询的实体
     */
    private AbilityEntity entity;

    /**
     * 创建时间范围--开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    /**
     * 创建时间范围--结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * 查找匹配关键字
     */
    private String keyword;

    public QueryWrapper<AbilityEntity> getQueryWrapper(){
        QueryWrapper<AbilityEntity> qw = new QueryWrapper<>();
        if (entity!=null){
            qw.lambda()
                    .eq(entity.getAbilityId()!=null, AbilityEntity::getAbilityId, entity.getAbilityId())
                    .eq(entity.getAbilityId() != null, AbilityEntity::getAbilityId, entity.getAbilityId())
                    .eq(entity.getAbilityType() != null, AbilityEntity::getAbilityType, entity.getAbilityType())
                    .eq(entity.getAbilityName() != null, AbilityEntity::getAbilityName, entity.getAbilityName())
                    .eq(entity.getUserId() != null, AbilityEntity::getUserId, entity.getUserId())
                    .eq(entity.getAbilityProvider() != null, AbilityEntity::getAbilityProvider, entity.getAbilityProvider())
                    .eq(entity.getAbilityDesc() != null, AbilityEntity::getAbilityDesc, entity.getAbilityDesc())
                    .eq(entity.getStatus() != null, AbilityEntity::getStatus, entity.getStatus())
                    .eq(entity.getNote() != null, AbilityEntity::getNote, entity.getNote())
                    .eq(entity.getCreateTime() != null, AbilityEntity::getCreateTime, entity.getCreateTime())
                    .eq(entity.getUpdateTime() != null, AbilityEntity::getUpdateTime, entity.getUpdateTime());
        }
        qw.lambda()
                .ge(Objects.nonNull(startTime), AbilityEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityEntity::getCreateTime, endTime)
                .and(keyword!=null && !"".equals(keyword),
                        i -> i.like(AbilityEntity::getAbilityName, keyword)
                                .or().like(AbilityEntity::getAbilityProvider, keyword)
                );
        qw.lambda()
                .orderByAsc(AbilityEntity::getStatus)
                .orderByDesc(AbilityEntity::getCreateTime);
        return qw;
    }


    public Page<AbilityEntity> toPage() {
        return new Page<>(this.getCurrent(), this.getSize());
    }

}
