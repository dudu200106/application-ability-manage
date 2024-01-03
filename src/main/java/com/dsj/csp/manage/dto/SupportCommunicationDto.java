package com.dsj.csp.manage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2024/1/3 11:01
 */
@Data
@Accessors(chain = true)
public class SupportCommunicationDto implements Serializable {
    private Long communicationId;

    private Long senderUserId;

    private String senderUserName;

    private Long receiverUserId;

    private String receiverUserName;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
