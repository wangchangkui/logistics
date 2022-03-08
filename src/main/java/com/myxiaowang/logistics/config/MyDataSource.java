package com.myxiaowang.logistics.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年03月08日 20:10:00
 */
@Configuration
public class MyDataSource {

//    @Primary
//    @Bean(name = "dataSource")
//    public DataSource dataSource() {
//        return new DruidDataSource();
//    }

    @ConfigurationProperties(prefix = "spring.druid.datasource-primary.datasource1")
    @Bean(name = "datasource_one")
    public DataSource getDbOne(){
        DruidDataSource build = DruidDataSourceBuilder.create().build();
        build.setMaxActive(10);
        build.setKeepAlive(true);
        build.setKillWhenSocketReadTimeout(true);
        return build;
    }

    @ConfigurationProperties(prefix = "spring.druid.datasource-primary.datasource2")
    @Bean(name = "datasource_two")
    public DataSource getDbTwo(){
        return DruidDataSourceBuilder.create().build();
    }

}

