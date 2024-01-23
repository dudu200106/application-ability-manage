package com.dsj.csp.manage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AbilityAuditVO {
    @Schema(description = "NL_XH")
    private Long abilityId;

    @Schema(description = "ZT")
    private int flag;

    @Schema(description = "BZ")
    private String note;
}
