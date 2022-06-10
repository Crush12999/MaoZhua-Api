package com.maozhua;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sryzzz
 * @create 2022/6/2 12:00
 * @description 猫爪短视频后台接口启动类
 */
@EnableMongoRepositories
@ComponentScan(basePackages = {"com.maozhua", "org.n3r.idworker"})
@MapperScan(basePackages = "com.maozhua.mapper")
@SpringBootApplication
public class MaoZhuaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MaoZhuaApplication.class, args);
    }
}
