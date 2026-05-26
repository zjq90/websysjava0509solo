package com.accounting.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 个人记账系统启动类
 * 系统基础记账功能模块入口
 */
@SpringBootApplication
@MapperScan("com.accounting.system.mapper")
public class AccountingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountingApplication.class, args);
        System.out.println("=========================================");
        System.out.println("  个人记账系统管理后台启动成功！");
        System.out.println("  API地址: http://localhost:8088/api");
        System.out.println("  Swagger文档: http://localhost:8088/api/doc.html");
        System.out.println("  H2控制台: http://localhost:8088/api/h2-console");
        System.out.println("=========================================");
    }
}
