package com.dsj.csp.manage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/26 16:07
 */
@Data
public class SupportQueryRequest implements Serializable {
    @Schema(description = "页码", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "1")
    private Long current = 1L;

    @Schema(description = "每页数量", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "10")
    private Long size = 10L;

    @Schema(description = "能力名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String abilityName;

    @Schema(description = "API名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String apiName;

    @Schema(description = "工单状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;

    @Schema(description = "工单创建起始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date createTimeBegin;

    @Schema(description = "工单创建结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date createTimeEnd;

    @Schema(description = "工单完成起始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date finishTimeBegin;

    @Schema(description = "工单完成结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date finishTimeEnd;
}
