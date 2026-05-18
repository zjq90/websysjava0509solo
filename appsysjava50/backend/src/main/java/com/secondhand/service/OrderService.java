package com.secondhand.service;

import com.secondhand.entity.Order;
import com.secondhand.entity.Product;
import com.secondhand.repository.OrderRepository;
import com.secondhand.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * 订单服务类
 *
 * @author secondhand
 * @version 1.0.0
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * 创建订单
     */
    @Transactional
    public Order createOrder(Long userId, Long productId, String pickupType, Long pickupPointId, String address, String receiver, String phone) {
        Product product = productRepository.findByIdAndIsDeletedFalse(productId).orElse(null);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        if (!"ON_SALE".equals(product.getStatus())) {
            throw new RuntimeException("商品已下架");
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setProductId(productId);
        order.setAmount(product.getPrice());
        order.setPickupType(pickupType);
        order.setPickupPointId(pickupPointId);
        order.setAddress(address);
        order.setReceiver(receiver);
        order.setPhone(phone);
        order.setStatus("PENDING");

        product.setStatus("LOCKED");
        productRepository.save(product);

        return orderRepository.save(order);
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%04d", new Random().nextInt(10000));
        return "ORD" + timestamp + random;
    }

    /**
     * 支付订单
     */
    @Transactional
    public Order payOrder(String orderNo, String paymentMethod) {
        Order order = orderRepository.findByOrderNoAndIsDeletedFalse(orderNo).orElse(null);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setPaymentMethod(paymentMethod);
        order.setPaymentTime(LocalDateTime.now());
        order.setStatus("PAID");

        Product product = productRepository.findByIdAndIsDeletedFalse(order.getProductId()).orElse(null);
        if (product != null) {
            product.setStatus("SOLD");
            productRepository.save(product);
        }

        return orderRepository.save(order);
    }

    /**
     * 取消订单
     */
    @Transactional
    public Order cancelOrder(String orderNo) {
        Order order = orderRepository.findByOrderNoAndIsDeletedFalse(orderNo).orElse(null);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus("CANCELLED");
        order.setCancelTime(LocalDateTime.now());

        Product product = productRepository.findByIdAndIsDeletedFalse(order.getProductId()).orElse(null);
        if (product != null) {
            product.setStatus("ON_SALE");
            productRepository.save(product);
        }

        return orderRepository.save(order);
    }

    /**
     * 确认收货
     */
    public Order confirmReceive(String orderNo) {
        Order order = orderRepository.findByOrderNoAndIsDeletedFalse(orderNo).orElse(null);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"PAID".equals(order.getStatus()) && !"SHIPPED".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus("COMPLETED");
        order.setReceiveTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    /**
     * 获取订单详情
     */
    public Order getOrderByOrderNo(String orderNo) {
        Order order = orderRepository.findByOrderNoAndIsDeletedFalse(orderNo).orElse(null);
        if (order != null) {
            Product product = productRepository.findByIdAndIsDeletedFalse(order.getProductId()).orElse(null);
            order.setProduct(product);
        }
        return order;
    }

    /**
     * 获取用户订单列表
     */
    public List<Order> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(userId);
        for (Order order : orders) {
            Product product = productRepository.findByIdAndIsDeletedFalse(order.getProductId()).orElse(null);
            order.setProduct(product);
        }
        return orders;
    }

    /**
     * 申请退款
     */
    public Order applyRefund(String orderNo) {
        Order order = orderRepository.findByOrderNoAndIsDeletedFalse(orderNo).orElse(null);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!"PAID".equals(order.getStatus()) && !"SHIPPED".equals(order.getStatus())) {
            throw new RuntimeException("订单状态不正确");
        }

        order.setStatus("REFUNDING");
        return orderRepository.save(order);
    }

}
