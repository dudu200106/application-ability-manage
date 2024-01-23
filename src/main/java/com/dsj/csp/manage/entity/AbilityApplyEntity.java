package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dsj.csp.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
@TableName("GXYYZC_NLSQ")
public class AbilityApplyEntity extends BaseEntity implements Serializable {
    @Schema(description = "能力申请ID")
    @TableId(value = "NLSQ_ID", type = IdType.NONE)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityApplyId;

    @Schema(description = "能力ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("NL_ID")
    private Long abilityId;

    @Schema(description = "应用ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("YY_ID")
    private Long appId;

    @Schema(description = "用户ID")
    @TableField("YH_ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @Schema(description = "能力名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @TableField("NL_MC")
    private String abilityName;

    @Schema(description = "能力类型")
    @TableField("NL_LX")
    private String abilityType;

    @Schema(description = "应用名称")
    @TableField("YY_MC")
    private String appName;

    @Schema(description = "申请使用的接口列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @TableField("NLSQ_SQJKLB")
    private String apiIds;

    @Schema(description = "公司名称")
    @TableField("QY_MC")
    private String companyName;

    @TableField("ZF_BMMC")
    @Schema(description = "政府名称")
    private String govName;

    @TableField("NLSQ_SQSM")
    @Schema(description = "申请说明")
    private String illustrate;

    @TableField("BZ")
    @Schema(description="备注")
    private String note;

    @TableField("NLSQ_SPSJ")
    @Schema(description="审批时间")
    private Date approveTime;

    @TableField(value = "NLSQ_CJSJ")
    @Schema(description="修改时间")
    private Date createTime;

    @TableField(value = "NLSQ_GXSJ")
    @Schema(description="更新时间")
    private Date updateTime;

    @TableField("NLSQ_SFTYXY")
    @Schema(description="是否同意所有协议")
    private Integer isAgreeProtocols;

    @TableField("DYLXZ")
    @Schema(description="调用量限制")
    private Integer recallLimit;


    @TableField("BFSXZ")
    @Schema(description="并发数限制")
    private Integer qps;

    @TableField("ZT")
    @Schema(description="状态")
    private Integer status;

    @TableField(value = "WG_QQMWGZ")
    @Schema(description="请求密文规则")
    private String reqCryptRule;

    @TableField(value = "WG_XYMWGZ")
    @Schema(description="响应密文规则")
    private String respCryptRule;
}
