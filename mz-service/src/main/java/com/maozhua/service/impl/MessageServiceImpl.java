package com.maozhua.service.impl;

import com.maozhua.mo.MessageMO;
import com.maozhua.pojo.Users;
import com.maozhua.repository.MessageRepository;
import com.maozhua.service.MessageService;
import com.maozhua.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/6/9 23:27
 * @description 消息业务逻辑层
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageRepository messageRepository;

    @Resource
    private UserService userService;

    /**
     * 创建消息
     *
     * @param fromUserId 哪个用户传来的
     * @param toUserId   发送给哪个用户
     * @param msgType    消息类型
     * @param msgContent 消息内容
     */
    @Override
    public void createMsg(String fromUserId, String toUserId, Integer msgType, Map msgContent) {
        Users fromUser = userService.getUserById(fromUserId);
        MessageMO messageMO = new MessageMO();
        messageMO.setFromUserId(fromUserId);
        messageMO.setFromNickname(fromUser.getNickname());
        messageMO.setFromFace(fromUser.getFace());

        messageMO.setToUserId(toUserId);

        messageMO.setMsgType(msgType);
        if (msgContent != null) {
            messageMO.setMsgContent(msgContent);
        }

        messageMO.setCreateTime(new Date());

        messageRepository.save(messageMO);
    }

}
