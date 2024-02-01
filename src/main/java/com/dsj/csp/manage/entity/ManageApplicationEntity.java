package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 应用列表
 *
 * @TableName MANAGE_APPLICATION
 */
@TableName(value = "GXYYZC_YYB")
@Data
public class ManageApplicationEntity implements Serializable {
    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "YY_ID")
    @Schema(description = "appID数据库id")
    private String  appId;

    /**
     * 应用名称
     */
    @NotEmpty(message = "应用名称不能为空")
    @TableField(value = "YY_MC")
    @Schema(description = "app名称")
    @Min(value = 50, message = "应用名称不能超过30字")
    private String appName;

    /**
     * 应用简介
     */
    @TableField(value = "YY_JC")
    @Schema(description = "简介")
    @NotEmpty(message = "应用简介不能为空")
    @Min(value = 500, message = "请控制在200字以内")
    private String appSynopsis;

    /**
     * 能力绑定
     */
    @TableField(value = "YY_NLBD")
    @Schema(description = "能力绑定")
    private String appAbility;

    /**
     * 同意服务协议 0否 1是
     */
    @TableField(value = "YY_FWXY")
    @Schema(description = "同意服务协议")
    private Integer appAgreement;

    /**
     * 是否实名（认证） 0否 1是
     */
    @TableField(value = "YY_SMRZ")
    @Schema(description = "是否实名（认证）")
    private Integer appAuthentication;

    /**
     * 密钥
     */
    @TableField(value = "YY_KEY")
    @Schema(description = "秘钥")
    private String appKey;

    /**
     * 密令
     */
    @TableField(value = "YY_SECRET")
    @Schema(description = "密令")
    private String appSecret;


    @TableField(value = "YY_WGKEY")
    @Schema(description = "网关秘钥")
    private String appWgKey;

    /**
     * 密令
     */
    @TableField(value = "YY_WGSECRET")
    @Schema(description = "网关密令")
    private String appWgSecret;

    /**
     * 状态（认证） 0不启用 1启用
     */
    @TableField(value = "YY_ZT")
    @Schema(description = "状态（认证）")
    private Integer appStatus;

    /**
     * 是否删除 0正常 1删除
     */
    @TableField(value = "YY_ISDELETE")
    @Schema(description = "是否删除")
    @TableLogic
    private Integer appIsdelete;

    /**
     * 创建时间
     */
    @TableField(value = "YY_CREATETIME" ,fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date appCreatetime;

    /**
     * 修改时间
     */
    @TableField(value = "YY_UPDATETIME", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "修改时间")
    private Date appUpdatetime;

    /**
     * 备注
     */
    @TableField(value = "YY_BZ")
    @Schema(description = "备注")
    private String appRemarks;

    /**
     * 创建人
     */
    @TableField(value = "YY_CREATENAME")
    @Schema(description = "创建人")
    private String appCreatename;

    /**
     * 修改人
     */
    @TableField(value = "YY_UPDATENAME")
    @Schema(description = "修改人")
    private String appUpdatename;

    /**
     * appid
     */
    @TableField(value = "YY_CODE")
    @Schema(description = "appid展示")
    private String appCode;

    /**
     * 审核状态 0正常 1删除 2锁定 3待审核 4未通过
     */
    @TableField(value = "YY_SHZT")
    @Schema(description = "审核状态")
    private Integer appExamine;

    /**
     * 应用图标路径
     */
    @TableField(value = "YY_TB")
    @Schema(description = "图表路径")
    private String appIconpath;

    /**
     * 用户id
     */
    @TableField(value = "YH_XH_ID")
    @Schema(description = "用户id")
    private String appUserId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}