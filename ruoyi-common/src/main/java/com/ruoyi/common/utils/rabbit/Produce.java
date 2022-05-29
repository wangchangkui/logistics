package com.ruoyi.common.utils.rabbit;

import com.ruoyi.common.config.PropertiesConfig;
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
   private final Logger logger = LoggerFactory.getLogger(Produce.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PropertiesConfig propertiesConfig;

    private final RabbitTemplate.ConfirmCallback confirmCallback = (correlationData, ack, cause) -> {
        logger.info("correlationData : " + correlationData);

        logger.info("ack : " + ack);
        if (!ack) {
            logger.error("ConfirmCallback发生异常....");
        }
    };

    private final RabbitTemplate.ReturnCallback returnCallback = (message, replyCode, replyText, exchange, routingKey) -> {
        logger.info("message : " + message);
        logger.info("replyCode : " + replyCode);
        logger.info("replyText : " + replyText);
        logger.info("exchange : " + exchange);
        logger.info("routingKey : " + routingKey);
    };

    public void sendMsg(Object message, Map<String, Object> properties) {
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message<Object> msg = MessageBuilder.createMessage(message, messageHeaders);
        // 确认回调
        rabbitTemplate.setConfirmCallback(confirmCallback);
        // 消息回调
        rabbitTemplate.setReturnCallback(returnCallback);
        // 附加消息
        CorrelationData correlationData = new CorrelationData("myxiaowang");
        rabbitTemplate.convertAndSend(propertiesConfig.getExchange(), propertiesConfig.getRoute_key(), msg, correlationData);
    }

    public void sendMsg(String message){
        rabbitTemplate.convertAndSend(propertiesConfig.getExchange(), propertiesConfig.getRoute_key(),message);
    }

    public void sendMessage(String exChange,String message){
        rabbitTemplate.setExchange(exChange);
        rabbitTemplate.convertAndSend(message);
    }

}
