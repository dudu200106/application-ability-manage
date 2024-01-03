package com.dsj.csp.manage.entity.constant;

import lombok.Getter;

/**
 * 工单状态
 *
 * @author Quasar
 * @version 1.0.0
 * @since 2023/12/27 14:44
 */
@Getter
public enum SupportStatus {
    SUBMITTED(0, "待处理"),
    PROCESSING(1, "处理中"),
    FINISHED(2, "已完成"),
    CLOSED(3, "已关闭");

    SupportStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;

    private final String desc;

    public static SupportStatus of(Integer code) {
        if (null == code) {
            return null;
        }
        for (SupportStatus status : SupportStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
