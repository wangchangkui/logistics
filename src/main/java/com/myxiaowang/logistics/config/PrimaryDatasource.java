package com.myxiaowang.logistics.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年03月08日 21:14:00
 */

@Primary
@Component
public class PrimaryDatasource extends AbstractRoutingDataSource implements DataSource, InitializingBean {
    private final static String TWO="TWO";
    /**
     * 默认数据源是1
     */
    public static ThreadLocal<String> flag=new ThreadLocal<>();

    @Autowired
    @Qualifier("datasource_one")
    private DataSource dataSourceOne;

    @Autowired
    @Qualifier("datasource_two")
    private DataSource dataSourceTwo;


    @Override
    protected Object determineCurrentLookupKey() {
        // 相当于吧我现在ThreadLocal内的关键字传给routing 让他去判断
        return flag.get();
    }

    @Override
    public void afterPropertiesSet()  {
        flag.set("ONE");
        HashMap<Object, Object> datasource = new HashMap<>(2);
        datasource.put("ONE",dataSourceOne);
        datasource.put("TWO",dataSourceTwo);
        // 所有的数据源
        super.setTargetDataSources(datasource);
        // 默认的数据源
        super.setDefaultTargetDataSource(dataSourceOne);
        super.afterPropertiesSet();
    }
}
