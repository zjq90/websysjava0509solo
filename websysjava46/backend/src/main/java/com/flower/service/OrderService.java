package com.flower.service;

import com.flower.entity.AbnormalOrder;
import com.flower.entity.Order;
import com.flower.entity.OrderItem;
import com.flower.repository.AbnormalOrderRepository;
import com.flower.repository.OrderItemRepository;
import com.flower.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单服务类
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AbnormalOrderRepository abnormalOrderRepository;

    /**
     * 分页查询订单
     */
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    /**
     * 根据ID查询订单
     */
    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    /**
     * 根据订单编号查询
     */
    public Order findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    /**
     * 保存订单
     */
    @Transactional
    public Order save(Order order) {
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                item.setOrder(order);
            }
        }
        return orderRepository.save(order);
    }

    /**
     * 订单审核/接单
     */
    @Transactional
    public Order acceptOrder(Long id, String remark) {
        Order order = findById(id);
        if (order != null) {
            order.setStatus(2);
            if (remark != null) {
                order.setRemark(remark);
            }
            return orderRepository.save(order);
        }
        return null;
    }

    /**
     * 更新订单状态
     */
    @Transactional
    public Order updateStatus(Long id, Integer status) {
        Order order = findById(id);
        if (order != null) {
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    /**
     * 创建异常订单
     */
    @Transactional
    public AbnormalOrder createAbnormalOrder(Long orderId, Integer type, String reason, BigDecimal amount) {
        Order order = findById(orderId);
        if (order == null) {
            return null;
        }

        AbnormalOrder abnormalOrder = new AbnormalOrder();
        abnormalOrder.setOrderId(orderId);
        abnormalOrder.setOrderNo(order.getOrderNo());
        abnormalOrder.setType(type);
        abnormalOrder.setReason(reason);
        abnormalOrder.setAmount(amount);
        abnormalOrder.setStatus(0);

        order.setStatus(7);
        orderRepository.save(order);

        return abnormalOrderRepository.save(abnormalOrder);
    }

    /**
     * 处理异常订单
     */
    @Transactional
    public AbnormalOrder handleAbnormalOrder(Long abnormalId, String result, String handler) {
        AbnormalOrder abnormalOrder = abnormalOrderRepository.findById(abnormalId).orElse(null);
        if (abnormalOrder != null) {
            abnormalOrder.setStatus(2);
            abnormalOrder.setResult(result);
            abnormalOrder.setHandler(handler);
            abnormalOrder.setHandleTime(LocalDateTime.now());
            return abnormalOrderRepository.save(abnormalOrder);
        }
        return null;
    }

    /**
     * 获取订单明细
     */
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    /**
     * 获取异常订单列表
     */
    public List<AbnormalOrder> getAbnormalOrders(Integer status) {
        if (status != null) {
            return abnormalOrderRepository.findByStatus(status);
        }
        return abnormalOrderRepository.findAll();
    }
}
