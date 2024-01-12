package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dsj.csp.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/10
 */
@Data
@TableName("MANAGE_ABILITY")
public class AbilityEntity extends BaseEntity implements Serializable {
    @TableId(value = "ABILITY_ID", type = IdType.AUTO)
    private Long abilityId;

    @TableField("ABILITY_TYPE")
    private String abilityType;

    @TableField("ABILITY_NAME")
    private String abilityName;

    @TableField("USER_ID")
    private Long userId;

    @TableField("ABILITY_PROVIDER")
    private String abilityProvider;

    @TableField("ABILITY_DESC")
    private String abilityDesc;

    @TableField("STATUS")
    private Integer status;

    @TableField("NOTE")
    private String note;

    @TableLogic
    private Integer isDelete;

    @TableField(value = "CREATE_TIME")
    private Date createTime;

    @TableField(value = "UPDATE_TIME")
    private Date updateTime;

    @TableField("RECALL_LIMIT")
    private Integer recallLimit;

    @TableField("QPS")
    private Integer qps;
}
