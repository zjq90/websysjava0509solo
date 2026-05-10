package com.appsys.order.service;

import com.appsys.common.exception.BusinessException;
import com.appsys.common.result.PageResult;
import com.appsys.common.util.AESUtil;
import com.appsys.inventory.entity.Inventory;
import com.appsys.inventory.repository.InventoryRepository;
import com.appsys.order.dto.OrderDTO;
import com.appsys.order.dto.OrderItemDTO;
import com.appsys.order.entity.Customer;
import com.appsys.order.entity.Order;
import com.appsys.order.entity.OrderItem;
import com.appsys.order.repository.CustomerRepository;
import com.appsys.order.repository.OrderRepository;
import com.appsys.system.entity.SysUser;
import com.appsys.system.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单管理服务类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private AESUtil aesUtil;

    // 订单号计数器
    private final AtomicLong orderCounter = new AtomicLong(1);

    /**
     * 分页查询订单列表
     */
    public PageResult<Order> list(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Order> orderPage;
        
        if (StringUtils.hasText(keyword)) {
            orderPage = orderRepository.searchByKeyword(keyword, pageable);
        } else {
            orderPage = orderRepository.findByDeletedFalseOrderByCreatedTimeDesc(pageable);
        }
        
        // 处理客户手机号脱敏
        orderPage.getContent().forEach(order -> {
            if (order.getCustomer() != null && order.getCustomer().getPhone() != null) {
                order.getCustomer().setPhone(aesUtil.decryptPhone(order.getCustomer().getPhone(), true));
            }
        });
        
        return PageResult.of(orderPage);
    }

    /**
     * 根据ID查询订单详情
     */
    public Order getById(Long id) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        
        // 处理客户手机号脱敏
        if (order.getCustomer() != null && order.getCustomer().getPhone() != null) {
            order.getCustomer().setPhone(aesUtil.decryptPhone(order.getCustomer().getPhone(), true));
        }
        
        return order;
    }

    /**
     * 创建订单
     */
    @Transactional
    public Order create(OrderDTO dto) {
        // 检查客户是否存在
        Customer customer = customerRepository.findByIdAndDeletedFalse(dto.getCustomerId())
                .orElseThrow(() -> new BusinessException("客户不存在"));

        // 获取当前登录用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        SysUser salesman = userRepository.findByUsernameAndDeletedFalse(username).orElse(null);

        // 生成订单号
        String orderNo = generateOrderNo();

        // 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setCustomerId(dto.getCustomerId());
        order.setCustomerName(customer.getCustomerName());
        order.setOrderDate(LocalDateTime.now());
        order.setDiscountAmount(dto.getDiscountAmount() != null ? dto.getDiscountAmount() : BigDecimal.ZERO);
        order.setStatus(1);
        order.setRemark(dto.getRemark());
        
        if (salesman != null) {
            order.setSalesmanId(salesman.getId());
            order.setSalesmanName(salesman.getRealName() != null ? salesman.getRealName() : salesman.getUsername());
        }

        // 计算订单金额
        BigDecimal orderAmount = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemDTO itemDTO : dto.getItems()) {
            // 检查库存是否存在
            Inventory inventory = inventoryRepository.findByIdAndDeletedFalse(itemDTO.getInventoryId())
                    .orElseThrow(() -> new BusinessException("库存不存在"));

            // 检查库存是否充足
            if (inventory.getRemainingQuantity().compareTo(itemDTO.getQuantity()) < 0) {
                throw new BusinessException("库存不足，批次号：" + inventory.getBatchNo());
            }

            // 扣减库存
            inventory.setRemainingQuantity(inventory.getRemainingQuantity().subtract(itemDTO.getQuantity()));
            inventoryRepository.save(inventory);

            // 创建订单明细
            OrderItem item = new OrderItem();
            item.setInventoryId(itemDTO.getInventoryId());
            item.setBatchNo(inventory.getBatchNo());
            item.setSeedName(inventory.getSeedName());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setAmount(itemDTO.getQuantity().multiply(itemDTO.getUnitPrice()));
            item.setRemark(itemDTO.getRemark());
            items.add(item);

            // 累加订单金额
            orderAmount = orderAmount.add(item.getAmount());
        }

        order.setOrderAmount(orderAmount);
        order.setActualAmount(orderAmount.subtract(order.getDiscountAmount()));
        order.setItems(items);

        return orderRepository.save(order);
    }

    /**
     * 更新订单状态
     */
    @Transactional
    public Order updateStatus(Long id, Integer status) {
        Order order = getById(id);
        
        if (status < 1 || status > 4) {
            throw new BusinessException("无效的订单状态");
        }
        
        // 如果是取消订单，需要返还库存
        if (status == 4 && order.getStatus() != 4) {
            for (OrderItem item : order.getItems()) {
                Inventory inventory = inventoryRepository.findByIdAndDeletedFalse(item.getInventoryId()).orElse(null);
                if (inventory != null) {
                    inventory.setRemainingQuantity(inventory.getRemainingQuantity().add(item.getQuantity()));
                    inventoryRepository.save(inventory);
                }
            }
        }
        
        order.setStatus(status);
        return orderRepository.save(order);
    }

    /**
     * 删除订单（逻辑删除）
     */
    @Transactional
    public void delete(Long id) {
        Order order = getById(id);
        
        // 只有待发货状态的订单可以删除
        if (order.getStatus() != 1 && order.getStatus() != 4) {
            throw new BusinessException("只有待发货或已取消的订单可以删除");
        }
        
        // 如果是待发货订单，返还库存
        if (order.getStatus() == 1) {
            for (OrderItem item : order.getItems()) {
                Inventory inventory = inventoryRepository.findByIdAndDeletedFalse(item.getInventoryId()).orElse(null);
                if (inventory != null) {
                    inventory.setRemainingQuantity(inventory.getRemainingQuantity().add(item.getQuantity()));
                    inventoryRepository.save(inventory);
                }
            }
        }
        
        order.setDeleted(true);
        orderRepository.save(order);
    }

    /**
     * 生成订单号
     * 格式：ORD + yyyyMMdd + 4位序号
     */
    private String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long seq = orderCounter.getAndIncrement();
        // 重置序号，防止溢出
        if (seq > 9999) {
            orderCounter.set(1);
            seq = 1;
        }
        String seqStr = String.format("%04d", seq);
        String orderNo = "ORD" + dateStr + seqStr;
        
        // 检查是否已存在
        if (orderRepository.existsByOrderNo(orderNo)) {
            return generateOrderNo();
        }
        
        return orderNo;
    }
}
