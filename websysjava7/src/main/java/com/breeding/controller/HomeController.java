package com.breeding.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页控制器
 */
@Controller
public class HomeController {

    /**
     * 首页路由
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }
}
