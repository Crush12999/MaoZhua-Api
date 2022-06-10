package com.maozhua.service;

import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/6/9 23:27
 * @description 消息业务逻辑层
 */
public interface MessageService {

    /**
     * 创建消息
     *
     * @param fromUserId 哪个用户传来的
     * @param toUserId   发送给哪个用户
     * @param msgType    消息类型
     * @param msgContent 消息内容
     */
    void createMsg(String fromUserId, String toUserId, Integer msgType, Map msgContent);

}
