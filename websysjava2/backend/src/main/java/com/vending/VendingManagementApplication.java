package com.vending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 售货机管理系统主应用程序入口
 * 基于Spring Boot 2.7.x + JDK8 + H2数据库
 */
@SpringBootApplication
public class VendingManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(VendingManagementApplication.class, args);
    }
}
