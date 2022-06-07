package com.maozhua.service;

import com.maozhua.bo.CommentBO;
import com.maozhua.vo.CommentVO;

/**
 * @author sryzzz
 * @create 2022/6/7 23:31
 * @description 评论业务逻辑层
 */
public interface CommentService {

    /**
     * 发表评论
     *
     * @param commentBO 评论信息
     * @return 评论信息
     */
    CommentVO createComment(CommentBO commentBO);

}
