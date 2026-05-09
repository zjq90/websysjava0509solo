package com.breeding.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试功能控制器
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String testPage(Model model) {
        return "test/index";
    }

    @GetMapping("/api/status")
    @ResponseBody
    public Map<String, Object> getStatus() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "running");
        result.put("message", "系统运行正常");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
}
