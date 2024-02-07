package com.dsj.csp.common.aop.aspect;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dsj.csp.common.aop.annotation.AopLogger;
import com.dsj.csp.common.enums.LogEnum;
import com.dsj.csp.manage.entity.LogEntity;
import com.dsj.csp.manage.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Date;

import static com.dsj.csp.manage.util.IP.getIpAddress;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/2/4 9:32
 * @Todo:
 */
@Aspect
@Component
public class AopLoggerAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.dsj.csp.common.aop.annotation.AopLogger)")
    public void aopLoggerAspect() {
    }

    @Autowired
    public LogService logService;

    /**
     * 环绕触发
     *
     * @param point
     * @return
     */
    @Before("aopLoggerAspect()")
    public Object doAround(JoinPoint point) {
        LogEntity logEntity = new LogEntity();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Object result = null;
        long startTime = System.currentTimeMillis();
        try {
            result=point.getSignature();
            System.out.println(result);
//            result = point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            logger.error(throwable.getMessage());
        }
        String describe = getAopLoggerDescribe(point);
//        int operateType=getAopLoggerOperateType(point);
//        System.out.println("操作类型"+operateType);
        LogEnum logType = getAopLoggerLogType(point);
        logEntity.setLogType(logType.getCode());
        System.out.println(logType.getCode());

        LogEnum operateType = getAopLoggerOperateType(point);
        logEntity.setOperateType(operateType.getCode());
        System.out.println(operateType.getCode());
        if (StringUtils.isBlank(describe)) {
            describe = "-";
        }
        // 打印请求相关参数
        logger.info("========================================== Start ==========================================");
        logger.info("Describe       : {}", describe);
        logEntity.setLogContent(describe);

        // 打印请求 url
        logger.info("URL            : {}", request.getRequestURL());
        logEntity.setRequestUrl(String.valueOf(request.getRequestURL()));
        logger.info("URI            : {}", request.getRequestURI());
        // 打印 Http method
        logger.info("HTTP Method    : {}", request.getMethod());
        logEntity.setRequestType(request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        logger.info("Class Method   : {}.{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
        logEntity.setMethod(point.getSignature().getDeclaringTypeName() + point.getSignature().getName());
        // 打印请求的 IP
        logger.info("IP             : {}", request.getRemoteAddr());

        logEntity.setIp(getIpAddress(request));
        // 打印请求入参
        logger.info("Request Args   : {}", point.getArgs());
        // 打印请求出参
        logger.info("Response Args  : {}", result);

        logger.info("Time Consuming : {} ms", System.currentTimeMillis() - startTime);
        logEntity.setCostTime(System.currentTimeMillis() - startTime);
        logger.info("=========================================== End ===========================================");
        //请求的参数

//        Object[] args = point.getArgs();
        //将参数所在的数组转换成json
//        String params = JSON.toJSONString(args);
        // 获取方法参数名和值
        MethodSignature methodSignature = (MethodSignature) point.getSignature();

        String[] parameterNames = methodSignature.getParameterNames();
        Object[] parameterValues = point.getArgs();
        logEntity.setCreateTime(new Date());
        StringBuilder parameters = new StringBuilder();
        for (int i = 0; i < parameterNames.length; i++) {
            parameters.append(parameterNames[i]).append(": ").append(parameterValues[i]);
            if (i < parameterNames.length - 1) {
                parameters.append(", ");
            }
        }
        logEntity.setRequestParam(String.valueOf(parameters));
        // 记录日志
        System.out.println("Method " + methodSignature.getMethod().getName() +
                " called with parameters: " + parameters);
        logEntity.setCreateBy("管理员");
        logEntity.setUpdateBy("管理员");
        logService.save(logEntity);
        return result;
    }

    /**
     * 获取注解中对方法的描述信息
     *
     * @param joinPoint 切点
     * @return describe
     */
    public static String getAopLoggerDescribe(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AopLogger controllerLog = method.getAnnotation(AopLogger.class);
        return controllerLog.describe();
    }
//    public static String getAopLoggerOperateType(JoinPoint joinPoint) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        AopLogger controllerLog = method.getAnnotation(AopLogger.class);
//        return controllerLog.();
//    }

    public static LogEnum getAopLoggerLogType(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AopLogger controllerLog = method.getAnnotation(AopLogger.class);
        return controllerLog.logType();
    }

    public static LogEnum getAopLoggerOperateType(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AopLogger controllerLog = method.getAnnotation(AopLogger.class);
        return controllerLog.operateType();
    }
}
