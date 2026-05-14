package com.photostudio.service;

import com.photostudio.entity.Order;
import com.photostudio.entity.Order.OrderStatus;
import com.photostudio.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单服务类
 * 提供订单管理相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * 获取所有订单
     * @return 订单列表
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * 根据ID获取订单
     * @param id 订单ID
     * @return 订单信息
     */
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * 根据订单编号获取订单
     * @param orderNo 订单编号
     * @return 订单信息
     */
    public Optional<Order> getOrderByNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    /**
     * 根据状态获取订单
     * @param status 状态
     * @return 订单列表
     */
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * 根据客户姓名搜索订单
     * @param customerName 客户姓名
     * @return 订单列表
     */
    public List<Order> searchOrdersByCustomerName(String customerName) {
        return orderRepository.findByCustomerNameContaining(customerName);
    }

    /**
     * 获取指定时间范围内创建的订单
     * @param start 开始时间
     * @param end 结束时间
     * @return 订单列表
     */
    public List<Order> getOrdersByTimeRange(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByCreateTimeBetween(start, end);
    }

    /**
     * 创建订单
     * @param order 订单信息
     * @return 创建的订单
     */
    @Transactional
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    /**
     * 更新订单信息
     * @param id 订单ID
     * @param order 订单信息
     * @return 更新后的订单
     */
    @Transactional
    public Order updateOrder(Long id, Order order) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        existing.setOrderNo(order.getOrderNo());
        existing.setCustomerName(order.getCustomerName());
        existing.setCustomerPhone(order.getCustomerPhone());
        existing.setAddress(order.getAddress());
        existing.setAmount(order.getAmount());
        existing.setStatus(order.getStatus());
        existing.setShootingDate(order.getShootingDate());
        existing.setCompletionDate(order.getCompletionDate());
        existing.setRemark(order.getRemark());
        
        return orderRepository.save(existing);
    }

    /**
     * 删除订单
     * @param id 订单ID
     */
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("订单不存在");
        }
        orderRepository.deleteById(id);
    }

    /**
     * 更新订单状态
     * @param id 订单ID
     * @param status 状态
     * @return 更新后的订单
     */
    @Transactional
    public Order updateStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        order.setStatus(status);
        
        if (status == OrderStatus.COMPLETED) {
            order.setCompletionDate(LocalDateTime.now());
        }
        
        return orderRepository.save(order);
    }

    /**
     * 标记订单为完成
     * @param id 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public Order completeOrder(Long id) {
        return updateStatus(id, OrderStatus.COMPLETED);
    }

    /**
     * 标记订单为配送中
     * @param id 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public Order markAsDelivering(Long id) {
        return updateStatus(id, OrderStatus.DELIVERING);
    }

    /**
     * 取消订单
     * @param id 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public Order cancelOrder(Long id) {
        return updateStatus(id, OrderStatus.CANCELLED);
    }
}
