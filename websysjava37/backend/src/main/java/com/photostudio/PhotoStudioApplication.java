package com.photostudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 影楼管理系统启动类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class PhotoStudioApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoStudioApplication.class, args);
        System.out.println("========================================");
        System.out.println("  影楼管理系统启动成功!");
        System.out.println("  Swagger文档: http://localhost:8080/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}
