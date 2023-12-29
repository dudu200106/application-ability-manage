package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 应用表
 * @TableName MANAGE_APP
 */
@TableName(value ="MANAGE_APP")
@Data
public class AppEntity implements Serializable {
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

    /**
     * 逻辑删除（0未删除 1已删除 ）
     */
    @TableField(value = "IS_DELETE")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = -1850215692500928269L;
}
