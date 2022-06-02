package com.maozhua.controller;

import com.maozhua.grace.result.GraceJsonResult;
import com.maozhua.utils.SmsUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author sryzzz
 * @create 2022/6/2 16:00
 * @description
 */
@RestController
public class HelloController {

    @Resource
    private SmsUtil smsUtil;

    @GetMapping("send")
    public GraceJsonResult sendSms() throws Exception {
        String code = "0526";
        smsUtil.sendSms("", code);
        return GraceJsonResult.ok();
    }
}
