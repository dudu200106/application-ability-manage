package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dsj.csp.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@TableName("GXYYZC_NLSQ")
public class AbilityApplyEntity extends BaseEntity implements Serializable {
    @TableId(value = "NLSQ_ID", type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityApplyId;

    @TableField("NL_ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityId;

    @TableField("YY_ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long appId;

    @TableField("YH_ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @TableField("NL_MC")
    private String abilityName;

    @TableField("NL_LX")
    private String abilityType;

    @TableField("YY_MC")
    private String appName;

    @TableField("NLSQ_SQJKLB")
    private String apiIds;

    @TableField("QY_MC")
    private String companyName;

    @TableField("ZF_BMMC")
    private String govName;

    @TableField("zt")
    private Integer status;

    @TableField("NLSQ_SQSM")
    private String illustrate;

    @TableField("BZ")
    private String note;

    @TableField("NLSQ_SPSJ")
    private Date approveTime;

    @TableField(value = "NLSQ_CJSJ")
    private Date createTime;

    @TableField(value = "NLSQ_GXSJ")
    private Date updateTime;

    @TableField("NLSQ_SFTYXY")
    private Integer isAgreeProtocols;

    @TableField("DYLXZ")
    private Integer recallLimit;

    @TableField("BFSXZ")
    private Integer qps;

    @TableField(value = "WG_QQMWGZ")
    private String reqCryptRule;

    @TableField(value = "WG_XYMWGZ")
    private String respCryptRule;
}
