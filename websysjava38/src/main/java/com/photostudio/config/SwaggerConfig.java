package com.photostudio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger OpenAPI配置类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("影楼web平台管理系统API")
                        .version("1.0.0")
                        .description("影楼管理系统后端API接口文档，包含订单管理、客户管理、套餐管理、云相册管理等功能")
                        .contact(new Contact()
                                .name("PhotoStudio Team")
                                .email("support@photostudio.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
