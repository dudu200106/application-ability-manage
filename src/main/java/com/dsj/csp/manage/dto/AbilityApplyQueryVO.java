package com.dsj.csp.manage.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.PageQuery;
import com.dsj.csp.manage.entity.AbilityApplyEntity;
import com.dsj.csp.manage.entity.AbilityEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/17
 */

@Data
public class AbilityApplyQueryVO extends PageQuery<AbilityApplyEntity> implements Serializable {

    private AbilityApplyEntity entity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    private String keyword;

    public QueryWrapper<AbilityApplyEntity> getQueryWrapper(){
        QueryWrapper<AbilityApplyEntity> qw = new QueryWrapper<>();
        qw.lambda()
                .eq(entity.getAbilityApplyId() != null, AbilityApplyEntity::getAbilityApplyId, entity.getAbilityApplyId())
                .eq(entity.getAbilityId() != null, AbilityApplyEntity::getAbilityId, entity.getAbilityId())
                .eq(entity.getAppId() != null, AbilityApplyEntity::getAppId, entity.getAppId())
                .eq(entity.getUserId() != null, AbilityApplyEntity::getUserId, entity.getUserId())
                .eq(entity.getAbilityName() != null, AbilityApplyEntity::getAbilityName, entity.getAbilityName())
                .eq(entity.getAbilityType() != null, AbilityApplyEntity::getAbilityType, entity.getAbilityType())
                .eq(entity.getAppName() != null, AbilityApplyEntity::getAppName, entity.getAppName())
                .eq(entity.getApiIds() != null, AbilityApplyEntity::getApiIds, entity.getApiIds())
                .eq(entity.getCompanyName() != null, AbilityApplyEntity::getCompanyName, entity.getCompanyName())
                .eq(entity.getGovName() != null, AbilityApplyEntity::getGovName, entity.getGovName())
                .eq(entity.getStatus() != null, AbilityApplyEntity::getStatus, entity.getStatus())
                .eq(entity.getIllustrate() != null, AbilityApplyEntity::getIllustrate, entity.getIllustrate())
                .eq(entity.getNote() != null, AbilityApplyEntity::getNote, entity.getNote())
                .eq(entity.getApproveTime() != null, AbilityApplyEntity::getApproveTime, entity.getApproveTime())
                .eq(entity.getCreateTime() != null, AbilityApplyEntity::getCreateTime, entity.getCreateTime())
                .eq(entity.getUpdateTime() != null, AbilityApplyEntity::getUpdateTime, entity.getUpdateTime())
                .eq(entity.getIsAgreeProtocols() != null, AbilityApplyEntity::getIsAgreeProtocols, entity.getIsAgreeProtocols())
                .eq(entity.getRecallLimit() != null, AbilityApplyEntity::getRecallLimit, entity.getRecallLimit())
                .eq(entity.getQps() != null, AbilityApplyEntity::getQps, entity.getQps());
        qw.lambda()
                .ge(Objects.nonNull(startTime), AbilityApplyEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityApplyEntity::getCreateTime, endTime)
                .and(keyword!=null && !"".equals(keyword),
                        i -> i.like(AbilityApplyEntity::getAbilityName, keyword)
                                .or().like(AbilityApplyEntity::getCompanyName, keyword)
                                .or().like(AbilityApplyEntity::getGovName, keyword)
                );
        return qw;
    }

    public Page<AbilityApplyEntity> toPage() {
        return new Page<>(this.getCurrent(), this.getSize());
    }

}
