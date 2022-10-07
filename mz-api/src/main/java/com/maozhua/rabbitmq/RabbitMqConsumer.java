package com.maozhua.rabbitmq;

import com.maozhua.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 *
 * @author sryzzz
 * @create 2022/10/7 10:48
 * @description 消费者
 */
@Slf4j
@Component
public class RabbitMqConsumer {

    @RabbitListener(queues = {RabbitMqConfig.QUEUE_SYS_MSG})
    public void watchQueue(String payload, Message message) {
        log.info("payload => {}", payload);
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("routingKey => {}", routingKey);
    }
}
