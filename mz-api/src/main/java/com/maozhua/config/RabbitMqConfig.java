package com.maozhua.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类
 *
 * @author sryzzz
 * @create 2022/10/7 08:46
 * @description RabbitMQ 配置类
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 1、定义交换机
     * 2、定义队列
     * 3、创建交换机
     * 4、创建队列
     * 5、队列和交换机的绑定
     */

    /**
     * 定义交换机
     */
    public static final String EXCHANGE_MSG = "exchange_msg";

    /**
     * 定义队列
     */
    public static final String QUEUE_SYS_MSG = "queue_sys_msg";

    @Bean(EXCHANGE_MSG)
    public Exchange exchange() {
        // 构建交换机
        return ExchangeBuilder
                // 使用 topic 类型
                .topicExchange(EXCHANGE_MSG)
                // 设置持久化
                .durable(true)
                .build();
    }

    @Bean(QUEUE_SYS_MSG)
    public Queue queue() {
        return new Queue(QUEUE_SYS_MSG);
    }

    @Bean
    public Binding binding(@Qualifier(EXCHANGE_MSG) Exchange exchange,
                           @Qualifier(QUEUE_SYS_MSG) Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                // 定义路由规则：*代表一个占位符。#代表多个占位符
                .with("sys.msg.*")
                .noargs();
    }
}
