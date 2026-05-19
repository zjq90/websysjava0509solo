package com.pethospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "宠物医院问诊系统API", version = "1.0", description = "宠物医院问诊系统后端接口文档"))
public class PetHospitalApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetHospitalApplication.class, args);
    }
}
