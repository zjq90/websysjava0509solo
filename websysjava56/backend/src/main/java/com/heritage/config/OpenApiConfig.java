package com.heritage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI heritageOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("文物收藏数据中台 API")
                        .description("文物收藏数据中台后端服务API文档，包含数据采集、数据治理、数据分析、数据服务等模块")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("文物收藏数据中台团队")
                                .email("support@heritage-platform.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
