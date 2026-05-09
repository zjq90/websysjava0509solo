package com.agricultural;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 农业财务与成本管理系统启动类
 * 
 * 系统功能包括：
 * 1. 进销存订单与财务单互通互联，自动生成应收应付账款
 * 2. 多个角度核算成本（育种成本、田间投入、加工费用、包装物流等）
 * 3. 利润分析，从品种、客户、区域层级等多环节分析
 * 
 * 技术栈：
 * - Spring Boot 2.7.18
 * - H2 内存数据库
 * - JPA/Hibernate
 * - Thymeleaf + Bootstrap V4
 * - Maven 构建
 * 
 * @author Agricultural Management System
 * @version 1.0.0
 */
@SpringBootApplication
public class ManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementSystemApplication.class, args);
        System.out.println("========================================");
        System.out.println("  农业财务与成本管理系统启动成功");
        System.out.println("  访问地址: http://localhost:8080");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}
