package com.bikesystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 共享单车系统后端启动类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.bikesystem.mapper")
@EnableScheduling
public class BikeSharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeSharingApplication.class, args);
        System.out.println("========================================");
        System.out.println("  共享单车系统后端服务启动成功!");
        System.out.println("  接口文档: http://localhost:8080/api/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("========================================");
    }
}
