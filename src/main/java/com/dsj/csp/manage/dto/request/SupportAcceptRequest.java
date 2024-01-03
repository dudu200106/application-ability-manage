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
public class SupportAcceptRequest implements Serializable {
    @Schema(description = "受理用户ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long acceptUserId;

    @Schema(description = "受理用户名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String acceptUserName;
}
