package com.lims.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger-OpenAPI配置类
 * 配置API文档的基本信息和展示规则
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置OpenAPI基本信息
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("检验检查管理系统 API")
                        .description("实现检验、检查科室与临床科室的信息互通，提高医技科室工作效率和报告准确性")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("LIMS Team")
                                .email("support@lims.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
