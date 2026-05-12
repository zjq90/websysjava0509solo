package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 医院住院管理系统启动类
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@SpringBootApplication
public class HospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
        System.out.println("====================================");
        System.out.println("  医院住院管理系统启动成功！");
        System.out.println("  后端地址: http://localhost:8080");
        System.out.println("  Swagger文档: http://localhost:8080/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("====================================");
    }
}
