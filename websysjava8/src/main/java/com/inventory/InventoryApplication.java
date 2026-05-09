package com.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 智能库存管理系统主应用类
 * 基于Spring Boot开发的种子库存管理系统
 * 功能包括：
 * - 种子品类、品种、批次、保质期精细化管理
 * - 温湿度预警
 * - 近效期提醒
 * - 先进先出控制
 * - 多仓库多门店管理
 * - 库存调拨与盘点
 */
@SpringBootApplication
public class InventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }
}
