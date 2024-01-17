package com.dsj.csp.manage.dto;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONString;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.PageQuery;
import com.dsj.csp.manage.entity.AbilityEntity;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Data
public class AbilityQueryDTO extends PageQuery<AbilityEntity> implements Serializable {

    private AbilityEntity entity;

    private  Date startTime;

    private Date endTime;

    private String keyword;

    public QueryWrapper<AbilityEntity> getQueryWrapper(){
        QueryWrapper<AbilityEntity> qw = new QueryWrapper<>();
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
                .eq(entity.getUpdateTime() != null, AbilityEntity::getUpdateTime, entity.getUpdateTime())
                .eq(entity.getRecallLimit() != null, AbilityEntity::getRecallLimit, entity.getRecallLimit())
                .eq(entity.getQps() != null, AbilityEntity::getQps, entity.getQps());
        qw
                .ge(Objects.nonNull(startTime), "CREATE_TIME", startTime)
                .le(Objects.nonNull(endTime), "CREATE_TIME", endTime)
                .and(keyword!=null && !"".equals(keyword),
                        i -> i.like("ABILITY_NAME", keyword)
                                .or().like("ABILITY_PROVIDER", keyword)
                );
        return qw;
    }


    public Page<AbilityEntity> toPage() {
        return new Page<>(this.getCurrent(), this.getSize());
    }

}
