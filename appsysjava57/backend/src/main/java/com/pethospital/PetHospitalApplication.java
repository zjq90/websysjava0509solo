package com.pethospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 宠物医院问诊系统启动类
 * 
 * @author Pet Hospital Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class PetHospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetHospitalApplication.class, args);
        System.out.println("============================================");
        System.out.println("  宠物医院问诊系统启动成功！");
        System.out.println("  Swagger文档: http://localhost:8080/api/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("============================================");
    }
}
