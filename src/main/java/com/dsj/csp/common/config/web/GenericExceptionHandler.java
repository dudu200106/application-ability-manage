package com.dsj.csp.common.config.web;

import com.dsj.common.dto.BusinessException;
import com.dsj.common.dto.Result;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@ControllerAdvice
public class GenericExceptionHandler {

    /**
     * 业务异常包装处理
     *
     * @param e 业务异常
     * @return Result
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result<?> businessExceptionHandler(BusinessException e) {
        return Result.failed(e.getMessage());
    }

    /**
     * 请求头参数校验异常包装处理，
     * 一般是 @Valid 或 @Validated 注解校验失败
     *
     * @param e 请求头参数校验异常
     * @return Result
     * @see Valid
     * @see Validated
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<?> bindExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Objects.requireNonNull(bindingResult.getFieldError());
        return Result.failed("传入参数不规范：" +
                bindingResult.getFieldError().getField() + "，" + bindingResult.getFieldError().getDefaultMessage());
    }
}
