package com.myxiaowang.logistics.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 这个类是获取所有配置文件的类
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年12月29日 11:13:00
 */
@Configuration
@Data
public class PropertiesConfig {
    @Value("${aliyun.END_POINT}")
    private String END_POINT;
    @Value("${aliyun.ACCESS_KEY_ID}")
    private String ACCESS_KEY_ID;
    @Value("${aliyun.ACCESS_KEY_SECRET}")
    private String ACCESS_KEY_SECRET;
    @Value("${aliyun.PUBLIC_KEY}")
    private String PUBLIC_KEY;
    @Value("${aliyun.MER_PRIVATE_KEY}")
    private String MER_PRIVATE_KEY;
    @Value("${aliyun.APP_ID}")
    private String APP_ID;
    @Value("${spring.rabbitmq.template.routing-key}")
    private String route_key;
    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;
    @Value("${tenxun.SDK_AppID}")
    private String txSkdId;
    @Value("${tenxun.APP_KEY}")
    private String APP_KEY;
    @Value("${tenxun.Template_ID}")
    private String Template_ID;
    @Value("${tenxun.SecrityId}")
    private String tx_Secrity_Id;
    @Value("${tenxun.SecrityKey}")
    private String tx_Secrity_key;
    @Value("${tenxun.APP_ID}")
    private String tx_App_id;
    @Value("${aliyun.BUKKET_NAME}")
    private String BUKKET_NAME;
    @Value("${aliyun.Authorization}")
    private String authentication;
    @Value("${aliyun.AuthUrl}")
    private String authUrl;

}
