package com.maozhua.controller;

import com.maozhua.base.BaseInfoProperties;
import com.maozhua.grace.result.GraceJsonResult;
import com.maozhua.grace.result.ResponseStatusEnum;
import com.maozhua.pojo.Users;
import com.maozhua.service.FansService;
import com.maozhua.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author sryzzz
 * @create 2022/6/6 01:17
 * @description 粉丝相关业务接口
 */
@Api(tags = "FansController => 粉丝相关业务接口")
@Slf4j
@RestController
@RequestMapping("fans")
public class FansController extends BaseInfoProperties {

    @Resource
    private UserService userService;

    @Resource
    private FansService fansService;

    @PostMapping("follow")
    public GraceJsonResult follow(@RequestParam String myId,
                                  @RequestParam String vlogerId) {

        if (StringUtils.isBlank(myId) || StringUtils.isBlank(vlogerId)) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }

        // 判断当前用户，自己不能关注自己
        if (myId.equalsIgnoreCase(vlogerId)) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }

        // 判断两个用户是否存在
        Users me = userService.getUserById(myId);
        Users vloger = userService.getUserById(vlogerId);
        // FIXME 两个用户id的数据库查询后的判断，是分开判断好还是合并判断好
        if (me == null || vloger == null) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }

        fansService.doFollow(myId, vlogerId);

        // 博主的粉丝 + 1，我的关注 + 1。
        redisOperator.increment(REDIS_MY_FOLLOWS_COUNTS + ":" + myId, 1);
        redisOperator.increment(REDIS_MY_FANS_COUNTS + ":" + vlogerId, 1);

        // 我和博主的关联关系，依赖Redis，不要存储数据库
        redisOperator.set(REDIS_FANS_AND_VLOGGER_RELATIONSHIP + ":" + myId + ":" + vlogerId, "1");

        return GraceJsonResult.ok();
    }
}
