package com.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 宠物医院问诊系统启动类
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class PetClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetClinicApplication.class, args);
    }
}