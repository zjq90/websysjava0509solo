package com.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 大学生社团管理后台系统 - 主启动类
 * 系统功能：
 * 1. 社团审核与管理（成立审核、年度注册、违规处理）
 * 2. 活动监管（校级活动审批、内容审查）
 * 3. 数据统计与分析（社团数据、经费监管）
 * 4. 系统设置（权限管理、消息模板、数据备份）
 *
 * @author Club Management System
 * @version 1.0.0
 */
@SpringBootApplication
public class ClubManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubManagementApplication.class, args);
        System.out.println("========================================");
        System.out.println("  大学生社团管理后台系统启动成功!");
        System.out.println("  API文档: http://localhost:8080/api/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("========================================");
    }
}
