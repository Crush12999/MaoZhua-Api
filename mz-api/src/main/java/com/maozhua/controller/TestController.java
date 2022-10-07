package com.maozhua.controller;

import com.maozhua.config.RabbitMqConfig;
import com.maozhua.grace.result.GraceJsonResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 测试接口
 *
 * @author sryzzz
 * @create 2022/10/7 09:01
 * @description 测试接口
 */
@Slf4j
@Api(tags = "测试接口")
@RequestMapping("test")
@RestController
public class TestController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("produce")
    public GraceJsonResult produce() {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_MSG, "sys.msg.send", "我发了一个消息~");
        return GraceJsonResult.ok();
    }

    @GetMapping("produce2")
    public GraceJsonResult produce2() {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_MSG, "sys.msg.delete", "我删除了一个消息~");
        return GraceJsonResult.ok();
    }
}
