package com.example.websys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Web平台管理系统主启动类
 * 功能：集成移动端H5看板，支持管理层实时查看经营动态
 * 技术栈：SpringBoot + H2 + Maven + Bootstrap V4
 */
@SpringBootApplication
public class WebSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSysApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  Web平台管理系统启动成功！");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("==============================================");
    }

}
