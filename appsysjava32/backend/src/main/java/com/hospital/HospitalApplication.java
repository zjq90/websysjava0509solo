package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 医院住院服务APP启动类
 * 住院服务延伸功能系统
 * 
 * @author hospital
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class HospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
        System.out.println("=========================================");
        System.out.println("    医院住院服务APP后端系统启动成功！");
        System.out.println("    Swagger文档: http://localhost:8080/api/swagger-ui.html");
        System.out.println("    H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("=========================================");
    }
}
