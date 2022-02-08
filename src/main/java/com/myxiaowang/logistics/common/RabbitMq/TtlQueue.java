package com.myxiaowang.logistics.common.RabbitMq;

import com.myxiaowang.logistics.config.PropertiesConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月08日 09:27:00
 */
@Configuration
@AllArgsConstructor
public class TtlQueue {

    @Autowired
    private PropertiesConfig propertiesConfig;

    /**
     * 延迟交换机
     * @return DirectExchange
     */
    @Bean
    public DirectExchange myTtlExchange(){
        return new DirectExchange(propertiesConfig.getExchange(),true,false);
    }

    /**
     * 死信交换机
     * @return DirectExchange
     */
    @Bean
    public DirectExchange deadLetterExchange(){
        return new DirectExchange("dead_exchange",true,false);
    }
    /**
     * 延迟队列的配置
     * x-message-ttl 消息过期的时间
     * x-dead-letter-exchange 死队列交换机名称
     * x-dead-letter-routing-key 死信队列路由键
     * @return Queue
     */
    @Bean(name = "directQueue")
    public Queue directQueue(){
        Map<String, Object> queue = new HashMap<>(8);
        queue.put("x-message-ttl",120000);
        queue.put("x-dead-letter-exchange","dead_exchange");
        queue.put("x-dead-letter-routing-key","dead");
        return new Queue("ttl.direct.queue",true,false,false,queue);
    }

    /**
     * 死信队列配置
     * @return Queue
     */
    @Bean(name = "deadQueue")
    public Queue deadQueue(){
        return new Queue("dead_ttl.direct.queue",true,false,false);
    }


    /**
     * 备份队列
     * @return Queue
     */
    @Bean(name = "messageQueue")
    public Queue directQueueMessage(){
        return new Queue("ttl.message.direct.queue",true);
    }

    /**
     * 正常配置
     * @param directQueue 路由队列
     * @param myTtlExchange 指定交换机
     * @return Binding
     */
    @Bean
    public Binding myBinding1(Queue directQueue, DirectExchange myTtlExchange){
        return BindingBuilder.bind(directQueue).to(myTtlExchange).with(propertiesConfig.getRoute_key());
    }

    @Bean
    public Binding myBinding2(Queue messageQueue, DirectExchange myTtlExchange){
        return BindingBuilder.bind(messageQueue).to(myTtlExchange).with("ttl-message");
    }

    @Bean
    public Binding deadBinding(Queue deadQueue,DirectExchange deadLetterExchange){
        return BindingBuilder.bind(deadQueue).to(deadLetterExchange).with("dead");
    }
}
