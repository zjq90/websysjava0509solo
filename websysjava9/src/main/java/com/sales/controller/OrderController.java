package com.sales.controller;

import com.sales.entity.*;
import com.sales.service.CustomerService;
import com.sales.service.OrderService;
import com.sales.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 订单管理控制器
 * 实现订单全流程跟踪：下单→收款→发货→物流→签收
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    /**
     * 订单列表页面
     */
    @GetMapping
    public String list(Model model,
                      @RequestParam(required = false) OrderStatus status,
                      @RequestParam(required = false) String orderNo,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SalesOrder> orderPage;
        List<SalesOrder> orders;

        if (orderNo != null && !orderNo.isEmpty()) {
            Optional<SalesOrder> order = orderService.findByOrderNo(orderNo);
            orders = order.isPresent() ? Collections.singletonList(order.get()) : new ArrayList<SalesOrder>();
            model.addAttribute("orders", orders);
            model.addAttribute("page", null);
        } else if (status != null) {
            orderPage = orderService.findByStatus(status, pageable);
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("page", orderPage);
        } else {
            orderPage = orderService.findAll(pageable);
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("page", orderPage);
        }

        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("orderNo", orderNo);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "order/list";
    }

    /**
     * 新建订单页面
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("customers", customerService.findAllActive());
        model.addAttribute("products", productService.findAllActive());
        return "order/form";
    }

    /**
     * 查看订单详情
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<SalesOrder> order = orderService.findById(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            model.addAttribute("orderItems", orderService.getOrderItems(id));
            model.addAttribute("logisticsList", orderService.getOrderLogistics(id));
            return "order/detail";
        } else {
            redirectAttributes.addFlashAttribute("error", "订单不存在");
            return "redirect:/orders";
        }
    }

    /**
     * 确认下单
     */
    @PostMapping("/{id}/confirm")
    public String confirmOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.confirmOrder(id);
            redirectAttributes.addFlashAttribute("success", "订单已确认下单！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "操作失败：" + e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    /**
     * 确认收款
     */
    @PostMapping("/{id}/payment")
    public String confirmPayment(@PathVariable Long id, 
                                 @RequestParam BigDecimal paidAmount,
                                 RedirectAttributes redirectAttributes) {
        try {
            orderService.confirmPayment(id, paidAmount);
            redirectAttributes.addFlashAttribute("success", "收款确认成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "操作失败：" + e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    /**
     * 发货页面
     */
    @GetMapping("/{id}/ship")
    public String shipForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<SalesOrder> order = orderService.findById(id);
        if (order.isPresent()) {
            model.addAttribute("order", order.get());
            model.addAttribute("logistics", new Logistics());
            return "order/ship";
        } else {
            redirectAttributes.addFlashAttribute("error", "订单不存在");
            return "redirect:/orders";
        }
    }

    /**
     * 确认发货
     */
    @PostMapping("/{id}/ship")
    public String shipOrder(@PathVariable Long id, 
                            @ModelAttribute Logistics logistics,
                            RedirectAttributes redirectAttributes) {
        try {
            orderService.shipOrder(id, logistics);
            redirectAttributes.addFlashAttribute("success", "发货成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "操作失败：" + e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    /**
     * 更新物流信息
     */
    @PostMapping("/{id}/logistics")
    public String updateLogistics(@PathVariable Long id,
                                   @ModelAttribute Logistics logistics,
                                   RedirectAttributes redirectAttributes) {
        try {
            orderService.updateLogistics(id, logistics);
            redirectAttributes.addFlashAttribute("success", "物流信息已更新！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "操作失败：" + e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    /**
     * 确认签收
     */
    @PostMapping("/{id}/delivery")
    public String confirmDelivery(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.confirmDelivery(id);
            redirectAttributes.addFlashAttribute("success", "订单已签收！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "操作失败：" + e.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    /**
     * 取消订单
     */
    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                              @RequestParam String reason,
                              RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id, reason);
            redirectAttributes.addFlashAttribute("success", "订单已取消！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "操作失败：" + e.getMessage());
        }
        return "redirect:/orders/" + id;
    }
}
