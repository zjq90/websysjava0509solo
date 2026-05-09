package com.agriculture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 农业育种试验数据采集系统启动类
 * 系统功能：
 * 1. 田间数据采集功能 - 支持离线录入育种试验数据
 * 2. 病虫害图像上传 - 拍照上传病虫害图像
 * 3. 气象数据对接 - 实时获取种植区域环境数据
 * 4. 数据加密存储 - 敏感信息AES-256加密
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@SpringBootApplication
public class FieldCollectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(FieldCollectionApplication.class, args);
        System.out.println("========================================");
        System.out.println("  农业育种试验数据采集系统启动成功！");
        System.out.println("  Swagger地址: http://localhost:8080/api/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/api/h2-console");
        System.out.println("========================================");
    }
}
