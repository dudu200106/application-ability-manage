package com.dsj.csp.manage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AbilityApplyAuditVO {
    @Schema(description = "NLSQ_XH")
    private Long abilityApplyId;

    @Schema(description = "ZT")
    private int flag;

    @Schema(description = "BZ")
    private String note;
}
