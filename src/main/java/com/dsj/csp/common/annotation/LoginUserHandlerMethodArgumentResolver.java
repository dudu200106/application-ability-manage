package com.dsj.csp.common.annotation;

import com.dsj.csp.manage.service.UserApproveService;
import jakarta.annotation.Resource;
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
        return parameter.hasParameterAnnotation(CurrentUserToken.class) &&
                parameter.getParameterType().isAssignableFrom(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        // header中获取用户token
        String token = webRequest.getHeader("Authorization");
        // TODO 根据userId获取User信息，这里省略，直接创建一个User对象。
        return userApproveService.identify(token);
    }

}
