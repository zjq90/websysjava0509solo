package com.secondhand;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "二手交易系统 API",
        version = "1.0.0",
        description = "二手交易系统的后端API接口文档",
        contact = @Contact(name = "开发团队", email = "dev@secondhand.com")
    )
)
public class SecondhandTradingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondhandTradingApplication.class, args);
    }

}