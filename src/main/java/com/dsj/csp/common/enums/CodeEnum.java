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
    //登录信息异常
    TOKEN_ERROR(601,"登录状态过期,请重新登录"),
    //重复提交异常
    APPROVE_ERROR(602,"正在审核中，不可重复提交实名认证申请"),
    //更新异常
    UPDATE_ERROR(603,"更新失败"),
    //数据格式异常
    BIND_ERROR(650,"数据格式异常"),
    MISSING_SERVLET_REQUEST_PARAMETER_ERROR(651,"缺少必要参数"),
    HTTP_MESSAGE_NOT_READABLE_ERROR(652,"参数错误"),
    ILLEGAL_ARGUEMENT_ERROR(652,"非法参数"),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR(653,"入参异常"),
    ILLEGAL_STATE_ERROR(654,"请求参数不正确"),
    ;
    private final Integer code;
    private final String message;
}
