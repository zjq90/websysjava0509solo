package com.flower;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 花店商家端管理系统启动类
 * 基于Spring Boot 2.7.x + JDK8 + H2数据库
 *
 * @author Flower Shop Team
 * @version 1.0.0
 */
@SpringBootApplication
public class FlowerShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowerShopApplication.class, args);
        System.out.println("========================================");
        System.out.println("  花店商家端管理系统启动成功！");
        System.out.println("  接口文档: http://localhost:8080/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}
