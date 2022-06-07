package com.maozhua.service.impl;

import com.maozhua.base.BaseInfoProperties;
import com.maozhua.bo.CommentBO;
import com.maozhua.mapper.CommentMapper;
import com.maozhua.pojo.Comment;
import com.maozhua.service.CommentService;
import com.maozhua.vo.CommentVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author sryzzz
 * @create 2022/6/7 23:31
 * @description 评论业务逻辑层实现
 */
@Service
public class CommentServiceImpl extends BaseInfoProperties implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private Sid sid;

    /**
     * 发表评论
     *
     * @param commentBO 评论信息
     * @return 评论信息
     */
    @Override
    public CommentVO createComment(CommentBO commentBO) {
        String commentId = sid.nextShort();
        Comment comment = new Comment();
        comment.setId(commentId);

        comment.setVlogId(commentBO.getVlogId());
        comment.setVlogerId(commentBO.getVlogerId());

        comment.setCommentUserId(commentBO.getCommentUserId());
        comment.setFatherCommentId(commentBO.getFatherCommentId());
        comment.setContent(commentBO.getContent());

        comment.setLikeCounts(0);
        comment.setCreateTime(new Date());

        commentMapper.insert(comment);

        // 视频评论数 +1
        redisOperator.increment(REDIS_VLOG_COMMENT_COUNTS + ":" + commentBO.getVlogId(), 1);

        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(comment, commentVO);

        return commentVO;
    }

}
