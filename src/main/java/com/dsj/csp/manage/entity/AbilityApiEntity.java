package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@EqualsAndHashCode
@TableName("GXYYZC_NLJK")
public class AbilityApiEntity implements Serializable {
    @TableId("NLJK_ID")
    @Schema(description = "接口ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiId;

    @TableField("NL_ID")
    @Schema(description = "能力ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityId;

    @TableField("NLJK_MC")
    @Schema(description = "接口名称")
    private String apiName;

    @TableField("NLJK_MS")
    @Schema(description = "接口描述")
    private String apiDesc;

    @TableField("DYLXZ")
    @Schema(description = "调用量限制")
    private Integer recallLimit;

    @TableField("BFSXZ")
    @Schema(description = "并发数限制")
    private Integer qps;

    @TableField("NLJK_XYGS")
    @Schema(description = "响应数据格式")
    private String respFormat;

    @TableField("NLJK_XY")
    @Schema(description = "协议")
    private String protocol;

    @TableField("NLJK_ZJDZ")
    @Schema(description = "主机地址")
    private String apiHost;

    @TableField("NLJK_URL")
    @Schema(description = "接口URL")
    private String apiUrl;

    @TableField("NLJK_BBH")
    @Schema(description = "接口版本")
    private String apiVersion;


    @TableField(value = "WG_GY")
    @Schema(description = "公钥")
    private String publicKey;

    @TableField(value = "WG_SY")
    @Schema(description = "私钥")
    private String secretKey;

    @TableField(value = "NLJK_QQFF")
    @Schema(description = "请求方式")
    private String reqMethod;

    @TableField(value = "NLJK_QQSL")
    @Schema(description = "请求示例")
    private String reqDemo;

    @TableField(value = "NLJK_XYSL")
    @Schema(description = "响应示例")
    private String respDemo;

    @TableField(value = "NLJK_XYZTM")
    @Schema(description = "响应状态码")
    private String respStatusCode;

    @TableField(value = "ZT")
    @Schema(description = "状态")
    private Integer status;

    @TableField(value = "BZ")
    @Schema(description = "状态")
    private String note;

    @TableField(value = "YH_ID")
    @Schema(description = "接口提供者ID")
    private Long userId;

    /**
     * 逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(value = "IS_DELETE")
    @Schema(description = "逻辑删除")
    private Integer isDelete;

    @TableField(value = "NLJK_CJSJ" )
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(value = "NLJK_GXSJ")
    @Schema(description = "更新时间")
    private Date updateTime;

    @TableField(value = "NLJK_SPSJ")
    @Schema(description = "审批时间")
    private Date approveTime;

}
