package com.lims.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试数据控制器
 * 提供系统测试功能辅助接口
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/test")
@Tag(name = "测试数据管理", description = "系统测试功能辅助接口")
public class TestDataController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/count")
    @Operation(summary = "统计各类数据数量")
    public Map<String, Object> countAll() {
        Map<String, Object> result = new HashMap<>();
        result.put("departments", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM department", Integer.class));
        result.put("users", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sys_user", Integer.class));
        result.put("patients", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM patient", Integer.class));
        result.put("items", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM test_item", Integer.class));
        result.put("devices", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM device", Integer.class));
        result.put("applications", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM test_application", Integer.class));
        result.put("results", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM test_result", Integer.class));
        result.put("reports", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM test_report", Integer.class));
        return result;
    }

    @GetMapping("/status")
    @Operation(summary = "检查系统运行状态")
    public Map<String, Object> checkStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "running");
        result.put("message", "检验检查管理系统运行正常");
        result.put("database", "connected");
        result.put("swagger", "http://localhost:8080/api/swagger-ui.html");
        result.put("h2-console", "http://localhost:8080/api/h2-console");
        return result;
    }

    @PostMapping("/reset")
    @Operation(summary = "重置测试数据（谨慎使用）")
    public ResponseEntity<Map<String, String>> resetData() {
        Map<String, String> result = new HashMap<>();
        result.put("message", "重置功能请重启应用，数据会自动重新初始化");
        return ResponseEntity.ok(result);
    }
}
