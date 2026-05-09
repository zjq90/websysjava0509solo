package com.breeding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 育种与田间管理系统主入口类
 * 基于Spring Boot 2.7.x开发
 * 技术栈：Spring Boot + JPA + H2 + Thymeleaf + Bootstrap 4
 */
@SpringBootApplication
public class BreedingManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BreedingManagementApplication.class, args);
    }
}
