package com.photostudio.service;

import com.photostudio.entity.Express;
import com.photostudio.entity.Express.ExpressStatus;
import com.photostudio.entity.Order;
import com.photostudio.entity.Order.OrderStatus;
import com.photostudio.repository.ExpressRepository;
import com.photostudio.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 快递服务类
 * 提供快递管理相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class ExpressService {

    private final ExpressRepository expressRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public ExpressService(ExpressRepository expressRepository, OrderRepository orderRepository) {
        this.expressRepository = expressRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * 获取所有快递信息
     * @return 快递列表
     */
    public List<Express> getAllExpress() {
        return expressRepository.findAll();
    }

    /**
     * 根据ID获取快递信息
     * @param id 快递ID
     * @return 快递信息
     */
    public Optional<Express> getExpressById(Long id) {
        return expressRepository.findById(id);
    }

    /**
     * 根据订单获取快递信息
     * @param orderId 订单ID
     * @return 快递信息
     */
    public Optional<Express> getExpressByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        return expressRepository.findByOrder(order);
    }

    /**
     * 根据快递单号获取快递信息
     * @param trackingNo 快递单号
     * @return 快递信息
     */
    public Optional<Express> getExpressByTrackingNo(String trackingNo) {
        return expressRepository.findByTrackingNo(trackingNo);
    }

    /**
     * 根据状态获取快递列表
     * @param status 状态
     * @return 快递列表
     */
    public List<Express> getExpressByStatus(ExpressStatus status) {
        return expressRepository.findByStatus(status);
    }

    /**
     * 获取未签收的快递
     * @return 快递列表
     */
    public List<Express> getUnSignedExpress() {
        return expressRepository.findByStatusOrderByCreateTimeDesc(ExpressStatus.IN_TRANSIT);
    }

    /**
     * 创建快递信息
     * @param express 快递信息
     * @return 创建的快递
     */
    @Transactional
    public Express createExpress(Express express) {
        Order order = orderRepository.findById(express.getOrder().getId())
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        
        express.setOrder(order);
        Express saved = expressRepository.save(express);
        
        order.setStatus(OrderStatus.DELIVERING);
        orderRepository.save(order);
        
        return saved;
    }

    /**
     * 更新快递信息
     * @param id 快递ID
     * @param express 快递信息
     * @return 更新后的快递
     */
    @Transactional
    public Express updateExpress(Long id, Express express) {
        Express existing = expressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("快递信息不存在"));
        
        if (express.getOrder() != null && express.getOrder().getId() != null) {
            Order order = orderRepository.findById(express.getOrder().getId())
                    .orElseThrow(() -> new RuntimeException("订单不存在"));
            existing.setOrder(order);
        }
        
        existing.setTrackingNo(express.getTrackingNo());
        existing.setCompany(express.getCompany());
        existing.setReceiverName(express.getReceiverName());
        existing.setReceiverPhone(express.getReceiverPhone());
        existing.setReceiverAddress(express.getReceiverAddress());
        existing.setStatus(express.getStatus());
        existing.setTrackingInfo(express.getTrackingInfo());
        existing.setRemark(express.getRemark());
        
        return expressRepository.save(existing);
    }

    /**
     * 删除快递信息
     * @param id 快递ID
     */
    @Transactional
    public void deleteExpress(Long id) {
        if (!expressRepository.existsById(id)) {
            throw new RuntimeException("快递信息不存在");
        }
        expressRepository.deleteById(id);
    }

    /**
     * 更新物流状态
     * @param id 快递ID
     * @param status 状态
     * @param trackingInfo 物流轨迹信息
     * @return 更新后的快递
     */
    @Transactional
    public Express updateStatus(Long id, ExpressStatus status, String trackingInfo) {
        Express express = expressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("快递信息不存在"));
        
        express.setStatus(status);
        if (trackingInfo != null) {
            express.setTrackingInfo(trackingInfo);
        }
        
        if (status == ExpressStatus.SIGNED) {
            express.setSignTime(LocalDateTime.now());
            
            Order order = express.getOrder();
            order.setStatus(OrderStatus.COMPLETED);
            order.setCompletionDate(LocalDateTime.now());
            orderRepository.save(order);
        }
        
        return expressRepository.save(express);
    }

    /**
     * 模拟更新物流信息（用于测试）
     * @param id 快递ID
     * @return 更新后的快递
     */
    @Transactional
    public Express simulateTrackingUpdate(Long id) {
        Express express = expressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("快递信息不存在"));
        
        String currentTracking = express.getTrackingInfo() != null ? express.getTrackingInfo() : "";
        LocalDateTime now = LocalDateTime.now();
        
        switch (express.getStatus()) {
            case PENDING:
                express.setStatus(ExpressStatus.IN_TRANSIT);
                express.setTrackingInfo(currentTracking + now + " 包裹已揽收\n");
                express.setShipTime(now);
                break;
            case IN_TRANSIT:
                express.setStatus(ExpressStatus.DELIVERING);
                express.setTrackingInfo(currentTracking + now + " 包裹正在派送中\n");
                break;
            case DELIVERING:
                express.setStatus(ExpressStatus.SIGNED);
                express.setTrackingInfo(currentTracking + now + " 已签收\n");
                express.setSignTime(now);
                
                Order order = express.getOrder();
                order.setStatus(OrderStatus.COMPLETED);
                order.setCompletionDate(now);
                orderRepository.save(order);
                break;
            default:
                break;
        }
        
        return expressRepository.save(express);
    }

    /**
     * 标记为已签收
     * @param id 快递ID
     * @return 更新后的快递
     */
    @Transactional
    public Express markAsSigned(Long id) {
        return updateStatus(id, ExpressStatus.SIGNED, null);
    }
}
