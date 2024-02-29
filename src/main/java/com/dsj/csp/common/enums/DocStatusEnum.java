package com.dsj.csp.common.enums;

import lombok.Getter;

/**
 * 文档状态
 *
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-29
 */
@Getter
public enum DocStatusEnum {
    WAIT_AUDIT(0, "待审核"),
    PASSED(1, "审核通过未发布"),
    NOT_PASSED(2, "审核未通过"),
    PUBLISHED(3, "己发布"),
    OFFLINE(5, "已下线"),
    NOT_SUBMIT(6, "未提交");

    DocStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;

    private final String desc;

    // 返回code对应枚举类型
    public static DocStatusEnum of(Integer code) {
        if (null == code) {
            return null;
        }
        for (DocStatusEnum status : DocStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
