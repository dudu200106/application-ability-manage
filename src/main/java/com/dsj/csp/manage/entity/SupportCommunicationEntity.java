package com.dsj.csp.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dsj.csp.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Quasar
 * @version 1.0.0
 * @since 2024/1/3 11:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("MANAGE_SUPPORT_COMMUNICATION")
public class SupportCommunicationEntity extends BaseEntity {
    @TableId(value = "COMMUNICATION_ID", type = IdType.AUTO)
    private Long communicationId;

    @TableField("SUPPORT_ID")
    private Long supportId;

    @TableField("APP_ID")
    private Long appId;

    @TableField("SENDER_ID")
    private Long senderId;

    @TableField("SENDER_NAME")
    private String senderName;

    @TableField("RECEIVER_ID")
    private Long receiverId;

    @TableField("RECEIVER_NAME")
    private String receiverName;

    @TableField("CONTENT")
    private String content;
}
