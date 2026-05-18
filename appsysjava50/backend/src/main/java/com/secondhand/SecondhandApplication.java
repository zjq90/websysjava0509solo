package com.secondhand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 二手交易系统启动类
 *
 * @author secondhand
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class SecondhandApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondhandApplication.class, args);
        System.out.println("========================================");
        System.out.println("  二手交易系统启动成功!");
        System.out.println("  API文档地址: http://localhost:8080/api/swagger-ui.html");
        System.out.println("  H2数据库控制台: http://localhost:8080/api/h2-console");
        System.out.println("========================================");
    }

}
