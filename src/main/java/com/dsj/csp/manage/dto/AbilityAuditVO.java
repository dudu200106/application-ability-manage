package com.dsj.csp.manage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbilityAuditVO  implements Serializable {
    @Schema(description = "能力申请ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityApplyId;

    @Schema(description = "接口申请ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiApplyId;

    @Schema(description = "能力ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityId;

    @Schema(description = "接口ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long apiId;

    @Schema(description = "审核状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(max= 5, min = 0, message="flag范围在0~5")
    private int flag;

    @Schema(description = "备注说明")
    private String note;
}
