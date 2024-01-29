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
    APPNAME(201,"应用名已存在")

    ;
    private final Integer code;
    private final String message;
}
