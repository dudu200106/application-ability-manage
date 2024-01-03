package com.dsj.csp.manage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/26 16:01
 */
@Data
public class SupportReplyRequest implements Serializable {
    @Schema(description = "回复用户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long replyUserId;

    @Schema(description = "回复用户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String replyUserName;

    @Schema(description = "回复内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "最后一条消息ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long lastCommunicationId;
}
