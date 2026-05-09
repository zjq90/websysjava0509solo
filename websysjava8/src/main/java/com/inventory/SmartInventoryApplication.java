package com.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 智能库存管理系统主程序入口
 * 功能：启动Spring Boot应用，启用定时任务调度
 * 技术栈：Spring Boot 2.7.x + JPA + H2
 */
@SpringBootApplication
@EnableScheduling
public class SmartInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartInventoryApplication.class, args);
        System.out.println("==================================");
        System.out.println("  智能库存管理系统启动成功！");
        System.out.println("  访问地址: http://localhost:8080/");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("==================================");
    }
}
