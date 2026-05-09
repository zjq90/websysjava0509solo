package com.traceability.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面路由控制器
 * 处理前端页面的路由请求
 */
@Controller
public class IndexController {

    /**
     * 首页
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 种子批次管理页面
     */
    @GetMapping("/seed-batch")
    public String seedBatch() {
        return "seed-batch";
    }

    /**
     * 亲本来源管理页面
     */
    @GetMapping("/parent-source")
    public String parentSource() {
        return "parent-source";
    }

    /**
     * 田间种植管理页面
     */
    @GetMapping("/field-planting")
    public String fieldPlanting() {
        return "field-planting";
    }

    /**
     * 收获管理页面
     */
    @GetMapping("/harvest")
    public String harvest() {
        return "harvest";
    }

    /**
     * 加工管理页面
     */
    @GetMapping("/processing")
    public String processing() {
        return "processing";
    }

    /**
     * 包装管理页面
     */
    @GetMapping("/packaging")
    public String packaging() {
        return "packaging";
    }

    /**
     * 销售管理页面
     */
    @GetMapping("/sale")
    public String sale() {
        return "sale";
    }

    /**
     * 质检报告管理页面
     */
    @GetMapping("/quality-report")
    public String qualityReport() {
        return "quality-report";
    }

    /**
     * 全链条追溯页面
     */
    @GetMapping("/traceability")
    public String traceability() {
        return "traceability";
    }

    /**
     * 二维码管理页面
     */
    @GetMapping("/qr-code")
    public String qrCode() {
        return "qr-code";
    }

    /**
     * 测试数据生成页面
     */
    @GetMapping("/test-data")
    public String testData() {
        return "test-data";
    }
}
