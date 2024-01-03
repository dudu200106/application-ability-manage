package com.dsj.csp.manage.dto.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/26 16:01
 */
@Data
public class SupportReplyRequest implements Serializable {
    private Long supportId;

    private Long replyUserId;

    private String replyUserName;

    private String content;

    private Long lastCommunicationId;
}
