package com.maozhua.config;

import com.maozhua.utils.MinIoUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sryzzz
 * @create 2022/6/5 00:49
 * @description MinIO 配置类
 */
@Configuration
@Data
public class MinIoConfig {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.fileHost}")
    private String fileHost;
    @Value("${minio.bucketName}")
    private String bucketName;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.imgSize}")
    private Integer imgSize;
    @Value("${minio.fileSize}")
    private Integer fileSize;

    @Bean
    public MinIoUtil creatMinioClient() {
        return new MinIoUtil(endpoint, bucketName, accessKey, secretKey, imgSize, fileSize);
    }
}
