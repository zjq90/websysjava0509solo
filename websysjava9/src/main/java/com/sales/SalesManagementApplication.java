package com.sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 销售与客户管理系统主应用类
 * 技术栈：Spring Boot + H2 + Maven + Bootstrap V4
 * 功能模块：订单管理、客户管理、价格策略管理
 */
@SpringBootApplication
public class SalesManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalesManagementApplication.class, args);
        System.out.println("===== 销售与客户管理系统启动成功 =====");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("H2控制台: http://localhost:8080/h2-console");
    }
}
