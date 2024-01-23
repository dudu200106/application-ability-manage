package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dsj.csp.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("GXYYZC_NLJK")
public class AbilityApiEntity extends BaseEntity implements Serializable {
    @TableId("NLJK_XH")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiId;

    @TableField("NL_XH")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityId;

    @TableField("NLJK_MC")
    private String apiName;

    @TableField("NLJK_MS")
    private String description;

    @TableField("DYLXZ")
    private Integer recallLimit;

    @TableField("BFSXZ")
    private Integer qps;

    @TableField("NLJK_XYGS")
    private String respFormat;

    @TableField("NLJK_XY")
    private String protocol;

    @TableField("NLJK_ZJDZ")
    private String apiHost;

    @TableField("NLJK_URL")
    private String apiUrl;

    @TableField("NLJK_BBH")
    private String apiVersion;

    @TableField(value = "NLJK_CJSJ" )
    private Timestamp createTime;

    @TableField(value = "NLJK_GXSJ")
    private Timestamp updateTime;

    @TableField(value = "WG_GY")
    private String publicKey;

    @TableField(value = "WG_SY")
    private String secretKey;

    @TableField(value = "NLJK_QQFF")
    private String reqMethod;

    @TableField(value = "NLJK_QQSL")
    private String reqDemo;

    @TableField(value = "NLJK_XYSL")
    private String respDemo;

    @TableField(value = "NLJK_XYZTM")
    private String respStatusCode;


}
