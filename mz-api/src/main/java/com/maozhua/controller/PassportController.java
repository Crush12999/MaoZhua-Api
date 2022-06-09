package com.maozhua.controller;

import com.maozhua.base.BaseInfoProperties;
import com.maozhua.bo.RegisterLoginBO;
import com.maozhua.grace.result.GraceJsonResult;
import com.maozhua.grace.result.ResponseStatusEnum;
import com.maozhua.pojo.Users;
import com.maozhua.service.UserService;
import com.maozhua.utils.IpUtil;
import com.maozhua.utils.SmsUtil;
import com.maozhua.vo.UserVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

/**
 * @author sryzzz
 * @create 2022/6/2 16:00
 * @description 通行证接口模块
 */
@Slf4j
@Api(tags = "通行证接口模块")
@RequestMapping("passport")
@RestController
public class PassportController extends BaseInfoProperties {

    @Resource
    private SmsUtil smsUtil;

    @Resource
    private UserService userService;

    @PostMapping("getSmsCode")
    public GraceJsonResult sendSms(@RequestParam String mobile,
                                   HttpServletRequest request) throws Exception {

        if (StringUtils.isBlank(mobile)) {
            return GraceJsonResult.ok();
        }

        // 获得用户IP地址
        String userIp = IpUtil.getRequestIp(request);
        String mobileSmsCodeKey = MOBILE_SMS_CODE + ":" + userIp;
        redisOperator.setnx60s(mobileSmsCodeKey, userIp);

        String code = (int)((Math.random() * 9 + 1) * 100000) + "";
        smsUtil.sendSms(mobile, code, 30);

        log.info("IP ==> " + userIp + "，手机号 ==> " + mobile + "，验证码 ==> " + code);

        // 把验证码放到Redis，用于后续验证，失效时间30分钟
        redisOperator.set(MOBILE_SMS_CODE + ":" + mobile, code, 30 * 60);
        return GraceJsonResult.ok();
    }

    @PostMapping("login")
    public GraceJsonResult login(@Valid @RequestBody RegisterLoginBO registerLoginBO,
                                 HttpServletRequest request) throws Exception {

        String mobile = registerLoginBO.getMobile();
        String code = registerLoginBO.getVerifyCode();

        // 从 redis 中获取验证码与前端信息校验
        String redisCode = redisOperator.get(MOBILE_SMS_CODE + ":" + mobile);
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(code)) {
            return GraceJsonResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        // 查询数据库，判断用户是否存在
        Users user = userService.queryMobileIsExist(mobile);
        if (user == null) {
            // 没注册过就注册
            user = userService.createUser(mobile);
        }

        // 保存用户信息和会话信息到 Redis
        String uToken = UUID.randomUUID().toString();
        redisOperator.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);

        // 用户登录注册成功以后，删除Redis中的验证码
        redisOperator.del(MOBILE_SMS_CODE + ":" + mobile);

        // 返回用户信息，包含token令牌
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserToken(uToken);

        return GraceJsonResult.ok(userVO);
    }

    /**
     * 退出登录
     */
    @PostMapping("logout")
    public GraceJsonResult logout(@RequestParam String userId,
                                   HttpServletRequest request) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return GraceJsonResult.ok();
        }

        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);

        return GraceJsonResult.ok();
    }
}
