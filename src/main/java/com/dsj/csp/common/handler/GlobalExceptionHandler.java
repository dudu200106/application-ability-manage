package com.dsj.csp.common.handler;

import com.dsj.common.dto.Result;
import com.dsj.csp.common.enums.CodeEnum;
import com.dsj.csp.common.exception.FlowException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 统一异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    //处理业务异常
    @ExceptionHandler(FlowException.class)
    public Result defaultExceptionHandler(HttpServletRequest req, HttpServletResponse resp, FlowException e){
        Result baseResult = new Result(e.getCode(),false,e.getMessage(),null);
        return baseResult;
    }

    //处理系统异常
    @ExceptionHandler(Exception.class)
    public Result systemExceptionHandler(HttpServletRequest req, HttpServletResponse resp, Exception e){
        e.printStackTrace();
        Result result = new Result(CodeEnum.SYSTEM_ERROR.getCode(),false,CodeEnum.SYSTEM_ERROR.getMessage(), null);
        return result;
    }

}
