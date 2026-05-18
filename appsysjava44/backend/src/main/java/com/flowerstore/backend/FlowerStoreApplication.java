package com.flowerstore.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 花店管理系统主启动类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
public class FlowerStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowerStoreApplication.class, args);
        System.out.println("====================================");
        System.out.println("  花店管理系统启动成功!");
        System.out.println("  Swagger文档: http://localhost:8080/swagger-ui.html");
        System.out.println("  H2数据库控制台: http://localhost:8080/h2-console");
        System.out.println("====================================");
    }
}
