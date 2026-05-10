package com.seedinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 种子库存管理系统主启动类
 * 基于Spring Boot 3.2.5构建的移动库存管理后端应用
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@SpringBootApplication
public class SeedInventoryApplication {

    /**
     * 应用程序入口方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SeedInventoryApplication.class, args);
        System.out.println("========================================");
        System.out.println("  种子库存管理系统启动成功!");
        System.out.println("  服务端口: 8080");
        System.out.println("  Swagger文档: http://localhost:8080/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}
