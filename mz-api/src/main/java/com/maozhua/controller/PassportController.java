package com.maozhua.controller;

import com.maozhua.bo.RegisterLoginBO;
import com.maozhua.grace.result.GraceJsonResult;
import com.maozhua.grace.result.ResponseStatusEnum;
import com.maozhua.utils.IpUtil;
import com.maozhua.utils.SmsUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        smsUtil.sendSms(mobile, code);

        log.info("IP ==> " + userIp + "，手机号 ==> " + mobile + "，验证码 ==> " + code);

        // 把验证码放到Redis，用于后续验证，失效时间30分钟
        redisOperator.set(MOBILE_SMS_CODE + ":" + mobile, code, 30 * 60);
        return GraceJsonResult.ok();
    }

    @PostMapping("login")
    public GraceJsonResult login(@Valid @RequestBody RegisterLoginBO registerLoginBO,
                                 BindingResult bindingResult,
                                 HttpServletRequest request) throws Exception {
        // 判断 BindingResult 是否保存了错误的验证信息，如果有，需要返回给前端
        if (bindingResult.hasErrors()) {
            Map<String, String> map = getErrors(bindingResult);
            return GraceJsonResult.errorMap(map);
        }
        return GraceJsonResult.ok();
    }
}
