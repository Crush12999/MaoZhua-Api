package com.maozhua.controller;

import com.maozhua.base.BaseInfoProperties;
import com.maozhua.bo.CommentBO;
import com.maozhua.grace.result.GraceJsonResult;
import com.maozhua.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author sryzzz
 * @create 2022/6/7 23:30
 * @description 评论业务接口
 */
@Api(tags = "CommentController => 评论业务接口")
@Slf4j
@RestController
@RequestMapping("comment")
public class CommentController extends BaseInfoProperties {

    @Resource
    private CommentService commentService;

    @ApiOperation(value = "发表评论")
    @PostMapping("create")
    public GraceJsonResult createComment(@Valid @RequestBody CommentBO commentBO) {
        return GraceJsonResult.ok(commentService.createComment(commentBO));
    }

    @ApiOperation(value = "获取视频评论数")
    @GetMapping("counts")
    public GraceJsonResult counts(@RequestParam String vlogId) {
        String commentCountStr = redisOperator.get(REDIS_VLOG_COMMENT_COUNTS + ":" + vlogId);
        if (StringUtils.isBlank(commentCountStr)) {
            commentCountStr = ZERO;
        }
        return GraceJsonResult.ok(Integer.parseInt(commentCountStr));
    }

    @ApiOperation(value = "获取视频评论列表")
    @GetMapping("list")
    public GraceJsonResult listComments(@RequestParam String vlogId,
                                        @RequestParam(defaultValue = "") String userId,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {

        return GraceJsonResult.ok(commentService.listVlogComments(vlogId, page, pageSize));
    }

    @ApiOperation(value = "删除视频评论")
    @DeleteMapping("delete")
    public GraceJsonResult removeComment(@RequestParam String commentUserId,
                                         @RequestParam String commentId,
                                         @RequestParam String vlogId) {
        commentService.removeComment(commentUserId, commentId, vlogId);
        return GraceJsonResult.ok();
    }
}
