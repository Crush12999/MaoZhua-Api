package com.maozhua.controller;

import com.maozhua.base.BaseInfoProperties;
import com.maozhua.bo.CommentBO;
import com.maozhua.grace.result.GraceJsonResult;
import com.maozhua.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
}