package com.maozhua.service.impl;

import com.github.pagehelper.PageHelper;
import com.maozhua.base.BaseInfoProperties;
import com.maozhua.bo.CommentBO;
import com.maozhua.enums.MessageEnum;
import com.maozhua.enums.YesOrNo;
import com.maozhua.mapper.CommentMapper;
import com.maozhua.mapper.CommentMapperCustom;
import com.maozhua.pojo.Comment;
import com.maozhua.pojo.Vlog;
import com.maozhua.service.CommentService;
import com.maozhua.service.MessageService;
import com.maozhua.service.VlogService;
import com.maozhua.utils.PagedGridResult;
import com.maozhua.vo.CommentVO;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CommentMapperCustom commentMapperCustom;

    @Resource
    private MessageService messageService;

    @Resource
    private VlogService vlogService;

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

        // 系统消息：评论/回复
        Vlog vlog = vlogService.getVlogById(commentBO.getVlogId());
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("commentId", commentId);
        msgContent.put("commentContent", commentBO.getContent());
        msgContent.put("vlogId", vlog.getId());
        msgContent.put("vlogCover", vlog.getCover());


        Integer type = MessageEnum.COMMENT_VLOG.type;
        if (StringUtils.isNotBlank(commentBO.getFatherCommentId()) &&
                !commentBO.getFatherCommentId().equalsIgnoreCase(ZERO)) {
            type = MessageEnum.REPLY_YOU.type;
        }
        messageService.createMsg(commentBO.getCommentUserId(),
                commentBO.getVlogerId(), type, msgContent);

        return commentVO;
    }

    /**
     * 查询评论列表
     *
     * @param vlogId   视频ID
     * @param userId   用户ID
     * @param page     第几页
     * @param pageSize 每页显示多少条
     * @return 评论列表
     */
    @Override
    public PagedGridResult listVlogComments(String vlogId, String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("vlogId", vlogId);

        PageHelper.startPage(page, pageSize);

        List<CommentVO> comments = commentMapperCustom.listVlogComments(map);
        comments.forEach(item -> {
            String isLike = redisOperator.getHashValue(REDIS_USER_LIKE_COMMENT, userId + ":" + item.getCommentId());
            String likeCounts = redisOperator.getHashValue(REDIS_VLOG_COMMENT_LIKED_COUNTS, item.getCommentId());
            if (StringUtils.isNotBlank(isLike) && ONE.equals(isLike)) {
                item.setIsLike(YesOrNo.YES.type);
            }
            if (StringUtils.isNotBlank(likeCounts)) {
                item.setLikeCounts(Integer.parseInt(likeCounts));
            }
        });

        return setterPagedGrid(comments, page);
    }

    /**
     * 删除评论
     *
     * @param commentUserId 这条评论的用户ID
     * @param commentId     这条评论的ID
     * @param vlogId        评论所属视频ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeComment(String commentUserId, String commentId, String vlogId) {
        Comment pendingComment = new Comment();
        pendingComment.setId(commentId);
        pendingComment.setCommentUserId(commentUserId);
        commentMapper.delete(pendingComment);

        if (!ZERO.equals(redisOperator.get(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId))) {
            redisOperator.decrement(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId, 1);
        }

    }

}
