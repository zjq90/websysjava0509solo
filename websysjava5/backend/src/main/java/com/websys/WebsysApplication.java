package com.websys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Web平台管理系统启动类
 * 
 * @author websys
 * @version 1.0.0
 */
@SpringBootApplication
public class WebsysApplication {

    /**
     * 应用程序入口
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(WebsysApplication.class, args);
    }
}
