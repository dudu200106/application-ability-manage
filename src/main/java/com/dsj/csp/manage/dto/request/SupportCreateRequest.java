package com.dsj.csp.manage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2024/1/3 15:15
 */
@Data
public class SupportCreateRequest implements Serializable {
    @Schema(description = "应用ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long appId;

    @Schema(description = "工单标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "能力ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long abilityId;

    @Schema(description = "能力名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String abilityName;

    @Schema(description = "API ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long apiId;

    @Schema(description = "API名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String apiName;

    @Schema(description = "创建用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long createUserId;

    @Schema(description = "创建用户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createUserName;

    @Schema(description = "工单描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(description = "请求JSON", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String requestJson;

    @Schema(description = "响应JSON", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String responseJson;
}
