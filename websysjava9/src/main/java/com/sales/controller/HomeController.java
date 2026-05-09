package com.sales.controller;

import com.sales.entity.OrderStatus;
import com.sales.service.CustomerService;
import com.sales.service.OrderService;
import com.sales.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

/**
 * 首页控制器
 * 处理系统首页和仪表盘的请求
 */
@Controller
public class HomeController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    /**
     * 系统首页
     * 展示系统概览和统计数据
     */
    @GetMapping("/")
    public String index(Model model) {
        // 统计数据
        long totalOrders = orderService.findAll().size();
        long pendingOrders = orderService.findByStatus(OrderStatus.PENDING_ORDER).size() 
                           + orderService.findByStatus(OrderStatus.ORDERED).size();
        long shippingOrders = orderService.findByStatus(OrderStatus.SHIPPED).size() 
                            + orderService.findByStatus(OrderStatus.IN_TRANSIT).size();
        long totalCustomers = customerService.findAll().size();
        long totalProducts = productService.findAll().size();

        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("shippingOrders", shippingOrders);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("recentOrders", orderService.findAll().stream().limit(5).collect(Collectors.toList()));

        return "index";
    }
}
