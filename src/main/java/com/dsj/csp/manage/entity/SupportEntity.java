package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dsj.csp.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2024/1/3 10:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("MANAGE_SUPPORT")
public class SupportEntity extends BaseEntity {
    @TableId(value = "SUPPORT_ID", type = IdType.AUTO)
    private Long supportId;

    @TableField("APP_ID")
    private Long appId;

    @TableField("TITLE")
    private String title;

    @TableField("ABILITY_ID")
    private Long abilityId;

    @TableField("ABILITY_NAME")
    private String abilityName;

    @TableField("API_ID")
    private Long apiId;

    @TableField("API_NAME")
    private String apiName;

    @TableField("CREATE_USER_ID")
    private Long createUserId;

    @TableField("CREATE_USER_NAME")
    private String createUserName;

    @TableField("ACCEPT_USER_ID")
    private Long acceptUserId;

    @TableField("ACCEPT_USER_NAME")
    private String acceptUserName;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("REQUEST_JSON")
    private String requestJson;

    @TableField("RESPONSE_JSON")
    private String responseJson;

    @TableField("STATUS")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("FINISH_TIME")
    private Date finishTime;
}
