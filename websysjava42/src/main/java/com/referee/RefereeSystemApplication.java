package com.referee;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 裁判管理系统主启动类
 * 系统功能包括：
 * 1. 裁判模块：运动员信息、比赛信息管理，评分审核
 * 2. 裁判长模块：比赛项目管理、成绩管理
 * 3. 运动员模块：查看比赛项目和成绩
 *
 * @author Referee System
 * @version 1.0.0
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "裁判管理系统 API",
        version = "1.0.0",
        description = "裁判管理系统后端RESTful API接口文档",
        contact = @Contact(name = "开发团队", email = "support@referee.com")
    )
)
public class RefereeSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefereeSystemApplication.class, args);
    }
}
