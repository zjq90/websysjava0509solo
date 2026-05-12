package com.hospital.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=====================================");
        System.out.println("医院药品与物资管理系统启动成功!");
        System.out.println("=====================================");
        System.out.println("后端API地址: http://localhost:8080/api");
        System.out.println("Swagger文档: http://localhost:8080/api/swagger-ui.html");
        System.out.println("H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("=====================================");
    }
}
