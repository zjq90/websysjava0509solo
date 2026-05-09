package com.agricultural.service;

import com.agricultural.entity.*;
import com.agricultural.entity.enums.*;
import com.agricultural.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 订单管理服务类
 * 核心功能：
 * 1. 销售订单和采购订单的增删改查
 * 2. 订单与财务单互通互联，自动生成应收应付账款
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final FinanceRepository financeRepository;
    private final FinanceService financeService;

    public OrderService(OrderRepository orderRepository,
                        FinanceRepository financeRepository,
                        FinanceService financeService) {
        this.orderRepository = orderRepository;
        this.financeRepository = financeRepository;
        this.financeService = financeService;
    }

    /**
     * 查询所有订单
     */
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    /**
     * 分页查询所有订单
     */
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    /**
     * 根据订单类型查询
     */
    public List<Order> findByType(OrderType type) {
        return orderRepository.findByOrderTypeOrderByOrderDateDesc(type);
    }

    /**
     * 根据状态查询
     */
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByOrderStatusOrderByOrderDateDesc(status);
    }

    /**
     * 根据ID查询
     */
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * 根据订单编号查询
     */
    public Optional<Order> findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    /**
     * 根据客户ID查询
     */
    public List<Order> findByCustomerId(Long customerId) {
        return orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId);
    }

    /**
     * 生成订单编号
     * 销售订单：XS + 年月日 + 序号
     * 采购订单：CG + 年月日 + 序号
     */
    public String generateOrderNo(OrderType type) {
        String prefix = type == OrderType.SALES ? "XS" : "CG";
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String searchPrefix = prefix + dateStr;
        
        String maxNo = orderRepository.findMaxOrderNoByPrefix(searchPrefix);
        int sequence = 1;
        if (maxNo != null && maxNo.length() > searchPrefix.length()) {
            try {
                sequence = Integer.parseInt(maxNo.substring(searchPrefix.length())) + 1;
            } catch (NumberFormatException e) {
                sequence = 1;
            }
        }
        
        return searchPrefix + String.format("%03d", sequence);
    }

    /**
     * 保存订单
     * 核心功能：自动关联生成财务账款（应收/应付）
     */
    @Transactional
    public Order save(Order order) {
        order.calculateAmounts();
        Order savedOrder = orderRepository.save(order);
        
        if (savedOrder.getFinance() == null && 
            savedOrder.getOrderStatus() != OrderStatus.PENDING &&
            savedOrder.getOrderStatus() != OrderStatus.CANCELLED) {
            Finance finance = createFinanceFromOrder(savedOrder);
            savedOrder.setFinance(finance);
            savedOrder = orderRepository.save(savedOrder);
        }
        
        return savedOrder;
    }

    /**
     * 从订单自动生成财务账款
     * 销售订单 -> 应收账款
     * 采购订单 -> 应付账款
     */
    private Finance createFinanceFromOrder(Order order) {
        Finance finance = new Finance();
        String prefix = order.getOrderType() == OrderType.SALES ? "AR" : "AP";
        finance.setFinanceNo(financeService.generateFinanceNo(
            order.getOrderType() == OrderType.SALES ? FinanceType.RECEIVABLE : FinanceType.PAYABLE));
        finance.setFinanceType(order.getOrderType() == OrderType.SALES ? 
            FinanceType.RECEIVABLE : FinanceType.PAYABLE);
        finance.setStatus(FinanceStatus.UNSETTLED);
        finance.setCustomer(order.getCustomer());
        finance.setFinanceDate(order.getOrderDate());
        finance.setDueDate(order.getOrderDate().plusDays(30));
        finance.setTotalAmount(order.getNetAmount());
        finance.setPaidAmount(BigDecimal.ZERO);
        finance.setRemainingAmount(order.getNetAmount());
        finance.setSummary((order.getOrderType() == OrderType.SALES ? "销售" : "采购") + 
            "订单账款 - " + order.getOrderNo());
        finance.setOrder(order);
        
        return financeRepository.save(finance);
    }

    /**
     * 更新订单状态
     * 状态变更时自动处理财务关联
     */
    @Transactional
    public Order updateStatus(Long id, OrderStatus newStatus) {
        return orderRepository.findById(id).map(order -> {
            OrderStatus oldStatus = order.getOrderStatus();
            order.setOrderStatus(newStatus);
            
            if (order.getFinance() == null && 
                newStatus != OrderStatus.PENDING && 
                newStatus != OrderStatus.CANCELLED) {
                Finance finance = createFinanceFromOrder(order);
                order.setFinance(finance);
            }
            
            return orderRepository.save(order);
        }).orElse(null);
    }

    /**
     * 删除订单
     */
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    /**
     * 统计待处理订单数量
     */
    public long countPendingOrders() {
        return orderRepository.countPendingOrders();
    }

    /**
     * 计算销售总金额
     */
    public BigDecimal getTotalSalesAmount() {
        return orderRepository.sumNetAmountByOrderType(OrderType.SALES);
    }

    /**
     * 计算采购总金额
     */
    public BigDecimal getTotalPurchaseAmount() {
        return orderRepository.sumNetAmountByOrderType(OrderType.PURCHASE);
    }
}
