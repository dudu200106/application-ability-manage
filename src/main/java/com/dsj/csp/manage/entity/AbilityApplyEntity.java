package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dsj.csp.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@TableName("MANAGE_ABILITY_APPLY")
public class AbilityApplyEntity extends BaseEntity implements Serializable {
    @TableId(value = "ABILITY_APPLY_ID", type = IdType.AUTO)
    private Long abilityApplyId;

    @TableField("ABILITY_ID")
    private Long abilityId;

    @TableField("APP_ID")
    private Long appId;

    @TableField("USER_ID")
    private Long userId;

    @TableField("ABILITY_NAME")
    private String abilityName;

    @TableField("ABILITY_TYPE")
    private String abilityType;

    @TableField("APP_NAME")
    private String appName;

    @TableField("API_IDS")
    private String apiIds;

    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("GOV_NAME")
    private String govName;

    @TableField("STATUS")
    private Integer status;

    @TableField("illustrate")
    private String illustrate;

    @TableField("NOTE")
    private String note;

    @TableField("APPROVE_TIME")
    private Date approveTime;

    // mybatis plus 好像会与POJO里面的isDelete冲突
//    @TableField("IS_DELETE")
//    private Integer isDelete;

    @TableField(value = "CREATE_TIME")
    private Date createTime;

    @TableField(value = "UPDATE_TIME")
    private Date updateTime;
}
