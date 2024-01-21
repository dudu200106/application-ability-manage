package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dsj.csp.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@TableName("GXYYZC_NLSQ")
public class AbilityApplyEntity extends BaseEntity implements Serializable {
    @TableId(value = "NLSQ_XH", type = IdType.AUTO)
    private Long abilityApplyId;

    @TableField("NL_XH")
    private Long abilityId;

    @TableField("YY_XH")
    private Long appId;

    @TableField("YH_XH")
    private Long userId;

    @TableField("NL_MC")
    private String abilityName;

    @TableField("NL_LX")
    private String abilityType;

    @TableField("YY_MC")
    private String appName;

    @TableField("SQJKLB")
    private String apiIds;

    @TableField("QY_MC")
    private String companyName;

    @TableField("ZF_BMMC")
    private String govName;

    @TableField("zt")
    private Integer status;

    @TableField("SQSM")
    private String illustrate;

    @TableField("BZ")
    private String note;

    @TableField("SPSJ")
    private Date approveTime;

    @TableField(value = "CJSJ")
    private Date createTime;

    @TableField(value = "GXSJ")
    private Date updateTime;

    @TableField("SFTYXY")
    private Integer isAgreeProtocols;

    @TableField("DYLXZ")
    private Integer recallLimit;

    @TableField("BFSXZ")
    private Integer qps;
}
