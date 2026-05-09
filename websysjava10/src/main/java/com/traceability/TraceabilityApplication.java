package com.traceability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 质量追溯管理系统主入口类
 * 系统功能：
 * 1. 全链条追溯：亲本来源→田间种植→收获→加工→包装→销售
 * 2. 二维码/条码生成，实现"一袋一码"溯源
 * 3. 质检报告上传与种子批次关联
 * 
 * @author Traceability Team
 * @version 1.0.0
 */
@SpringBootApplication
public class TraceabilityApplication {

    public static void main(String[] args) {
        SpringApplication.run(TraceabilityApplication.class, args);
        System.out.println("==========================================");
        System.out.println("  质量追溯管理系统启动成功！");
        System.out.println("  访问地址: http://localhost:8080/");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("==========================================");
    }
}
