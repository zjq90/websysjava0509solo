package com.petclinic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI配置类
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI petClinicOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("宠物医院问诊系统API")
                        .description("宠物医院问诊系统后端API文档，包含健康管理、饮食建议、运动监测、宠物圈等功能")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("PetClinic Team")
                                .email("support@petclinic.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}