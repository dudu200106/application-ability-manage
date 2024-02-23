package com.dsj.csp.common.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解析注解的方法参数，获取当前请求用户token信息实例，注入UserApproveRequest实例
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-21
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUserToken {
//    String value() default "Authorization";
}
