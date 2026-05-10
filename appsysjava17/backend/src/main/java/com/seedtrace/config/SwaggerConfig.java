package com.seedtrace.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger OpenAPI 配置类
 * 
 * <p>该类配置Swagger API文档的基本信息，包括：</p>
 * <ul>
 *   <li>API标题和描述</li>
 *   <li>版本信息</li>
 *   <li>联系信息</li>
 *   <li>许可证信息</li>
 * </ul>
 * 
 * <p>访问地址：http://localhost:8080/swagger-ui.html</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置OpenAPI元数据
     * 
     * @return OpenAPI配置对象
     */
    @Bean
    public OpenAPI seedTraceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("种子质量追溯系统 API")
                .description("种子质量追溯系统的RESTful API接口文档，支持全链条溯源查询。\n\n" +
                    "### 核心功能\n" +
                    "- 批次管理：种子批次信息的增删改查\n" +
                    "- 亲本溯源：查询种子的亲本来源信息\n" +
                    "- 田间管理：记录田间种植、施肥、打药等操作\n" +
                    "- 加工流程：记录种子加工参数和质量检测\n" +
                    "- 质检报告：存储质检结果和报告\n" +
                    "- 销售记录：记录销售信息（敏感数据加密存储）\n" +
                    "- 溯源查询：通过批次号查询全链路信息\n" +
                    "- PDF导出：生成溯源报告PDF\n" +
                    "\n### 数据校验规则\n" +
                    "- 批次编号：8位数字+字母组合，全局唯一\n" +
                    "- 保质期：不得早于当前日期+6个月\n" +
                    "- 发芽率：0-100%，精度保留1位小数\n" +
                    "- 客户手机号：符合中国大陆手机号格式（1开头，11位）")
                .version("1.0.0")
                .contact(new Contact()
                    .name("种子质量追溯系统")
                    .email("support@seedtrace.com")
                    .url("https://www.seedtrace.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
    }
}
