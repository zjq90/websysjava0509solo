package com.sales.controller;

import com.sales.entity.*;
import com.sales.service.CustomerService;
import com.sales.service.OrderService;
import com.sales.service.PriceStrategyService;
import com.sales.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 订单创建控制器
 * 处理订单创建和编辑的请求
 */
@Controller
@RequestMapping("/orders")
public class OrderCreateController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PriceStrategyService priceStrategyService;

    /**
     * 创建订单
     */
    @PostMapping
    public String createOrder(@RequestParam Long customerId,
                               @RequestParam List<Long> productIds,
                               @RequestParam List<Integer> quantities,
                               @RequestParam(required = false) String remark,
                               RedirectAttributes redirectAttributes) {
        try {
            Optional<Customer> customer = customerService.findById(customerId);
            if (!customer.isPresent()) {
                throw new RuntimeException("客户不存在");
            }

            List<OrderItem> orderItems = new ArrayList<>();
            for (int i = 0; i < productIds.size(); i++) {
                Optional<Product> product = productService.findById(productIds.get(i));
                if (product.isPresent() && quantities.get(i) > 0) {
                    OrderItem item = new OrderItem();
                    item.setProduct(product.get());
                    item.setQuantity(quantities.get(i));
                    orderItems.add(item);
                }
            }

            if (orderItems.isEmpty()) {
                throw new RuntimeException("请至少选择一个产品");
            }

            SalesOrder order = orderService.createOrder(customer.get(), orderItems, remark);
            redirectAttributes.addFlashAttribute("success", "订单创建成功！订单号：" + order.getOrderNo());
            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "创建订单失败：" + e.getMessage());
            return "redirect:/orders/new";
        }
    }

    /**
     * 获取计算后的价格（AJAX调用）
     */
    @GetMapping("/api/calculate-price")
    @ResponseBody
    public BigDecimal getCalculatedPrice(@RequestParam Long customerId,
                                         @RequestParam Long productId,
                                         @RequestParam Integer quantity) {
        Optional<Customer> customer = customerService.findById(customerId);
        Optional<Product> product = productService.findById(productId);

        if (customer.isPresent() && product.isPresent()) {
            return priceStrategyService.calculatePrice(product.get(), customer.get(), quantity);
        }
        return BigDecimal.ZERO;
    }
}
