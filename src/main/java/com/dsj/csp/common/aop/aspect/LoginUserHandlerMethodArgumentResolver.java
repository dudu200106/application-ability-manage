package com.dsj.csp.common.aop.aspect;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dsj.csp.common.aop.annotation.LoginUserToken;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.service.UserApproveService;
import com.dsj.csp.manage.util.IdentifyUser;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-21
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Resource
    private UserApproveService userApproveService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUserToken.class) &&
                parameter.getParameterType().isAssignableFrom(UserApproveRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
//        LoginUserToken annotation = parameter.getParameterAnnotation(LoginUserToken.class);
//        // header中获取用户token
//        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//        String token = request.getHeader(annotation.value());
        return IdentifyUser.getUserInfo();
    }

}