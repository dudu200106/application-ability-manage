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
    //登录信息异常
    TOKEN_ERROR(601,"登录状态过期,请重新登录"),
    //重复提交异常
    APPROVE_ERROR(602,"正在审核中，不可重复提交实名认证申请"),
    //更新异常
    UPDATE_ERROR(603,"更新失败"),
    APPNAME(611,"应用名已存在"),
    Img_ERROR(612,"图片已破损,请重新上传"),
    //数据格式异常
    BIND_ERROR(650,"数据格式异常"),
    MISSING_SERVLET_REQUEST_PARAMETER_ERROR(651,"缺少必要参数"),
    METHOD_ARGUMENT_NOT_VALID_ERROR(652,"数据验证未通过"),
//    REDIS_ERROR(653,"redis链接异常"),
    HTTP_MESSAGE_NOT_READABLE_ERROR(654,"参数错误"),
    ILLEGAL_ARGUEMENT_ERROR(655,"非法参数"),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR(656,"入参异常"),
    DUPLICATE_KEY_ERROR(657,"服务器异常,数据库数据异常"),
    SPEL_EVALUATION_ERROR(658,"服务器异常"),
    NULL_POINTER_ERROR(659,"空数据异常"),
    REMOTE_ERROR(660,"远程服务调用异常"),
    ILLEGAL_STATE_ERROR(661,"请求参数不正确"),
    METHOD_ARGUMENT_TYPE_MISMATCH_ERROR(662,"请求参数不正确"),
    UNSUPPORTED_ENCODING_ERROR(653,"账号异常，存在非正常编码字符"),
    RUNTIME_ERROR(654,"运行异常，请联系管理员处理"),
    DM_ERROR(655,"数据字段过长"),
    //业务异常
    SYSTEM_ERROR(500,"系统异常")
    ;
    private final Integer code;
    private final String message;
}
