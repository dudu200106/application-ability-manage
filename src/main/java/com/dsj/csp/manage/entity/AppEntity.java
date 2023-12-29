package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.dsj.csp.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应用表
 * @TableName MANAGE_APP
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="MANAGE_APP")
@Data
public class AppEntity extends BaseEntity implements Serializable {
    /**
     * 应用id
     */
    @TableId(value = "APP_ID", type = IdType.AUTO)
    private Long appId;

    /**
     * 应用号
     */
    @TableField(value = "APP_CODE")
    private String appCode;

    /**
     * 应用创建人id（用户id）
     */
    @TableField(value = "CREATE_USER_ID")
    private Long createUserId;

    /**
     * APIkey
     */
    @TableField(value = "APP_KEY")
    private String appKey;

    /**
     * secretkey
     */
    @TableField(value = "SECRET_KEY")
    private String secretKey;

    /**
     * 应用名称
     */
    @TableField(value = "APP_NAME")
    private String appName;

    /**
     * 应用logo
     */
    @TableField(value = "APP_LOGO")
    private String appLogo;

    /**
     * 应用联系人名称
     */
    @TableField(value = "APP_USERNAME")
    private String appUsername;

    /**
     * 联系人电话
     */
    @TableField(value = "APP_PHONE")
    private String appPhone;

    /**
     * 应用资质证明材料（存储文件id，中间逗号隔开）
     */
    @TableField(value = "APP_EVIDENCE")
    private String appEvidence;

    /**
     * 应用状态（0未启用 1已启用）
     */
    @TableField(value = "APP_STATUS")
    private Integer appStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = -1850215692500928269L;
}
