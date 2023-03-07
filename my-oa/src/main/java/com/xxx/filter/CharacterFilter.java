package com.xxx.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter("/*")
public class CharacterFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取参数乱码解决，要放在第一行
        request.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        chain.doFilter(request,response);//放行
    }

    @Override
    public void destroy() {

    }
}
