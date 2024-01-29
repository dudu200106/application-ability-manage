package com.dsj.csp.common.exception;

import com.dsj.csp.common.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * 自定义业务异常
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowException extends RuntimeException implements Serializable {
    //状态码（成功：200   失败：其他）
    private Integer code;
    //异常信息
    private String message;

    public FlowException(CodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }
}
