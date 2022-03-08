package com.myxiaowang.logistics.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年03月08日 20:22:00
 */
@Configuration
public class DruidConfig {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // 白名单
        Map<String, String> initParameters = new HashMap<>(16);
        // 禁用HTML页面上的“REST ALL”功能
        initParameters.put("resetEnable", "false");
        // IP白名单（没有配置或者为空，则允许所有访问）
        initParameters.put("/druid/*", "");
        // ip黑名单
        initParameters.put("deny", "");
        // 监控页面登录用户名
        initParameters.put("loginUsername", "admin");
        // 监控页面登录用户密码
        initParameters.put("loginPassword", "admin");
        registrationBean.setInitParameters(initParameters);
        return registrationBean;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // 过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        // 忽略过滤格式
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
