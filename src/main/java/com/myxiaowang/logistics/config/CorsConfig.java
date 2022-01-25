package com.myxiaowang.logistics.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 解决跨域的问题2
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年11月29日 15:39:00
 */


@WebFilter(value = "/*",filterName = "myCors")
@Component
public class CorsConfig implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest rep = (HttpServletRequest) servletRequest;
        resp.addHeader("Access-Control-Allow-Origin", rep.getHeader("Origin"));
        //允许跨域请求中携带cookie
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        // 如果存在自定义的header参数，需要在此处添加，逗号分隔
        resp.addHeader("Access-Control-Allow-Headers", "authorization,Origin, No-Cache, X-Requested-With, "
                + "If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, " + "Content-Type, X-E4M-With");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        filterChain.doFilter(rep, resp);
    }

    @Override
    public void destroy() {

    }
}
