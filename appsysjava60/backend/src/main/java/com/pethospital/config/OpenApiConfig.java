package com.pethospital.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-OpenAPI配置类
 * 配置API文档信息
 * 
 * @author Pet Hospital Team
 */
@Configuration
public class OpenApiConfig {

    /**
     * 配置OpenAPI文档信息
     * 
     * @return OpenAPI配置对象
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("宠物医院问诊系统API")
                        .version("1.0.0")
                        .description("宠物医院问诊系统后端API文档，包含医生端知识库、数据统计等功能接口")
                        .contact(new Contact()
                                .name("Pet Hospital Team")
                                .email("support@pethospital.com")));
    }
}
