package com.dsj.csp.common.aop.annotation;

import com.dsj.csp.common.enums.LogEnum;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @author Du Shun Chang
 * @version 1.0
 * @date 2024/2/4 9:32
 * @Todo:
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AopLogger {
    String describe() default "";

    //    String logType() default "2";
    LogEnum logType();

//    Integer operateType();


    LogEnum operateType();
}