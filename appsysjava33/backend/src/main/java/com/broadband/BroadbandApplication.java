package com.broadband;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 宽带服务App后端启动类
 *
 * @author broadband
 * @version 1.0.0
 */
@SpringBootApplication
public class BroadbandApplication {

    public static void main(String[] args) {
        SpringApplication.run(BroadbandApplication.class, args);
        System.out.println("====================================");
        System.out.println("  宽带服务App后端系统启动成功!");
        System.out.println("  Swagger文档: http://localhost:8080/api/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("====================================");
    }
}
