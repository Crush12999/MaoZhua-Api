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

    @Resource
    public RedisOperator redisOperator;

    /**
     * 获取数据校验的错误信息
     *
     * @param bindingResult BindingResult
     * @return 数据校验的错误信息
     */
    public Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError error : fieldErrors) {
            // 错误所对应的属性字段名
            String field = error.getField();
            // 错误提示
            String msg = error.getDefaultMessage();
            map.put(field, msg);
        }

        return map;
    }
}
