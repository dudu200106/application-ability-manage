package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 应用列表
 *
 * @TableName MANAGE_APPLICATION
 */
@TableName(value = "MANAGE_APPLICATION")
@Data
public class ManageApplication implements Serializable {
    /**
     * id
     */
    @TableId
    @Schema(description = "appID数据库id")
    private Long appId;

    /**
     * 应用名称
     */

    @Schema(description = "app名称")
    private String appName;

    /**
     * 应用简介
     */
    @Schema(description = "简介")
    private String appSynopsis;

    /**
     * 能力绑定
     */
    @Schema(description = "能力绑定")
    private String appAbility;

    /**
     * 同意服务协议 0否 1是
     */
    @Schema(description = "同意服务协议")
    private Integer appAgreement;

    /**
     * 是否实名（认证） 0否 1是
     */
    @Schema(description = "是否实名（认证）")
    private Integer appAuthentication;

    /**
     * 密钥
     */
    @Schema(description = "秘钥")
    private String appKey;

    /**
     * 密令
     */
    @Schema(description = "密令")
    private String appSecret;

    /**
     * 状态（认证） 0不启用 1启用
     */
    @Schema(description = "状态（认证）")
    private Integer appStatus;

    /**
     * 是否删除 0正常 1删除
     */
    @Schema(description = "是否删除")
    @TableLogic
    private Integer appIsdelete;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date appCreatetime;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date appUpdatetime;

    /**
     * 备注
     */
    @Schema(description = " 备注")
    private String appRemarks;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String appCreatename;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    private String appUpdatename;

    /**
     * appid
     */
    @Schema(description = "appid展示")
    private String appCode;

    /**
     * 审核状态 0正常 1删除 2锁定 3待审核 4未通过
     */
    @Schema(description = "审核状态")
    private Integer appExamine;

    /**
     * 应用图标路径
     */
    @Schema(description = "图表路径")
    private String appIconpath;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private String appUserId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ManageApplication other = (ManageApplication) that;
        return (this.getAppId() == null ? other.getAppId() == null : this.getAppId().equals(other.getAppId()))
                && (this.getAppName() == null ? other.getAppName() == null : this.getAppName().equals(other.getAppName()))
                && (this.getAppSynopsis() == null ? other.getAppSynopsis() == null : this.getAppSynopsis().equals(other.getAppSynopsis()))
                && (this.getAppAbility() == null ? other.getAppAbility() == null : this.getAppAbility().equals(other.getAppAbility()))
                && (this.getAppAgreement() == null ? other.getAppAgreement() == null : this.getAppAgreement().equals(other.getAppAgreement()))
                && (this.getAppAuthentication() == null ? other.getAppAuthentication() == null : this.getAppAuthentication().equals(other.getAppAuthentication()))
                && (this.getAppKey() == null ? other.getAppKey() == null : this.getAppKey().equals(other.getAppKey()))
                && (this.getAppSecret() == null ? other.getAppSecret() == null : this.getAppSecret().equals(other.getAppSecret()))
                && (this.getAppStatus() == null ? other.getAppStatus() == null : this.getAppStatus().equals(other.getAppStatus()))
                && (this.getAppIsdelete() == null ? other.getAppIsdelete() == null : this.getAppIsdelete().equals(other.getAppIsdelete()))
                && (this.getAppCreatetime() == null ? other.getAppCreatetime() == null : this.getAppCreatetime().equals(other.getAppCreatetime()))
                && (this.getAppUpdatetime() == null ? other.getAppUpdatetime() == null : this.getAppUpdatetime().equals(other.getAppUpdatetime()))
                && (this.getAppRemarks() == null ? other.getAppRemarks() == null : this.getAppRemarks().equals(other.getAppRemarks()))
                && (this.getAppCreatename() == null ? other.getAppCreatename() == null : this.getAppCreatename().equals(other.getAppCreatename()))
                && (this.getAppUpdatename() == null ? other.getAppUpdatename() == null : this.getAppUpdatename().equals(other.getAppUpdatename()))
                && (this.getAppCode() == null ? other.getAppCode() == null : this.getAppCode().equals(other.getAppCode()))
                && (this.getAppExamine() == null ? other.getAppExamine() == null : this.getAppExamine().equals(other.getAppExamine()))
                && (this.getAppIconpath() == null ? other.getAppIconpath() == null : this.getAppIconpath().equals(other.getAppIconpath()))
                && (this.getAppUserId() == null ? other.getAppUserId() == null : this.getAppUserId().equals(other.getAppUserId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        result = prime * result + ((getAppName() == null) ? 0 : getAppName().hashCode());
        result = prime * result + ((getAppSynopsis() == null) ? 0 : getAppSynopsis().hashCode());
        result = prime * result + ((getAppAbility() == null) ? 0 : getAppAbility().hashCode());
        result = prime * result + ((getAppAgreement() == null) ? 0 : getAppAgreement().hashCode());
        result = prime * result + ((getAppAuthentication() == null) ? 0 : getAppAuthentication().hashCode());
        result = prime * result + ((getAppKey() == null) ? 0 : getAppKey().hashCode());
        result = prime * result + ((getAppSecret() == null) ? 0 : getAppSecret().hashCode());
        result = prime * result + ((getAppStatus() == null) ? 0 : getAppStatus().hashCode());
        result = prime * result + ((getAppIsdelete() == null) ? 0 : getAppIsdelete().hashCode());
        result = prime * result + ((getAppCreatetime() == null) ? 0 : getAppCreatetime().hashCode());
        result = prime * result + ((getAppUpdatetime() == null) ? 0 : getAppUpdatetime().hashCode());
        result = prime * result + ((getAppRemarks() == null) ? 0 : getAppRemarks().hashCode());
        result = prime * result + ((getAppCreatename() == null) ? 0 : getAppCreatename().hashCode());
        result = prime * result + ((getAppUpdatename() == null) ? 0 : getAppUpdatename().hashCode());
        result = prime * result + ((getAppCode() == null) ? 0 : getAppCode().hashCode());
        result = prime * result + ((getAppExamine() == null) ? 0 : getAppExamine().hashCode());
        result = prime * result + ((getAppIconpath() == null) ? 0 : getAppIconpath().hashCode());
        result = prime * result + ((getAppUserId() == null) ? 0 : getAppUserId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", appId=").append(appId);
        sb.append(", appName=").append(appName);
        sb.append(", appSynopsis=").append(appSynopsis);
        sb.append(", appAbility=").append(appAbility);
        sb.append(", appAgreement=").append(appAgreement);
        sb.append(", appAuthentication=").append(appAuthentication);
        sb.append(", appKey=").append(appKey);
        sb.append(", appSecret=").append(appSecret);
        sb.append(", appStatus=").append(appStatus);
        sb.append(", appIsdelete=").append(appIsdelete);
        sb.append(", appCreatetime=").append(appCreatetime);
        sb.append(", appUpdatetime=").append(appUpdatetime);
        sb.append(", appRemarks=").append(appRemarks);
        sb.append(", appCreatename=").append(appCreatename);
        sb.append(", appUpdatename=").append(appUpdatename);
        sb.append(", appCode=").append(appCode);
        sb.append(", appExamine=").append(appExamine);
        sb.append(", appIconpath=").append(appIconpath);
        sb.append(", appUserId=").append(appUserId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
