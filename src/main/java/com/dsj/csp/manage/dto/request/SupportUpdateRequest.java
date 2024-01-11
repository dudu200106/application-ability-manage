package com.dsj.csp.manage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/26 16:07
 */
@Data
public class SupportUpdateRequest implements Serializable {
    @Schema(description = "工单标题", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String title;

    @Schema(description = "工单状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;
}
