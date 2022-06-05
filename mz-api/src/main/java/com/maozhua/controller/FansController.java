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
import org.springframework.web.bind.annotation.*;

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

        return GraceJsonResult.ok();
    }

    @PostMapping("cancel")
    public GraceJsonResult cancel(@RequestParam String myId,
                                  @RequestParam String vlogerId) {

        fansService.doCancel(myId, vlogerId);

        return GraceJsonResult.ok();
    }

    @GetMapping("queryDoIFollowVloger")
    public GraceJsonResult queryDoMeFollowVloger(@RequestParam String myId,
                                  @RequestParam String vlogerId) {
        return GraceJsonResult.ok(fansService.queryDoMeFollowVloger(myId, vlogerId));
    }
}
