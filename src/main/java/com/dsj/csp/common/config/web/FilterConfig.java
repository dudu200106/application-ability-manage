package com.dsj.csp.common.config.web;

import com.dsj.csp.common.filter.TokenValidationFilter;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-21
 */
@Configuration
public class FilterConfig {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public TokenValidationFilter getTokenValidationFilter() {
        // 将未纳入容器的Filter Bean纳入容器
        return new TokenValidationFilter();
    }

    @Bean
    public FilterRegistrationBean<Filter> orderFilter() {
        // 利用工厂给容器外的对象注入所需组件
//        TokenValidationFilter filterBean = new TokenValidationFilter();
//        applicationContext.getAutowireCapableBeanFactory().autowireBean(filterBean);

        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();
        filter.setName("TokenValidationFilter");
        filter.setUrlPatterns(Arrays.asList("/doc/*"));
        filter.setFilter(getTokenValidationFilter());//指定优先级
        filter.setOrder(-1);
        return filter;
    }
}
