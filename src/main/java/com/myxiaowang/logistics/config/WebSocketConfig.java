package com.myxiaowang.logistics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Myxiaowang
 */
@Configuration
public class WebSocketConfig {
  /**
   * 注入serverEndpointExporter对象bean，自动使用@ServerEndpoint注解声明webscoket endpoint
   */
  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }
}