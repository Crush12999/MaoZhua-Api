package com.maozhua.service.impl;

import com.maozhua.base.BaseInfoProperties;
import com.maozhua.enums.MessageEnum;
import com.maozhua.mo.MessageMO;
import com.maozhua.pojo.Users;
import com.maozhua.repository.MessageRepository;
import com.maozhua.service.MessageService;
import com.maozhua.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/6/9 23:27
 * @description 消息业务逻辑层
 */
@Service
public class MessageServiceImpl extends BaseInfoProperties implements MessageService {

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

    /**
     * 分页查询消息列表
     *
     * @param toUserId 接收方
     * @param page     当前页
     * @param pageSize 每页显示条数
     * @return 消息列表
     */
    @Override
    public List<MessageMO> queryList(String toUserId, Integer page, Integer pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "createTime");
        List<MessageMO> list = messageRepository.findAllByToUserIdOrderByCreateTimeDesc(toUserId, pageable);

        for (MessageMO msg : list) {
            // 如果类型是关注消息，则需要查询我之前有没有关注过他，用于在前端标记"互粉"，"互关"
            if (msg.getMsgType() != null && msg.getMsgType().equals(MessageEnum.FOLLOW_YOU.type)) {
                Map map = msg.getMsgContent();
                if (map == null) {
                    map = new HashMap<>();
                }
                String relationship = redisOperator.get(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + msg.getToUserId() + ":" + msg.getFromUserId());
                if (StringUtils.isNotBlank(relationship) && relationship.equals(ONE)) {
                    map.put("isFriend", true);
                } else {
                    map.put("isFriend", false);
                }
                msg.setMsgContent(map);
            }
        }

        return list;
    }

}
