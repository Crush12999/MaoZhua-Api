package com.maozhua.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author sryzzz
 * @create 2022/6/2 13:38
 * @description 接口文档配置
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {

    @Bean
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("猫爪短视频接口文档")
                        .description("猫爪短视频接口文档")
                        .termsOfServiceUrl("http://www.xx.com/")
                        .contact(new Contact("sryzzz", "https://github.com/Crush12999/MaoZhua-Api", "sryzzz99@163.com"))
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("1.0 版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.maozhua.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
