package com.maozhua.mapper;

import com.maozhua.my.mapper.MyMapper;
import com.maozhua.pojo.Comment;
import org.springframework.stereotype.Repository;

/**
 * @author sryzzz
 * @create 2022/6/2 12:00
 * @description CommentMapper
 */
@Repository
public interface CommentMapper extends MyMapper<Comment> {
}