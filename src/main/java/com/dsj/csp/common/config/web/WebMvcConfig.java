package com.dsj.csp.common.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 注册自定义的HandleMethodArgumentResolver
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-21
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    /*注册该过滤器入容器*/
//    @Bean
//    public FilterRegistrationBean<Filter> orderFilter() {
//        // 利用工厂给容器外的对象注入所需组件
//        TokenValidationFilter filterBean = new TokenValidationFilter();
//        applicationContext.getAutowireCapableBeanFactory().autowireBean(filterBean);
//
//        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();
//        filter.setName("TokenValidationFilter");
//        filter.setUrlPatterns(Arrays.asList("/doc/*"));
//        filter.setFilter(filterBean);//指定优先级
//        filter.setOrder(-1);
//        return filter;
//    }

}
