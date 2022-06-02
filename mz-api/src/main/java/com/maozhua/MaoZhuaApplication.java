package com.maozhua;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sryzzz
 * @create 2022/6/2 12:00
 * @description 猫爪短视频后台接口启动类
 */
@MapperScan(basePackages = "com.maozhua.mapper")
@SpringBootApplication
public class MaoZhuaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaoZhuaApplication.class, args);
    }
}
