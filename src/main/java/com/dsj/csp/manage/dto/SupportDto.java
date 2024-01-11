package com.dsj.csp.manage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "工单ID")
    private Long supportId;

    @Schema(description = "应用ID")
    private Long appId;

    @Schema(description = "工单标题")
    private String title;

    @Schema(description = "能力ID")
    private Long abilityId;

    @Schema(description = "能力名称")
    private String abilityName;

    @Schema(description = "API ID")
    private Long apiId;

    @Schema(description = "API名称")
    private String apiName;

    @Schema(description = "创建用户ID")
    private Long createUserId;

    @Schema(description = "创建用户名称")
    private String createUserName;

    @Schema(description = "受理用户ID")
    private Long acceptUserId;

    @Schema(description = "受理用户名称")
    private String acceptUserName;

    @Schema(description = "工单描述")
    private String description;

    @Schema(description = "请求JSON")
    private String requestJson;

    @Schema(description = "响应JSON")
    private String responseJson;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "完成时间")
    private Date finishTime;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
