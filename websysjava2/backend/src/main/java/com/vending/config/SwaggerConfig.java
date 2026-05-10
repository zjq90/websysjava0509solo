package com.vending.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger OpenAPI 配置类
 */
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("售货机管理系统 API")
                .version("1.0.0")
                .description("售货机管理系统接口文档，包含商品管理、库存监控、智能补货、订单管理等功能")
                .contact(new Contact()
                    .name("开发团队")
                    .email("support@vending.com")));
    }
}
