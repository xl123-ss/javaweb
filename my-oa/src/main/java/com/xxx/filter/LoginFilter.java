package com.xxx.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request2 = (HttpServletRequest)request;
        HttpServletResponse response2 = (HttpServletResponse)response;
        HttpSession session = request2.getSession();
        Object name = session.getAttribute("name");

        String uri = request2.getRequestURI();
        System.out.println(uri);
        String[] pers = {"login.html","/"};
        for (String per : pers) {
            if (uri.contains(per)){
                chain.doFilter(request,response);//放行
            }
        }
        System.out.println(name);
        //如果名字为空
        if (name == null){
            if (uri.contains(".html")){
                request.getRequestDispatcher("/").forward(request,response);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
