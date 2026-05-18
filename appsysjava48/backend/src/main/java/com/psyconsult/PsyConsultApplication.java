package com.psyconsult;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PsyConsultApplication {

    public static void main(String[] args) {
        SpringApplication.run(PsyConsultApplication.class, args);
        System.out.println("====================================");
        System.out.println("  心理咨询系统后端启动成功！");
        System.out.println("  Swagger文档: http://localhost:8080/swagger-ui/");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("====================================");
    }
}
