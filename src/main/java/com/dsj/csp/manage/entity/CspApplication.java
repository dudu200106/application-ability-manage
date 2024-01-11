package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 应用列表
 * @TableName CSP_APPLICATION
 */
@TableName(value ="CSP_APPLICATION")
@Data
public class CspApplication implements Serializable {
    /**
     * id
     */
    @TableId
    private Long appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用简介
     */
    private String appSynopsis;

    /**
     * 能力绑定
     */
    private String appAbility;

    /**
     * 同意服务协议 0否 1是
     */
    private Long appAgreement;

    /**
     * 是否实名（认证） 0否 1是
     */
    private Long appAuthentication;

    /**
     * 密钥
     */
    private String appKey;

    /**
     * 密令
     */
    private String appSecret;

    /**
     * 状态（认证） 0不启用 1启用
     */
    private int appStatus;

    /**
     * 是否删除 0正常 1删除
     */

    @TableLogic
    private int appIsdelete;

    /**
     * 创建时间
     */
    private Date appCreatetime;

    /**
     * 修改时间
     */
    private Date appUpdatetime;

    /**
     * 备注
     */
    private String appRemarks;

    /**
     * 创建人
     */
    private String appCreatename;

    /**
     * 修改人
     */
    private String appUpdatename;

    /**
     * appid
     */
    private String appCode;

    /**
     * 是否审核
     */
    private Long appExamine;

    /**
     * 应用图标路径
     */
    private String appIconpath;

    /**
     * 用户id
     */
    private String appUserId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}