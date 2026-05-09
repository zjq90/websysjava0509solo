package com.sales.service;

import com.sales.entity.*;
import com.sales.repository.LogisticsRepository;
import com.sales.repository.OrderItemRepository;
import com.sales.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单服务层
 * 实现订单全流程跟踪：下单→收款→发货→物流→签收
 */
@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private LogisticsRepository logisticsRepository;

    @Autowired
    private PriceStrategyService priceStrategyService;

    @Autowired
    private ProductService productService;

    // 订单号计数器
    private final AtomicLong orderCounter = new AtomicLong(1);

    /**
     * 生成订单号
     * 格式：SO + 日期 + 6位序号
     */
    public String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = orderCounter.getAndIncrement();
        return String.format("SO%s%06d", dateStr, seq);
    }

    /**
     * 获取所有订单
     */
    public List<SalesOrder> findAll() {
        return orderRepository.findAllByOrderByCreateTimeDesc();
    }

    /**
     * 根据ID获取订单
     */
    public Optional<SalesOrder> findById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * 根据订单号获取订单
     */
    public Optional<SalesOrder> findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    /**
     * 按状态获取订单
     */
    public List<SalesOrder> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * 按客户ID获取订单
     */
    public List<SalesOrder> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerIdOrderByCreateTimeDesc(customerId);
    }

    /**
     * 创建订单
     * 初始状态：待下单
     */
    public SalesOrder createOrder(Customer customer, List<OrderItem> orderItems, String remark) {
        SalesOrder order = new SalesOrder();
        order.setOrderNo(generateOrderNo());
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING_ORDER);
        order.setRemark(remark);

        // 设置订单项并计算价格
        for (OrderItem item : orderItems) {
            // 使用价格策略计算单价
            Product product = item.getProduct();
            BigDecimal price = priceStrategyService.calculatePrice(product, customer, item.getQuantity());
            item.setUnitPrice(price);
            item.calculateSubtotal();
            item.setOrder(order);
        }

        order.setOrderItems(orderItems);
        order.calculateTotalAmount();

        return orderRepository.save(order);
    }

    /**
     * 保存订单
     */
    public SalesOrder save(SalesOrder order) {
        return orderRepository.save(order);
    }

    /**
     * 确认下单
     * 状态变更：待下单 → 已下单
     */
    public SalesOrder confirmOrder(Long orderId) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getStatus() == OrderStatus.PENDING_ORDER) {
                order.setStatus(OrderStatus.ORDERED);
                order.setOrderTime(LocalDateTime.now());
                return orderRepository.save(order);
            }
            throw new RuntimeException("订单状态不允许确认下单");
        }).orElseThrow(() -> new RuntimeException("订单不存在: " + orderId));
    }

    /**
     * 确认收款
     * 状态变更：已下单/待收款 → 已收款
     */
    public SalesOrder confirmPayment(Long orderId, BigDecimal paidAmount) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getStatus() == OrderStatus.ORDERED || 
                order.getStatus() == OrderStatus.PENDING_PAYMENT) {
                order.setStatus(OrderStatus.PAID);
                order.setPaymentTime(LocalDateTime.now());
                // 累计已收金额
                BigDecimal newPaid = order.getPaidAmount().add(paidAmount);
                order.setPaidAmount(newPaid);
                return orderRepository.save(order);
            }
            throw new RuntimeException("订单状态不允许收款");
        }).orElseThrow(() -> new RuntimeException("订单不存在: " + orderId));
    }

    /**
     * 安排发货
     * 状态变更：已收款 → 已发货
     */
    public SalesOrder shipOrder(Long orderId, Logistics logistics) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getStatus() == OrderStatus.PAID || 
                order.getStatus() == OrderStatus.PENDING_SHIPMENT) {
                order.setStatus(OrderStatus.SHIPPED);
                order.setShipmentTime(LocalDateTime.now());
                
                // 保存物流信息
                logistics.setOrder(order);
                logisticsRepository.save(logistics);
                
                return orderRepository.save(order);
            }
            throw new RuntimeException("订单状态不允许发货");
        }).orElseThrow(() -> new RuntimeException("订单不存在: " + orderId));
    }

    /**
     * 更新物流信息
     * 状态变更：已发货 → 运输中
     */
    public SalesOrder updateLogistics(Long orderId, Logistics logisticsUpdate) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getStatus() == OrderStatus.SHIPPED || 
                order.getStatus() == OrderStatus.IN_TRANSIT) {
                order.setStatus(OrderStatus.IN_TRANSIT);
                
                // 保存新的物流信息
                logisticsUpdate.setOrder(order);
                logisticsRepository.save(logisticsUpdate);
                
                return orderRepository.save(order);
            }
            throw new RuntimeException("订单状态不允许更新物流");
        }).orElseThrow(() -> new RuntimeException("订单不存在: " + orderId));
    }

    /**
     * 确认签收
     * 状态变更：运输中 → 已签收
     */
    public SalesOrder confirmDelivery(Long orderId) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getStatus() == OrderStatus.IN_TRANSIT || 
                order.getStatus() == OrderStatus.SHIPPED) {
                order.setStatus(OrderStatus.DELIVERED);
                order.setDeliveryTime(LocalDateTime.now());
                return orderRepository.save(order);
            }
            throw new RuntimeException("订单状态不允许签收");
        }).orElseThrow(() -> new RuntimeException("订单不存在: " + orderId));
    }

    /**
     * 取消订单
     */
    public SalesOrder cancelOrder(Long orderId, String reason) {
        return orderRepository.findById(orderId).map(order -> {
            if (order.getStatus() != OrderStatus.DELIVERED && 
                order.getStatus() != OrderStatus.CANCELLED) {
                order.setStatus(OrderStatus.CANCELLED);
                if (order.getRemark() != null) {
                    order.setRemark(order.getRemark() + " | 取消原因：" + reason);
                } else {
                    order.setRemark("取消原因：" + reason);
                }
                return orderRepository.save(order);
            }
            throw new RuntimeException("订单状态不允许取消");
        }).orElseThrow(() -> new RuntimeException("订单不存在: " + orderId));
    }

    /**
     * 获取订单的物流信息
     */
    public List<Logistics> getOrderLogistics(Long orderId) {
        return logisticsRepository.findByOrderIdOrderByCreateTimeDesc(orderId);
    }

    /**
     * 获取订单的订单项
     */
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    /**
     * 删除订单
     */
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    /**
     * 分页查询所有订单
     */
    public Page<SalesOrder> findAll(Pageable pageable) {
        return orderRepository.findAllByOrderByCreateTimeDesc(pageable);
    }

    /**
     * 按状态分页查询
     */
    public Page<SalesOrder> findByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatusOrderByCreateTimeDesc(status, pageable);
    }

    /**
     * 按客户ID分页查询
     */
    public Page<SalesOrder> findByCustomerId(Long customerId, Pageable pageable) {
        return orderRepository.findByCustomerIdOrderByCreateTimeDesc(customerId, pageable);
    }
}
