package com.maozhua.service;

import com.maozhua.bo.CommentBO;
import com.maozhua.utils.PagedGridResult;
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

    /**
     * 查询评论列表
     *
     * @param vlogId   视频ID
     * @param userId   用户ID
     * @param page     第几页
     * @param pageSize 每页显示多少条
     * @return 评论列表
     */
    PagedGridResult listVlogComments(String vlogId, String userId, Integer page, Integer pageSize);

    /**
     * 删除评论
     *
     * @param commentUserId 这条评论的用户ID
     * @param commentId     这条评论的ID
     * @param vlogId        评论所属视频ID
     */
    void removeComment(String commentUserId, String commentId, String vlogId);

}
