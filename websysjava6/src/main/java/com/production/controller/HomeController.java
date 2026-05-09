package com.production.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 */
@Controller
public class HomeController {

    /**
     * 首页重定向到仪表盘
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    /**
     * 仪表盘页面
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}