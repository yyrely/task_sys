package com.chuncongcong.task.config.filter;


import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token,token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
        filterChain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}

