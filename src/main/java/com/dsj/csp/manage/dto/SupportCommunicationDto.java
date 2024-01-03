package com.dsj.csp.manage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "工单沟通记录ID")
    private Long communicationId;

    @Schema(description = "发送用户ID")
    private Long senderUserId;

    @Schema(description = "发送用户名称")
    private String senderUserName;

    @Schema(description = "接收用户ID")
    private Long receiverUserId;

    @Schema(description = "接收用户名称")
    private String receiverUserName;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "创建时间")
    private Date createTime;
}
