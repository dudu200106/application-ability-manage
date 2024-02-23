package com.dsj.csp.common.filter;

import com.dsj.common.dto.Result;
import com.dsj.csp.common.exception.FlowException;
import com.dsj.csp.manage.util.IdentifyUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author SeanDu
 * @version 1.0.0
 * @date 2024-02-21
 */
public class TokenValidationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //检查请求中的Token是否有效，是否包含正确的权限等
        //如果Token验证通过，继续请求链的执行; 验证失败，可以返回适当的错误响应或进行重定向
        try{
            // Token验证通过，继续请求链的执行
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String token = request.getHeader("Authorization");
            String method = request.getMethod();
            // 放行未登录的查询请求,不会修改数据(注意可能存在GET方式的修改操作!)
            if (token==null && method.equals("GET")){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            // 调用方法从request的请求头中解析、获取用户Token信息
            IdentifyUser.getUserInfo();
            filterChain.doFilter(servletRequest, servletResponse);
        }catch (FlowException e){
            // Token验证失败，返回错误响应或重定向到登录页面
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            Result result = Result.failed(-1, e.getMessage());
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(result));
            // 重定向回首页
//            httpServletResponse.sendRedirect("/");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
