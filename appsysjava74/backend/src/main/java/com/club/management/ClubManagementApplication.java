package com.club.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 社团管理系统启动类
 * 
 * @author club-management
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class ClubManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubManagementApplication.class, args);
    }
}
