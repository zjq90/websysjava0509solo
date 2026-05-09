package com.agricultural.controller;

import com.agricultural.entity.Order;
import com.agricultural.entity.enums.OrderStatus;
import com.agricultural.entity.enums.OrderType;
import com.agricultural.service.CustomerService;
import com.agricultural.service.OrderService;
import com.agricultural.service.VarietyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 订单管理控制器
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final VarietyService varietyService;

    public OrderController(OrderService orderService,
                           CustomerService customerService,
                           VarietyService varietyService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.varietyService = varietyService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String type,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int size,
                       Model model) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Order> orderPage;
        if (type != null && !type.isEmpty()) {
            model.addAttribute("orders", orderService.findByType(OrderType.valueOf(type)));
            model.addAttribute("isFiltered", true);
        } else {
            orderPage = orderService.findAll(pageRequest);
            model.addAttribute("orders", orderPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", orderPage.getTotalPages());
            model.addAttribute("totalItems", orderPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("isFiltered", false);
        }
        model.addAttribute("selectedType", type);
        return "order/list";
    }

    @GetMapping("/sales")
    public String salesList(Model model) {
        model.addAttribute("orders", orderService.findByType(OrderType.SALES));
        model.addAttribute("selectedType", "SALES");
        return "order/list";
    }

    @GetMapping("/purchase")
    public String purchaseList(Model model) {
        model.addAttribute("orders", orderService.findByType(OrderType.PURCHASE));
        model.addAttribute("selectedType", "PURCHASE");
        return "order/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<Order> order = orderService.findById(id);
        if (!order.isPresent()) {
            attributes.addFlashAttribute("error", "订单不存在");
            return "redirect:/order";
        }
        model.addAttribute("order", order.get());
        return "order/view";
    }

    @GetMapping("/add")
    public String addForm(@RequestParam String type, Model model) {
        Order order = new Order();
        order.setOrderType(OrderType.valueOf(type));
        order.setOrderStatus(OrderStatus.PENDING);
        model.addAttribute("order", order);
        model.addAttribute("orderNo", orderService.generateOrderNo(OrderType.valueOf(type)));
        model.addAttribute("customers", customerService.findAllEnabled());
        model.addAttribute("varieties", varietyService.findAllEnabled());
        return "order/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Order order, RedirectAttributes attributes) {
        try {
            orderService.save(order);
            attributes.addFlashAttribute("success", "保存成功，已自动生成对应财务账款");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/order?type=" + order.getOrderType();
    }

    @GetMapping("/status/{id}/{status}")
    public String updateStatus(@PathVariable Long id, @PathVariable String status, 
                                RedirectAttributes attributes) {
        try {
            Order updated = orderService.updateStatus(id, OrderStatus.valueOf(status));
            if (updated != null) {
                attributes.addFlashAttribute("success", "状态更新成功");
            } else {
                attributes.addFlashAttribute("error", "订单不存在");
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "更新失败：" + e.getMessage());
        }
        return "redirect:/order";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            orderService.deleteById(id);
            attributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/order";
    }
}
