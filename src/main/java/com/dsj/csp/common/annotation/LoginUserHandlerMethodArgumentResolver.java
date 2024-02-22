package com.dsj.csp.common.annotation;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.dsj.csp.manage.dto.request.UserApproveRequest;
import com.dsj.csp.manage.service.UserApproveService;
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
        LoginUserToken annotation = parameter.getParameterAnnotation(LoginUserToken.class);
        // header中获取用户token
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = request.getHeader(annotation.value());
        if (StringUtils.isEmpty(token)){
            // 一般来说请求头中都会在Accesstoken和Authorization中包含token，取其一就行
            token = request.getHeader("Authorization");
        }
        return userApproveService.identify(token);
    }

}
