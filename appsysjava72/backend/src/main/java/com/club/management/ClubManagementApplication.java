package com.club.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 大学生社团管理系统启动类
 *
 * @author club-management
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.club.management.mapper")
@EnableAsync
@EnableScheduling
public class ClubManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubManagementApplication.class, args);
        System.out.println("===============================================");
        System.out.println("  大学生社团管理系统后端API启动成功!");
        System.out.println("  接口文档: http://localhost:8080/doc.html");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("===============================================");
    }
}
