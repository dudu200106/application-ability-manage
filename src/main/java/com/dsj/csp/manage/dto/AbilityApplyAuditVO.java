package com.dsj.csp.manage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AbilityApplyAuditVO {
    @Schema(description = "NLSQ_XH")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityApplyId;

    @Schema(description = "ZT")
    private int flag;

    @Schema(description = "BZ")
    private String note;
}
