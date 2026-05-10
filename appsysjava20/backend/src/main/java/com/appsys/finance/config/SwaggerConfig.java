package com.appsys.finance.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("财务与报表管理系统 API")
                .version("1.0.0")
                .description("财务与报表管理系统后端API接口文档，包含客户管理、产品管理、员工管理、财务报表和管理层经营看板等功能。\n\n" +
                    "技术栈：Spring Boot + JPA + H2 + SpringDoc OpenAPI\n\n" +
                    "主要特性：\n" +
                    "- 敏感信息AES-256加密存储\n" +
                    "- 批次编号8位数字字母组合且全局唯一\n" +
                    "- 保质期不得早于当前日期+6个月\n" +
                    "- 发芽率0-100%，精度保留1位小数\n" +
                    "- 客户手机号中国大陆格式校验")
                .contact(new Contact()
                    .name("开发团队")
                    .email("dev@example.com")));
    }
}
