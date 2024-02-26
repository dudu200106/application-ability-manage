package com.dsj.csp.common.aop.aspect;

import com.dsj.csp.manage.util.IdentifyUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 声明式检验请求的Token是否有效
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-23
 */
@Aspect
@Component
public class LoginAuthenticateAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(com.dsj.csp.common.aop.annotation.LoginAuthentication)")
    public void getLoginAuthenticateAspect() {
    }

    @Before("getLoginAuthenticateAspect()")
    public void doBefore(JoinPoint point) throws Throwable {
        try {
            IdentifyUser.getUserInfo();
        } catch (Throwable throwable) {
//            //获取response
//            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
//            HttpServletResponse response = servletRequestAttributes.getResponse();
//            // 重定向回首页
//            httpServletResponse.sendRedirect("/");
            throw throwable;
        }
    }

}
