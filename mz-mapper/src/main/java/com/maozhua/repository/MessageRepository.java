package com.maozhua.repository;

import com.maozhua.mo.MessageMO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author sryzzz
 * @create 2022/6/10 12:01
 * @description MessageRepository：消息仓库
 */
@Repository
public interface MessageRepository extends MongoRepository<MessageMO, String> {
}
