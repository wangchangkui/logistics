package com.ruoyi.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author Jandmla
 * @version 1.0
 * @description TODO
 * @data 2022/3/21 16:40
 */
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator{
//    /**
//     * 修改握手,就是在握手协议建立之前修改其中携带的内容
//     * @param sec
//     * @param request
//     * @param response
//     */
//    @Override
//    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
//
//        sec.getUserProperties().put("user", ShiroKit.getUser());
//        //sec.getUserProperties().put("name", "wb");
//        super.modifyHandshake(sec, request, response);
//    }
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        //这个对象说一下，貌似只有服务器是tomcat的时候才需要配置,具体我没有研究
        return new ServerEndpointExporter();
    }
}
