package com.sales.controller;

import com.sales.entity.*;
import com.sales.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 测试功能控制器
 * 提供测试数据生成和功能测试辅助
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PriceStrategyRepository priceStrategyRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private LogisticsRepository logisticsRepository;

    private Random random = new Random();

    /**
     * 生成测试订单数据
     */
    @GetMapping("/generate")
    public String generateTestOrders(RedirectAttributes redirectAttributes) {
        try {
            List<Customer> customers = customerRepository.findAll();
            List<Product> products = productRepository.findAll();

            if (customers.isEmpty() || products.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "请先确保有客户和产品数据！");
                return "redirect:/";
            }

            // 生成10个测试订单，每个订单处于不同的状态
            OrderStatus[] statuses = OrderStatus.values();
            
            for (int i = 0; i < 10; i++) {
                Customer customer = customers.get(random.nextInt(customers.size()));
                SalesOrder order = new SalesOrder();
                order.setOrderNo("TEST" + System.currentTimeMillis() + i);
                order.setCustomer(customer);
                order.setStatus(statuses[i % (statuses.length - 1)]); // 排除已取消状态
                
                // 创建订单项
                List<OrderItem> items = new ArrayList<>();
                int itemCount = random.nextInt(3) + 1; // 1-3个产品
                BigDecimal total = BigDecimal.ZERO;
                
                for (int j = 0; j < itemCount; j++) {
                    Product product = products.get(random.nextInt(products.size()));
                    int quantity = random.nextInt(50) + 1;
                    
                    OrderItem item = new OrderItem();
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    item.setUnitPrice(product.getBasePrice());
                    item.setSubtotal(product.getBasePrice().multiply(new BigDecimal(quantity)));
                    item.setOrder(order);
                    items.add(item);
                    total = total.add(item.getSubtotal());
                }
                
                order.setOrderItems(items);
                order.setTotalAmount(total);
                
                // 根据状态设置相关时间
                if (order.getStatus().ordinal() >= OrderStatus.ORDERED.ordinal()) {
                    order.setOrderTime(java.time.LocalDateTime.now().minusDays(random.nextInt(30)));
                }
                if (order.getStatus().ordinal() >= OrderStatus.PAID.ordinal()) {
                    order.setPaymentTime(java.time.LocalDateTime.now().minusDays(random.nextInt(20)));
                    order.setPaidAmount(total);
                }
                if (order.getStatus().ordinal() >= OrderStatus.SHIPPED.ordinal()) {
                    order.setShipmentTime(java.time.LocalDateTime.now().minusDays(random.nextInt(10)));
                }
                if (order.getStatus().ordinal() >= OrderStatus.DELIVERED.ordinal()) {
                    order.setDeliveryTime(java.time.LocalDateTime.now().minusDays(random.nextInt(5)));
                }
                
                orderRepository.save(order);
                
                // 为已发货和运输中的订单添加物流信息
                if (order.getStatus() == OrderStatus.SHIPPED || 
                    order.getStatus() == OrderStatus.IN_TRANSIT ||
                    order.getStatus() == OrderStatus.DELIVERED) {
                    Logistics logistics = new Logistics();
                    logistics.setOrder(order);
                    logistics.setTrackingNo("SF" + System.currentTimeMillis());
                    logistics.setLogisticsCompany("顺丰速运");
                    logistics.setFromAddress("公司仓库");
                    logistics.setToAddress(customer.getProvince() + customer.getCity() + customer.getAddress());
                    
                    if (order.getStatus() == OrderStatus.DELIVERED) {
                        logistics.setCurrentLocation("已签收");
                        logistics.setStatusDescription("订单已签收，感谢您的购买！");
                    } else if (order.getStatus() == OrderStatus.IN_TRANSIT) {
                        logistics.setCurrentLocation("配送中心");
                        logistics.setStatusDescription("货物正在配送中");
                    } else {
                        logistics.setCurrentLocation("仓库已出库");
                        logistics.setStatusDescription("货物已发出");
                    }
                    
                    logisticsRepository.save(logistics);
                }
            }

            redirectAttributes.addFlashAttribute("success", "成功生成10个测试订单！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "生成测试数据失败：" + e.getMessage());
        }
        return "redirect:/orders";
    }

    /**
     * 清空所有数据（测试用）
     */
    @GetMapping("/clear")
    public String clearAllData(RedirectAttributes redirectAttributes) {
        try {
            logisticsRepository.deleteAll();
            orderItemRepository.deleteAll();
            orderRepository.deleteAll();
            priceStrategyRepository.deleteAll();
            customerRepository.deleteAll();
            productRepository.deleteAll();
            
            redirectAttributes.addFlashAttribute("success", "已清空所有数据！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "清空数据失败：" + e.getMessage());
        }
        return "redirect:/";
    }
}
