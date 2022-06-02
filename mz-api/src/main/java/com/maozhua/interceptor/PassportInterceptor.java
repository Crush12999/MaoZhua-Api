package com.maozhua.interceptor;

import com.maozhua.controller.BaseInfoProperties;
import com.maozhua.exception.GraceException;
import com.maozhua.grace.result.ResponseStatusEnum;
import com.maozhua.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sryzzz
 * @create 2022/6/2 22:21
 * @description 验证码校验拦截器
 */
@Slf4j
public class PassportInterceptor extends BaseInfoProperties implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 获得用户 IP
        String userIp = IpUtil.getRequestIp(request);

        // 判断该IP地址是否短时间内连续获取验证码
        String mobileSmsCodeKey = MOBILE_SMS_CODE + ":" + userIp;
        boolean keyIsExist = redisOperator.keyIsExist(mobileSmsCodeKey);
        if (keyIsExist) {
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            log.info("短信发送频率过高！");
            return false;
        }

        return true;
    }
}
