package com.pethospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 宠物医院问诊系统启动类
 * 
 * @author Pet Hospital Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableCaching
public class PetHospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetHospitalApplication.class, args);
    }
}
