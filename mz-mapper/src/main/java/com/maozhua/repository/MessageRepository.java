package com.maozhua.repository;

import com.maozhua.mo.MessageMO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sryzzz
 * @create 2022/6/10 12:01
 * @description MessageRepository：消息仓库
 */
@Repository
public interface MessageRepository extends MongoRepository<MessageMO, String> {

    /**
     * 自定义条件查询
     *
     * @param toUserId 接收方
     * @param pageable 分页
     * @return MessageMO集合
     */
    List<MessageMO> findAllByToUserIdOrderByCreateTimeDesc(String toUserId, Pageable pageable);

}
