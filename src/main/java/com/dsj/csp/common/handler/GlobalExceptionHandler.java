package com.dsj.csp.common.handler;

import com.dsj.common.dto.Result;
import com.dsj.csp.common.enums.CodeEnum;
import com.dsj.csp.common.exception.FlowException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
        Result result = new Result(e.getCode(),false,e.getMessage(),null);
        return result;
    }

    //处理数据格式异常
    @ExceptionHandler(BindException.class)
    public Result bind(HttpServletRequest req, HttpServletResponse resp, BindException e){
        Result result = new Result(CodeEnum.BIND_ERROR.getCode(),false,CodeEnum.BIND_ERROR.getMessage(), null);
        return result;
    }

    //缺少参数异常
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result missingServletRequestParameter(HttpServletRequest req, HttpServletResponse resp, MissingServletRequestParameterException e){
        Result result = new Result(CodeEnum.MISSING_SERVLET_REQUEST_PARAMETER_ERROR.getCode(),false,CodeEnum.MISSING_SERVLET_REQUEST_PARAMETER_ERROR.getMessage(), null);
        return result;
    }

    //参数错误异常
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result httpMessageNotReadable(HttpServletRequest req, HttpServletResponse resp, HttpMessageNotReadableException e){
        Result result = new Result(CodeEnum.HTTP_MESSAGE_NOT_READABLE_ERROR.getCode(),false,CodeEnum.HTTP_MESSAGE_NOT_READABLE_ERROR.getMessage(), null);
        return result;
    }

    //非法参数异常
    @ExceptionHandler(IllegalArgumentException.class)
    public Result illegalArgument(HttpServletRequest req, HttpServletResponse resp, IllegalArgumentException e){
        Result result = new Result(CodeEnum.ILLEGAL_ARGUEMENT_ERROR.getCode(),false,CodeEnum.ILLEGAL_ARGUEMENT_ERROR.getMessage(), null);
        return result;
    }

    //入参异常
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result httpMediaTypeNotSupported(HttpServletRequest req, HttpServletResponse resp, HttpMediaTypeNotSupportedException e){
        Result result = new Result(CodeEnum.HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR.getCode(),false,CodeEnum.HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR.getMessage(), null);
        return result;
    }

    //入参异常
    @ExceptionHandler(IllegalStateException.class)
    public Result illegalState(HttpServletRequest req, HttpServletResponse resp, IllegalStateException e){
        Result result = new Result(CodeEnum.ILLEGAL_STATE_ERROR.getCode(),false,CodeEnum.ILLEGAL_STATE_ERROR.getMessage(), null);
        return result;
    }

    //处理系统异常
    @ExceptionHandler(Exception.class)
    public Result systemExceptionHandler(HttpServletRequest req, HttpServletResponse resp, Exception e){
        e.printStackTrace();
        Result result = new Result(CodeEnum.SYSTEM_ERROR.getCode(),false,CodeEnum.SYSTEM_ERROR.getMessage(), null);
        return result;
    }
}
