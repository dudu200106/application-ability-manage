package com.dsj.csp.common.config.web;

import com.dsj.common.dto.BusinessException;
import com.dsj.common.dto.Result;
import com.dsj.csp.common.enums.CodeEnum;
import com.dsj.csp.common.exception.FlowException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Objects;

@RestControllerAdvice
public class GenericExceptionHandler {

    /**
     * 业务异常包装处理
     *
     * @param e 业务异常
     * @return Result
     */
    @ExceptionHandler(BusinessException.class)
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
    public Result<?> bindExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Objects.requireNonNull(bindingResult.getFieldError());
        return Result.failed("传入参数不规范：" +
                bindingResult.getFieldError().getField() + "，" + bindingResult.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(FlowException.class)
    public Result defaultExceptionHandler(HttpServletRequest req, HttpServletResponse resp, FlowException e){
        Result result = new Result(e.getCode(),false,e.getMessage(),null);
        return result;
    }

    /**
     * 处理数据格式异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result bind(HttpServletRequest req, HttpServletResponse resp, BindException e){
        Result result = new Result(CodeEnum.BIND_ERROR.getCode(),false,CodeEnum.BIND_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 缺少参数异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result missingServletRequestParameter(HttpServletRequest req, HttpServletResponse resp, MissingServletRequestParameterException e){
        Result result = new Result(CodeEnum.MISSING_SERVLET_REQUEST_PARAMETER_ERROR.getCode(),false,CodeEnum.MISSING_SERVLET_REQUEST_PARAMETER_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 参数错误异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result httpMessageNotReadable(HttpServletRequest req, HttpServletResponse resp, HttpMessageNotReadableException e){
        Result result = new Result(CodeEnum.HTTP_MESSAGE_NOT_READABLE_ERROR.getCode(),false,CodeEnum.HTTP_MESSAGE_NOT_READABLE_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 非法参数异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result illegalArgument(HttpServletRequest req, HttpServletResponse resp, IllegalArgumentException e){
        Result result = new Result(CodeEnum.ILLEGAL_ARGUEMENT_ERROR.getCode(),false,CodeEnum.ILLEGAL_ARGUEMENT_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 入参异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result httpMediaTypeNotSupported(HttpServletRequest req, HttpServletResponse resp, HttpMediaTypeNotSupportedException e){
        Result result = new Result(CodeEnum.HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR.getCode(),false,CodeEnum.HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 服务器异常,数据库数据异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result duplicateKey(HttpServletRequest req, HttpServletResponse resp, DuplicateKeyException e){
        Result result = new Result(CodeEnum.DUPLICATE_KEY_ERROR.getCode(),false,CodeEnum.DUPLICATE_KEY_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 服务器异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(SpelEvaluationException.class)
    public Result spelEvaluation(HttpServletRequest req, HttpServletResponse resp, SpelEvaluationException e){
        Result result = new Result(CodeEnum.SPEL_EVALUATION_ERROR.getCode(),false,CodeEnum.SPEL_EVALUATION_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 服务器异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public Result nullPointer(HttpServletRequest req, HttpServletResponse resp, NullPointerException e){
        Result result = new Result(CodeEnum.NULL_POINTER_ERROR.getCode(),false,CodeEnum.NULL_POINTER_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 服远程调用异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(RemoteException.class)
    public Result remoteError(HttpServletRequest req, HttpServletResponse resp, RemoteException e){
        Result result = new Result(CodeEnum.REMOTE_ERROR.getCode(),false,CodeEnum.REMOTE_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 入参异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(IllegalStateException.class)
    public Result illegalState(HttpServletRequest req, HttpServletResponse resp, IllegalStateException e){
        Result result = new Result(CodeEnum.ILLEGAL_STATE_ERROR.getCode(),false,CodeEnum.ILLEGAL_STATE_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 入参异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result methodArgumentTypeMismatch(HttpServletRequest req, HttpServletResponse resp, MethodArgumentTypeMismatchException e){
        Result result = new Result(CodeEnum.METHOD_ARGUMENT_TYPE_MISMATCH_ERROR.getCode(),false,CodeEnum.METHOD_ARGUMENT_TYPE_MISMATCH_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 入参异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(UnsupportedEncodingException.class)
    public Result unsupportedEncoding(HttpServletRequest req, HttpServletResponse resp, UnsupportedEncodingException e){
        Result result = new Result(CodeEnum.UNSUPPORTED_ENCODING_ERROR.getCode(),false,CodeEnum.UNSUPPORTED_ENCODING_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 运行异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public Result runtimeError(HttpServletRequest req, HttpServletResponse resp, RuntimeException e){
        Result result = new Result(CodeEnum.RUNTIME_ERROR.getCode(),false,CodeEnum.RUNTIME_ERROR.getMessage(), null);
        return result;
    }

    /**
     * 处理系统异常
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result systemExceptionHandler(HttpServletRequest req, HttpServletResponse resp, Exception e){
        e.printStackTrace();
        Result result = new Result(CodeEnum.SYSTEM_ERROR.getCode(),false,CodeEnum.SYSTEM_ERROR.getMessage(), null);
        return result;
    }

}
