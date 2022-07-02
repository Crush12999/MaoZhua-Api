package com.maozhua.controller;

import com.maozhua.base.BaseInfoProperties;
import com.maozhua.grace.result.GraceJsonResult;
import com.maozhua.mo.MessageMO;
import com.maozhua.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sryzzz
 * @create 2022/6/7 23:30
 * @description 消息功能模块接口
 */
@Api(tags = "MsgController => 消息功能模块接口")
@Slf4j
@RestController
@RequestMapping("msg")
public class MsgController extends BaseInfoProperties {

    @Resource
    private MessageService messageService;

    @ApiOperation(value = "分页获取消息")
    @GetMapping("list")
    public GraceJsonResult list(@RequestParam String userId,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize) {
        // MongoDB 从0开始分页，区别于数据库
        if (page == null) {
            page = COMMON_START_PAGE_ZERO;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        List<MessageMO> messageMOS = messageService.queryList(userId, page, pageSize);
        return GraceJsonResult.ok(messageMOS);
    }
}
