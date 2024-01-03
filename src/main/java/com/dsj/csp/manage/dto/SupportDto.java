package com.dsj.csp.manage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2024/1/3 10:59
 */
@Data
public class SupportDto implements Serializable {
    private Long supportId;

    private Long appId;

    private String title;

    private Long abilityId;

    private String abilityName;

    private Long apiId;

    private String apiName;

    private Long createUserId;

    private String createUserName;

    private Long acceptUserId;

    private String acceptUserName;

    private String description;

    private String requestJson;

    private String responseJson;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
