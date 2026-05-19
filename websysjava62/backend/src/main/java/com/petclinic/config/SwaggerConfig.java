package com.petclinic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-OpenAPI 配置类
 * 配置API文档的基本信息和全局设置
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("宠物问诊管理后台系统 API")
                        .version("1.0.0")
                        .description("宠物问诊管理后台系统接口文档，包含紧急症状库、药品管理、审核规则、智能设备监控、AI诊断、保险理赔等功能模块")
                        .contact(new Contact()
                                .name("Pet Clinic Team")
                                .email("support@petclinic.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
