package com.dsj.csp.manage.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
* 接口申请表
* @TableName GXYYZC_NLJKSQ
*/
@Data
@EqualsAndHashCode
@TableName("GXYYZC_NLJKSQ")
public class AbilityApiApplyEntity implements Serializable {

    /**
    * 接口申请ID
    */
    @TableId(value = "NLJKSQ_ID", type = IdType.NONE)
    @Schema(description="接口申请ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiApplyId;

    /**
    * 接口ID
    */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("NLJK_ID")
    @Schema(description="接口ID")
    private Long apiId;

    /**
    * 应用ID
    */
    @Schema(description = "应用ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("YY_ID")
    private Long appId;

    /**
    * 用户ID
    */
    @Schema(description = "用户ID")
    @TableField("YH_ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
    * 能力ID
    */
    @Schema(description = "能力ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("NL_ID")
    private Long abilityId;

    /**
    * 能力名称
    */
    @Length(max= 30,message="编码长度不能超过30")
    @Schema(description = "能力名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @TableField("NL_MC")
    private String abilityName;

    /**
    * 应用名称
    */
    @Length(max= 30,message="编码长度不能超过30")
    @Schema(description = "应用名称")
    @TableField("YY_MC")
    private String appName;

    /**
    * 接口名称
    */
    @Length(max= 30,message="编码长度不能超过30")
    @Schema(description = "接口名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @TableField("NLJK_MC")
    private String apiName;

    /**
    * 调用量限制
    */
    @TableField("DYLXZ")
    @Schema(description="调用量限制")
    private Integer recallLimit;

    /**
    * 并发数限制
    */
    @TableField("BFSXZ")
    @Schema(description="并发数限制")
    private Integer qps;

    /**
    * 状态 (默认:0未提交1待审核 2审核未通过 3未发布 4已发布 5已下线)
    */
    @Schema(description="状态 (默认:0未提交1待审核 2审核未通过 3未发布 4已发布 5已下线)")
    @TableField("ZT")
    private Integer status;

    /**
    * 申请说明
    */
    @TableField("NLSQ_SQSM")
    @Schema(description = "申请说明")
    private String illustrate;

    /**
    * 备注
    */
    @Length(max= 300,message="编码长度不能超过300")
    @TableField("BZ")
    @Schema(description="备注")
    private String note;

    /**
     * 逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(value = "IS_DELETE")
    @Schema(description="是否删除")
    private Integer isDelete;

    /**
    * 创建时间
    */
    @TableField(value = "NLSQ_CJSJ", fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private Date createTime;

    /**
    * 更新时间
    */
    @TableField(value = "NLSQ_GXSJ", fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新时间")
    private Date updateTime;

    /**
     * 审批时间
     */
    @TableField("NLSQ_SPSJ")
    @Schema(description="审批时间")
    private Date approveTime;

    /**
     * 审批时间
     */
    @TableField(value = "NLJK_TJSJ", fill = FieldFill.INSERT)
    @Schema(description="提交时间")
    private Date submitTime;

    /**
    * 是否同意协议
    */
    @TableField("NLSQ_SFTYXY")
    @Schema(description="是否同意所有协议")
    private Integer isAgreeProtocols;

    /**
    * 请求密文规则
    */
    @TableField(value = "WG_QQMWGZ")
    @Schema(description="请求密文规则")
    private String reqCryptRule;

    /**
    * 响应密文规则
    */
    @TableField(value = "WG_XYMWGZ")
    @Schema(description="响应密文规则")
    private String respCryptRule;

}
