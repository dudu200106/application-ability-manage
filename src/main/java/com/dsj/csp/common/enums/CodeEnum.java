package com.dsj.csp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码枚举
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {
    //正常
    SUCCESS(200,"OK"),
    //业务异常
    SYSTEM_ERROR(500,"系统异常"),
    //业务异常
    TOKEN_ERROR(601,"登录状态过期,请重新登录"),
    //重复提交异常
    APPROVE_ERROR(602,"正在审核中，不可重复提交实名认证申请"),
    //更新异常
    UPDATE_ERROR(603,"更新失败"),
    APPNAME(611,"应用名已存在"),
    Img_ERROR(612,"图片已破损,请重新上传")

    ;
    private final Integer code;
    private final String message;
}
