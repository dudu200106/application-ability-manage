package com.dsj.csp.common.enums;

import lombok.Getter;

/**
 * 接口申请状态
 *
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-29
 */
@Getter
public enum ApplyStatusEnum {
    NOT_SUBMIT(0, "未提交"),
    WAIT_AUDIT(1, "待审核"),
    PASSED(2, "审核通过"),
    NOT_PASSED(3, "审核不通过"),
    STOPPED(4, "已停用");

    ApplyStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;

    private final String desc;

    // 返回code对应枚举类型
    public static ApplyStatusEnum of(Integer code) {
        if (null == code) {
            return null;
        }
        for (ApplyStatusEnum status : ApplyStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
