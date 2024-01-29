package com.dsj.csp.manage.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dsj.common.dto.PageQuery;
import com.dsj.csp.manage.entity.AbilityApiEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
public class AbilityApiQueryVO extends PageQuery<AbilityApiEntity> implements Serializable {
    public AbilityApiQueryVO(){
    }

    public AbilityApiQueryVO(Long userId, Long appId, Long abilityId, int size, int current, String keyword, Date startTime, Date endTime) {
        super.setSize(size);
        super.setCurrent(current);
        this.appId = appId;
        this.userId = userId;
        this.abilityId = abilityId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.keyword = keyword;
    }

    /**
     * 查询的实体
     */
    private AbilityApiEntity entity;
//    private Long
    private Long abilityId;
    private Long appId;
    private Long userId;

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


    public QueryWrapper<AbilityApiEntity> getQueryWrapper(){
        QueryWrapper<AbilityApiEntity> qw = new QueryWrapper<>();
        if (entity!=null){
            qw.lambda()
                    .eq(entity.getApiId() != null, AbilityApiEntity::getApiId, entity.getApiId())
                    .eq(entity.getAbilityId() != null, AbilityApiEntity::getAbilityId, entity.getAbilityId())
                    .eq(entity.getApiName() != null, AbilityApiEntity::getApiName, entity.getApiName())
                    .eq(entity.getDescription() != null, AbilityApiEntity::getDescription, entity.getDescription())
                    .eq(entity.getRecallLimit() != null, AbilityApiEntity::getRecallLimit, entity.getRecallLimit())
                    .eq(entity.getQps() != null, AbilityApiEntity::getQps, entity.getQps())
                    .eq(entity.getRespFormat() != null, AbilityApiEntity::getRespFormat, entity.getRespFormat())
                    .eq(entity.getProtocol() != null, AbilityApiEntity::getProtocol, entity.getProtocol())
                    .eq(entity.getApiHost() != null, AbilityApiEntity::getApiHost, entity.getApiHost())
                    .eq(entity.getApiUrl() != null, AbilityApiEntity::getApiUrl, entity.getApiUrl())
                    .eq(entity.getApiVersion() != null, AbilityApiEntity::getApiVersion, entity.getApiVersion())
                    .eq(entity.getCreateTime() != null, AbilityApiEntity::getCreateTime, entity.getCreateTime())
                    .eq(entity.getUpdateTime() != null, AbilityApiEntity::getUpdateTime, entity.getUpdateTime())
                    .eq(entity.getPublicKey() != null, AbilityApiEntity::getPublicKey, entity.getPublicKey())
                    .eq(entity.getSecretKey() != null, AbilityApiEntity::getSecretKey, entity.getSecretKey())
                    .eq(entity.getReqMethod() != null, AbilityApiEntity::getReqMethod, entity.getReqMethod())
                    .eq(entity.getReqDemo() != null, AbilityApiEntity::getReqDemo, entity.getReqDemo())
                    .eq(entity.getRespDemo() != null, AbilityApiEntity::getRespDemo, entity.getRespDemo())
                    .eq(entity.getRespStatusCode() != null, AbilityApiEntity::getRespStatusCode, entity.getRespStatusCode());
        }
        qw.lambda()
                .ge(Objects.nonNull(startTime), AbilityApiEntity::getCreateTime, startTime)
                .le(Objects.nonNull(endTime), AbilityApiEntity::getCreateTime, endTime)
                .and(keyword!=null && !"".equals(keyword),
                        i -> i.like(AbilityApiEntity::getApiName, keyword)
                );
        return qw;
    }

    public Page<AbilityApiEntity> toPage() {
        return new Page<>(this.getCurrent(), this.getSize());
    }
    
}
