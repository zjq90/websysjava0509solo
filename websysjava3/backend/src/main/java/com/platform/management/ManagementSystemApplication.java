package com.platform.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Web平台管理系统启动类
 * 
 * @author platform
 * @version 1.0.0
 */
@SpringBootApplication
@MapperScan("com.platform.management.mapper")
public class ManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementSystemApplication.class, args);
    }
}
