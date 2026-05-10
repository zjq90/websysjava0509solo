package com.appsys.service;

import com.appsys.dto.OrderCreateRequest;
import com.appsys.entity.*;
import com.appsys.exception.ResourceNotFoundException;
import com.appsys.repository.CustomerRepository;
import com.appsys.repository.LogisticsTrackingRepository;
import com.appsys.repository.ProductRepository;
import com.appsys.repository.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 销售订单服务类
 * 处理订单的创建、状态流转、价格计算等核心业务逻辑
 * 支持完整的订单闭环流程：创建->确认->签署->ERP同步->备货->发货->签收->完成
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Service
public class SalesOrderService {

    @Autowired
    private SalesOrderRepository orderRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private LogisticsTrackingRepository logisticsRepository;
    
    @Autowired
    private CustomerService customerService;

    private static final AtomicLong orderCounter = new AtomicLong(1);
    private static final AtomicLong contractCounter = new AtomicLong(1);

    /**
     * 为订单注入客户名称
     * @param order 订单
     */
    private void populateCustomerName(SalesOrder order) {
        if (order != null && order.getCustomerId() != null) {
            Customer customer = customerRepository.findById(order.getCustomerId()).orElse(null);
            if (customer != null) {
                order.setCustomerName(customer.getName());
            }
        }
    }

    /**
     * 为订单列表注入客户名称
     * @param orders 订单列表
     */
    private void populateCustomerNames(List<SalesOrder> orders) {
        if (orders != null) {
            orders.forEach(this::populateCustomerName);
        }
    }

    /**
     * 获取所有订单
     * @return 订单列表
     */
    public List<SalesOrder> getAllOrders() {
        List<SalesOrder> orders = orderRepository.findAllOrderByCreatedAtDesc();
        populateCustomerNames(orders);
        return orders;
    }

    /**
     * 根据ID获取订单
     * @param id 订单ID
     * @return 订单
     */
    public SalesOrder getOrderById(Long id) {
        SalesOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在，ID: " + id));
        populateCustomerName(order);
        return order;
    }

    /**
     * 根据订单编号获取订单
     * @param orderNo 订单编号
     * @return 订单
     */
    public SalesOrder getOrderByOrderNo(String orderNo) {
        SalesOrder order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在，编号: " + orderNo));
        populateCustomerName(order);
        return order;
    }

    /**
     * 创建订单
     * 业务流程：业务员现场选品 -> 系统根据客户等级自动匹配价格策略 -> 生成订单
     * 
     * @param request 订单创建请求
     * @return 创建后的订单
     */
    @Transactional
    public SalesOrder createOrder(OrderCreateRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("客户不存在，ID: " + request.getCustomerId()));

        BigDecimal discountRate = customer.getLevel().getDiscountRate();

        SalesOrder order = new SalesOrder();
        order.setOrderNo(generateOrderNo());
        order.setCustomerId(customer.getId());
        order.setStatus(OrderStatus.PENDING_CONFIRMATION);
        order.setCustomerLevelAtOrder(customer.getLevel());
        order.setAppliedDiscountRate(discountRate);
        order.setSalespersonId(request.getSalespersonId());
        order.setSalespersonName(request.getSalespersonName());
        order.setShippingAddress(request.getShippingAddress());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverName(request.getReceiverName());
        order.setRemarks(request.getRemarks());

        List<OrderItem> items = new ArrayList<>();
        BigDecimal originalTotal = BigDecimal.ZERO;
        BigDecimal actualTotal = BigDecimal.ZERO;

        for (OrderCreateRequest.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("产品不存在，ID: " + itemRequest.getProductId()));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("产品库存不足: " + product.getName());
            }

            BigDecimal basePrice = product.getBasePrice();
            BigDecimal actualPrice = basePrice.multiply(discountRate);
            
            BigDecimal originalLineTotal = basePrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            BigDecimal actualLineTotal = actualPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setBatchNumber(product.getBatchNumber());
            item.setSpecification(product.getSpecification());
            item.setBasePrice(basePrice);
            item.setActualPrice(actualPrice);
            item.setQuantity(itemRequest.getQuantity());
            item.setOriginalLineTotal(originalLineTotal);
            item.setActualLineTotal(actualLineTotal);
            
            items.add(item);
            originalTotal = originalTotal.add(originalLineTotal);
            actualTotal = actualTotal.add(actualLineTotal);
        }

        order.setItems(items);
        order.setOriginalAmount(originalTotal);
        order.setActualAmount(actualTotal);
        order.setDiscountAmount(originalTotal.subtract(actualTotal));
        order.setCustomerName(customer.getName());

        return orderRepository.save(order);
    }

    /**
     * 确认订单（等待签署）
     * @param orderId 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public SalesOrder confirmOrder(Long orderId) {
        SalesOrder order = getOrderById(orderId);
        
        if (order.getStatus() != OrderStatus.PENDING_CONFIRMATION) {
            throw new IllegalStateException("只有待确认的订单才能确认");
        }
        
        order.setStatus(OrderStatus.PENDING_SIGNATURE);
        SalesOrder saved = orderRepository.save(order);
        populateCustomerName(saved);
        return saved;
    }

    /**
     * 客户签署电子合同
     * 业务流程：客户扫码签署
     * 
     * @param orderId 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public SalesOrder signContract(Long orderId) {
        SalesOrder order = getOrderById(orderId);
        
        if (order.getStatus() != OrderStatus.PENDING_SIGNATURE) {
            throw new IllegalStateException("只有待签署的订单才能签署合同");
        }
        
        order.setStatus(OrderStatus.SIGNED);
        order.setContractNo(generateContractNo());
        order.setSignedAt(LocalDateTime.now());
        
        SalesOrder saved = orderRepository.save(order);
        populateCustomerName(saved);
        return saved;
    }

    /**
     * 同步到ERP系统
     * 业务流程：同步至ERP系统 -> 触发仓库备货
     * 
     * @param orderId 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public SalesOrder syncToErp(Long orderId) {
        SalesOrder order = getOrderById(orderId);
        
        if (order.getStatus() != OrderStatus.SIGNED) {
            throw new IllegalStateException("只有已签署的订单才能同步到ERP");
        }
        
        order.setStatus(OrderStatus.STOCK_PREPARING);
        order.setErpSyncedAt(LocalDateTime.now());

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
                productRepository.save(product);
            }
        }
        
        SalesOrder saved = orderRepository.save(order);
        populateCustomerName(saved);
        return saved;
    }

    /**
     * 发货（简化版，自动创建物流记录）
     * @param orderId 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public SalesOrder shipOrder(Long orderId) {
        LogisticsTracking logisticsInfo = new LogisticsTracking();
        logisticsInfo.setCompanyName("顺丰速运");
        logisticsInfo.setCompanyCode("SF");
        logisticsInfo.setTrackingNo("SF" + System.currentTimeMillis());
        logisticsInfo.setCurrentLocation("已出库，等待揽收");
        return shipOrder(orderId, logisticsInfo);
    }

    /**
     * 发货
     * @param orderId 订单ID
     * @param logisticsInfo 物流信息
     * @return 更新后的订单
     */
    @Transactional
    public SalesOrder shipOrder(Long orderId, LogisticsTracking logisticsInfo) {
        SalesOrder order = getOrderById(orderId);
        
        if (order.getStatus() != OrderStatus.STOCK_PREPARING) {
            throw new IllegalStateException("只有备货中的订单才能发货");
        }
        
        order.setStatus(OrderStatus.SHIPPED);
        order.setShippedAt(LocalDateTime.now());
        
        logisticsInfo.setOrderId(orderId);
        logisticsInfo.setStatus(LogisticsStatus.IN_TRANSIT);
        logisticsRepository.save(logisticsInfo);
        
        SalesOrder saved = orderRepository.save(order);
        populateCustomerName(saved);
        return saved;
    }

    /**
     * 签收
     * @param orderId 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public SalesOrder deliverOrder(Long orderId) {
        SalesOrder order = getOrderById(orderId);
        
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalStateException("只有已发货的订单才能签收");
        }
        
        order.setStatus(OrderStatus.DELIVERED);
        order.setDeliveredAt(LocalDateTime.now());
        
        LogisticsTracking logistics = logisticsRepository.findByOrderId(orderId).orElse(null);
        if (logistics != null) {
            logistics.setStatus(LogisticsStatus.DELIVERED);
            logisticsRepository.save(logistics);
        }
        
        customerService.updatePurchaseAmount(order.getCustomerId(), order.getActualAmount());
        
        SalesOrder saved = orderRepository.save(order);
        populateCustomerName(saved);
        return saved;
    }

    /**
     * 完成订单
     * @param orderId 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public SalesOrder completeOrder(Long orderId) {
        SalesOrder order = getOrderById(orderId);
        
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException("只有已签收的订单才能完成");
        }
        
        order.setStatus(OrderStatus.COMPLETED);
        SalesOrder saved = orderRepository.save(order);
        populateCustomerName(saved);
        return saved;
    }

    /**
     * 取消订单
     * @param orderId 订单ID
     * @return 更新后的订单
     */
    @Transactional
    public SalesOrder cancelOrder(Long orderId) {
        SalesOrder order = getOrderById(orderId);
        
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("该订单无法取消");
        }
        
        if (order.getStatus() == OrderStatus.STOCK_PREPARING || order.getStatus() == OrderStatus.SHIPPED) {
            for (OrderItem item : order.getItems()) {
                Product product = productRepository.findById(item.getProductId()).orElse(null);
                if (product != null) {
                    product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                    productRepository.save(product);
                }
            }
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        SalesOrder saved = orderRepository.save(order);
        populateCustomerName(saved);
        return saved;
    }

    /**
     * 根据状态获取订单列表
     * @param status 订单状态
     * @return 订单列表
     */
    public List<SalesOrder> getOrdersByStatus(OrderStatus status) {
        List<SalesOrder> orders = orderRepository.findByStatusOrderByCreatedAtDesc(status);
        populateCustomerNames(orders);
        return orders;
    }

    /**
     * 获取客户历史订单
     * @param customerId 客户ID
     * @return 订单列表
     */
    public List<SalesOrder> getCustomerOrders(Long customerId) {
        List<SalesOrder> orders = orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        populateCustomerNames(orders);
        return orders;
    }

    /**
     * 生成订单编号
     * 格式：SO + 年月日 + 4位序号
     * @return 订单编号
     */
    private String generateOrderNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = orderCounter.getAndIncrement();
        return "SO" + date + String.format("%04d", seq);
    }

    /**
     * 生成合同编号
     * 格式：CT + 年月日 + 4位序号
     * @return 合同编号
     */
    private String generateContractNo() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = contractCounter.getAndIncrement();
        return "CT" + date + String.format("%04d", seq);
    }

    /**
     * 根据业务员ID获取订单
     * @param salespersonId 业务员ID
     * @return 订单列表
     */
    public List<SalesOrder> getOrdersBySalesperson(Long salespersonId) {
        List<SalesOrder> orders = orderRepository.findBySalespersonIdOrderByCreatedAtDesc(salespersonId);
        populateCustomerNames(orders);
        return orders;
    }

    /**
     * 统计各状态订单数量
     * @return 统计信息
     */
    public java.util.Map<String, Long> getOrderStatusCounts() {
        java.util.Map<String, Long> counts = new java.util.HashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            counts.put(status.name(), orderRepository.countByStatus(status));
        }
        return counts;
    }
}
