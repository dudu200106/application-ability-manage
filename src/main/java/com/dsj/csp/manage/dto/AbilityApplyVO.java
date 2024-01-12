package com.dsj.csp.manage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 能力申请试用VO
 *
 * @author Sean Du
 * @version 1.0.0
 * @date 2024/01/11
 */
@Data
@Schema(description = "能力申请VO")
public class AbilityApplyVO implements Serializable {

    @Schema(description = "能力ID")
    private Long abilityId;

    @Schema(description = "应用ID")
    private Long appId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "能力名称")
    private String abilityName;

    @Schema(description = "能力类型")
    private String abilityType;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "申请使用的接口列表")
    private String apiIds;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "政府名称")
    private String govName;

    @Schema(description = "申请说明")
    private String illustrate;
}
