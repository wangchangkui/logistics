package com.myxiaowang.logistics.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年11月22日 10:05:00
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {



    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxRequestSize(DataSize.ofBytes(1073741824L));
        factory.setMaxFileSize(DataSize.ofBytes(1073741824L));
        return factory.createMultipartConfig();
    }

    /**
     * 解决跨域问题
     * @return CorsFilter
     */
    @Bean
    public CorsFilter corsFilter(){
        return new CorsFilter(corsConfiguration());
    }

    public UrlBasedCorsConfigurationSource corsConfiguration(){
        // 添加配置
        CorsConfiguration corsConfiguration=new CorsConfiguration();
        // 设置请求头
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        // 添加请求路径
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }
}
