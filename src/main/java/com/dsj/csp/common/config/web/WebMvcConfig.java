package com.dsj.csp.common.config.web;

import com.dsj.csp.common.aop.aspect.LoginUserHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;

/**
 * 注册自定义的HandleMethodArgumentResolver
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-21
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        LoginUserHandlerMethodArgumentResolver resolver = new LoginUserHandlerMethodArgumentResolver();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(resolver);
        resolvers.add(resolver);
    }

}
