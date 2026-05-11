package com.hospital.controller;

import com.hospital.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "公共接口", description = "无需登录即可访问的接口")
@RestController
@RequestMapping("/public")
public class PublicController {

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<Map<String, Object>> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("timestamp", System.currentTimeMillis());
        result.put("service", "Hospital-App-Backend");
        result.put("version", "1.0.0");
        return Result.success(result);
    }

    @Operation(summary = "获取系统配置")
    @GetMapping("/config")
    public Result<Map<String, Object>> getConfig() {
        Map<String, Object> result = new HashMap<>();
        result.put("appName", "医院住院服务APP");
        result.put("version", "1.0.0");
        result.put("elderModeSupported", true);
        result.put("voiceInputSupported", true);
        
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("0", "待审核");
        statusMap.put("1", "审核通过");
        statusMap.put("2", "已入院");
        statusMap.put("3", "已出院");
        statusMap.put("4", "已取消");
        result.put("admissionStatus", statusMap);
        
        Map<String, String> feeTypeMap = new HashMap<>();
        feeTypeMap.put("药费", "药费");
        feeTypeMap.put("检查费", "检查费");
        feeTypeMap.put("治疗费", "治疗费");
        feeTypeMap.put("手术费", "手术费");
        feeTypeMap.put("护理费", "护理费");
        feeTypeMap.put("床位费", "床位费");
        feeTypeMap.put("其他", "其他");
        result.put("feeTypes", feeTypeMap);
        
        return Result.success(result);
    }

    @Operation(summary = "获取错误提示文案")
    @GetMapping("/error-messages")
    public Result<Map<String, String>> getErrorMessages() {
        Map<String, String> messages = new HashMap<>();
        messages.put("network_error", "网络连接异常，请检查您的网络设置");
        messages.put("payment_failed", "支付失败，请检查支付账户余额或稍后重试");
        messages.put("login_failed", "登录失败，请检查用户名和密码");
        messages.put("permission_denied", "您没有权限执行此操作");
        messages.put("data_not_found", "您查询的数据不存在");
        messages.put("server_error", "服务器繁忙，请稍后重试");
        messages.put("invalid_input", "输入信息不完整，请检查后重新提交");
        messages.put("order_expired", "订单已过期，请重新下单");
        return Result.success(messages);
    }
}
