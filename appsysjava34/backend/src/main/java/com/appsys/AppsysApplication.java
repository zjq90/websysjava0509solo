package com.appsys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "用户端APP系统API", version = "1.0.0", description = "用户端APP系统后端接口文档"))
public class AppsysApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppsysApplication.class, args);
    }
}
