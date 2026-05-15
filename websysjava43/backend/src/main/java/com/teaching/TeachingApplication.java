package com.teaching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "教学辅助系统API", version = "1.0", description = "教学辅助系统后端API文档"))
public class TeachingApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeachingApplication.class, args);
    }
}
