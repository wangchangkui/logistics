package com.myxiaowang.logistics.common.RabbitMq;

import com.myxiaowang.logistics.config.PropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月08日 09:46:00
 */
@Component
public class Produce {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PropertiesConfig propertiesConfig;

    public void sendMsg(String message){
        rabbitTemplate.convertAndSend(propertiesConfig.getExchange(), propertiesConfig.getRoute_key(),message);
    }

    public void sendMessage(String exChange,String message){
        rabbitTemplate.setExchange(exChange);
        rabbitTemplate.convertAndSend(message);
    }

}
