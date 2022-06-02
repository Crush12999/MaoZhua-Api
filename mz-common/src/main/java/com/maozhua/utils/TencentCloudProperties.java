package com.maozhua.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author sryzzz
 * @create 2022/6/2 15:15
 * @description 腾讯云短信配置类
 */
@Component
@Data
@PropertySource("classpath:tencentcloud.properties")
@ConfigurationProperties(prefix = "tencent.cloud")
public class TencentCloudProperties {

    private String secretId;

    private String secretKey;

    /**
     * 短信应用ID
     */
    private String templateId;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 短信应用ID
     */
    private String sdkAppId;
}
