package com.dsj.csp.common.enums;

import lombok.Getter;

/**
 * 接口状态
 *
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-29
 */
@Getter
public enum ApiStatusEnum {
    NOT_SUBMIT(0, "未提交"),
    WAIT_AUDIT(1, "待审核"),
    NOT_PASSED(2, "审核未通过"),
    PASSED(3, "审核通过未发布"),
    PUBLISHED(4, "己发布"),
    OFFLINE(5, "已下线");

    ApiStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;

    private final String desc;

    // 返回code对应枚举类型
    public static ApiStatusEnum of(Integer code) {
        if (null == code) {
            return null;
        }
        for (ApiStatusEnum status : ApiStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
