package com.maozhua.controller;

import com.maozhua.utils.RedisOperator;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/6/2 21:39
 * @description 公共接口
 */
public class BaseInfoProperties {

    /**
     * Redis验证码key
     */
    public static final String MOBILE_SMS_CODE = "mobile:sms_code";
    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_INFO = "redis_user_info";

    /**
     * 我的关注总数
     */
    public static final String REDIS_MY_FOLLOWS_COUNTS = "redis_my_follows_counts";
    /**
     * 我的粉丝总数
     */
    public static final String REDIS_MY_FANS_COUNTS = "redis_my_fans_counts";

    /**
     * 视频和发布者获赞数
     */
    public static final String REDIS_VLOG_BE_LIKED_COUNTS = "redis_vlog_be_liked_counts";
    public static final String REDIS_VLOGER_BE_LIKED_COUNTS = "redis_vloger_be_liked_counts";

    @Resource
    public RedisOperator redisOperator;

}
