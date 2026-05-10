package com.websys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Web平台管理系统主启动类
 * 系统功能包括：系统用户管理、设备管理、远程控制、商品管理等
 */
@SpringBootApplication
public class WebsysApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsysApplication.class, args);
        System.out.println("========================================");
        System.out.println("  Web平台管理系统启动成功！");
        System.out.println("  Swagger文档: http://localhost:8080/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}
