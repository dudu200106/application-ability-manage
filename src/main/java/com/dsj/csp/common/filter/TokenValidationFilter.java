package com.dsj.csp.common.filter;


import com.dsj.common.dto.Result;
import com.dsj.csp.common.exception.FlowException;
import com.dsj.csp.manage.service.UserApproveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-21
 */
public class TokenValidationFilter implements Filter {

    @Autowired
    private UserApproveService userApproveService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //在这里进行Token验证的逻辑
        //检查请求中的Token是否有效，是否包含正确的权限等
        //如果Token验证通过，继续请求链的执行
        //如果Token验证失败，可以返回适当的错误响应或进行重定向
        //例如，假设您在请求头中传递了一个名为"Authorization"的Token
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = httpServletRequest.getHeader( "Authorization");
        try{
            // Token验证通过，继续请求链的执行
            userApproveService.identify(token);
            filterChain.doFilter(servletRequest, servletResponse);
        }catch (FlowException e){
            //Token验证失败，返回错误响应或重定向到登录页面
            Result result = Result.failed(e.getCode(), e.getMessage());
            servletResponse.setContentType("application/json");
            servletResponse.getWriter().write(new ObjectMapper().writeValueAsString(result));
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
