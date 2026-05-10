package com.appsys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 销售订单管理系统主启动类
 * 系统功能包括：客户管理、产品管理、订单管理、物流跟踪、合同管理等
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@SpringBootApplication
public class AppSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppSysApplication.class, args);
    }
}
