package com.dsj.csp.common.aop.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-23
 */
@Aspect
@Component
public class LoginAuthenticateAspect {
    @Pointcut()
    public void getLoginAuthenticateAspect() {

    }

}
