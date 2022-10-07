package com.maozhua.rabbitmq;

import com.maozhua.base.RabbitMqConfig;
import com.maozhua.enums.MessageEnum;
import com.maozhua.exception.GraceException;
import com.maozhua.grace.result.ResponseStatusEnum;
import com.maozhua.mo.MessageMO;
import com.maozhua.service.MessageService;
import com.maozhua.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

    @Resource
    MessageService messageService;

    @RabbitListener(queues = {RabbitMqConfig.QUEUE_SYS_MSG})
    public void watchQueue(String payload, Message message) {

        String sysMsgPrefix = "sys.msg.";
        log.info("payload => {}", payload);

        MessageMO messageMO = JsonUtils.jsonToPojo(payload, MessageMO.class);

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        log.info("routingKey => {}", routingKey);

        if (routingKey.equalsIgnoreCase(sysMsgPrefix + MessageEnum.FOLLOW_YOU.enValue)) {
            // 关注
            messageService.createMsg(messageMO.getFromUserId(),
                    messageMO.getToUserId(),
                    MessageEnum.FOLLOW_YOU.type,
                    null);
        } else if (routingKey.equalsIgnoreCase(sysMsgPrefix + MessageEnum.LIKE_VLOG.enValue)) {

        } else if (routingKey.equalsIgnoreCase(sysMsgPrefix + MessageEnum.COMMENT_VLOG.enValue)) {

        } else if (routingKey.equalsIgnoreCase(sysMsgPrefix + MessageEnum.REPLY_YOU.enValue)) {

        } else if (routingKey.equalsIgnoreCase(sysMsgPrefix + MessageEnum.LIKE_COMMENT.enValue)) {

        } else {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }

    }
}
