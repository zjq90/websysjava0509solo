package com.production;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 生产管理系统主应用类
 * 这是整个Spring Boot应用的入口点
 */
@SpringBootApplication
public class ProductionManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductionManagementApplication.class, args);
        System.out.println("======================================");
        System.out.println("  生产管理系统启动成功!");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("======================================");
    }
}