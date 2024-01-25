package com.dsj.csp.manage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AbilityAuditVO {
    @Schema(description = "能力ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityId;

    @Schema(description = "审核状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private int flag;

    @Schema(description = "备注说明")
    private String note;
}
