package com.vending.service;

import com.vending.entity.Order;
import com.vending.entity.OrderItem;
import com.vending.entity.Product;
import com.vending.entity.Slot;
import com.vending.entity.VendingMachine;
import com.vending.repository.OrderRepository;
import com.vending.repository.ProductRepository;
import com.vending.repository.SlotRepository;
import com.vending.repository.VendingMachineRepository;
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

/**
 * 订单服务类
 * 提供订单的增删改查和支付取货功能
 */
@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private VendingMachineRepository machineRepository;
    
    @Autowired
    private SlotRepository slotRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * 分页查询订单
     */
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
    
    /**
     * 根据条件分页查询订单
     */
    public Page<Order> findByConditions(String paymentStatus, String pickupStatus, Pageable pageable) {
        if (paymentStatus != null && !paymentStatus.isEmpty() && pickupStatus != null && !pickupStatus.isEmpty()) {
            return orderRepository.findByPaymentStatusAndPickupStatus(paymentStatus, pickupStatus, pageable);
        } else if (paymentStatus != null && !paymentStatus.isEmpty()) {
            return orderRepository.findByPaymentStatus(paymentStatus, pageable);
        } else if (pickupStatus != null && !pickupStatus.isEmpty()) {
            return orderRepository.findByPickupStatus(pickupStatus, pageable);
        } else {
            return orderRepository.findAll(pageable);
        }
    }
    
    /**
     * 根据ID查询订单
     */
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    /**
     * 根据订单号查询
     */
    public Optional<Order> findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }
    
    /**
     * 创建订单
     */
    @Transactional
    public Order createOrder(Long machineId, Long productId, Long slotId, Integer quantity, 
                             String userIdentifier, String userName, String userPhone) {
        VendingMachine machine = machineRepository.findById(machineId)
            .orElseThrow(() -> new RuntimeException("售货机不存在"));
        
        Slot slot = slotRepository.findById(slotId)
            .orElseThrow(() -> new RuntimeException("货道不存在"));
        
        if (!slot.getMachine().getId().equals(machineId)) {
            throw new RuntimeException("货道不属于该售货机");
        }
        
        if (slot.getProduct() == null) {
            throw new RuntimeException("该货道未绑定商品");
        }
        
        if (!slot.getProduct().getId().equals(productId)) {
            throw new RuntimeException("货道商品与请求商品不符");
        }
        
        if (slot.getCurrentStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        
        Product product = slot.getProduct();
        
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setMachine(machine);
        order.setUserIdentifier(userIdentifier);
        order.setUserName(userName);
        order.setUserPhone(userPhone);
        order.setPaymentStatus("PENDING");
        order.setPickupStatus("PENDING");
        
        BigDecimal totalAmount = product.getRetailPrice().multiply(BigDecimal.valueOf(quantity));
        order.setTotalAmount(totalAmount);
        
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setProductName(product.getName());
        item.setPrice(product.getRetailPrice());
        item.setQuantity(quantity);
        item.setSubtotal(totalAmount);
        item.setSlot(slot);
        
        order.getItems().add(item);
        
        return orderRepository.save(order);
    }
    
    /**
     * 支付订单
     */
    @Transactional
    public Order payOrder(Long orderId, String paymentMethod) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        if (!"PENDING".equals(order.getPaymentStatus())) {
            throw new RuntimeException("订单状态不允许支付");
        }
        
        order.setPaymentStatus("PAID");
        order.setPaymentMethod(paymentMethod);
        order.setPaymentTransactionId("TXN" + System.currentTimeMillis());
        order.setPaymentTime(LocalDateTime.now());
        
        for (OrderItem item : order.getItems()) {
            Slot slot = item.getSlot();
            if (slot != null) {
                int newStock = slot.getCurrentStock() - item.getQuantity();
                if (newStock < 0) {
                    throw new RuntimeException("库存不足，扣减失败");
                }
                slot.setCurrentStock(newStock);
                slotRepository.save(slot);
            }
        }
        
        return orderRepository.save(order);
    }
    
    /**
     * 确认取货
     */
    @Transactional
    public Order confirmPickup(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        if (!"PAID".equals(order.getPaymentStatus())) {
            throw new RuntimeException("订单未支付");
        }
        
        if (!"PENDING".equals(order.getPickupStatus())) {
            throw new RuntimeException("订单已完成取货或失败");
        }
        
        order.setPickupStatus("PICKED");
        order.setPickupTime(LocalDateTime.now());
        
        return orderRepository.save(order);
    }
    
    /**
     * 取消订单
     */
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        if ("PAID".equals(order.getPaymentStatus())) {
            throw new RuntimeException("已支付订单不能取消，请申请退款");
        }
        
        if (!"PENDING".equals(order.getPaymentStatus())) {
            throw new RuntimeException("订单状态不允许取消");
        }
        
        order.setPaymentStatus("CANCELLED");
        order.setPickupStatus("CANCELLED");
        
        return orderRepository.save(order);
    }
    
    /**
     * 根据设备ID查询订单
     */
    public Page<Order> findByMachineId(Long machineId, Pageable pageable) {
        return orderRepository.findByMachineId(machineId, pageable);
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) 
            + String.format("%03d", (int)(Math.random() * 1000));
    }
}
