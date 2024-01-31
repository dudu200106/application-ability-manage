package com.dsj.csp.manage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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

    @Schema(description = "能力ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long abilityId;

    @Schema(description = "能力名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @Length(max= 30,message="编码长度不能超过30")
    private String abilityName;

    @Schema(description = "申请状态")
    private Integer status;

    @Schema(description = "应用ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long appId;

    @Schema(description = "用户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    @Schema(description = "申请使用的接口列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apiIds;

    @Schema(description = "申请说明")
    @Length(max= 300,message="编码长度不能超过300")
    private String illustrate;
}
