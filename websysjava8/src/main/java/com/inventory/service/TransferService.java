package com.inventory.service;

import com.inventory.entity.TransferOrder;
import com.inventory.entity.TransferOrderItem;
import com.inventory.repository.TransferOrderItemRepository;
import com.inventory.repository.TransferOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 调拨管理Service类
 * 处理库存调拨的全流程：申请 -> 审核 -> 出库 -> 入库 -> 完成
 */
@Service
public class TransferService {

    @Autowired
    private TransferOrderRepository transferOrderRepository;

    @Autowired
    private TransferOrderItemRepository transferOrderItemRepository;

    @Autowired
    private InventoryService inventoryService;

    /**
     * 查询所有调拨单
     */
    public List<TransferOrder> findAll() {
        return transferOrderRepository.findAll();
    }

    /**
     * 根据ID查询
     */
    public TransferOrder findById(Long id) {
        Optional<TransferOrder> optional = transferOrderRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 根据单号查询
     */
    public TransferOrder findByOrderNo(String orderNo) {
        return transferOrderRepository.findByOrderNo(orderNo);
    }

    /**
     * 根据状态查询
     */
    public List<TransferOrder> findByStatus(Integer status) {
        return transferOrderRepository.findByStatus(status);
    }

    /**
     * 创建调拨单（申请）
     */
    @Transactional(rollbackFor = Exception.class)
    public TransferOrder createOrder(TransferOrder order, List<TransferOrderItem> items) {
        // 生成调拨单号
        String orderNo = "DB" + System.currentTimeMillis();
        order.setOrderNo(orderNo);
        order.setStatus(0);
        order.setApplyTime(LocalDateTime.now());

        // 计算总数量
        int totalQty = 0;
        for (TransferOrderItem item : items) {
            totalQty += item.getQuantity();
        }
        order.setTotalQuantity(totalQty);

        TransferOrder savedOrder = transferOrderRepository.save(order);

        // 保存明细
        for (TransferOrderItem item : items) {
            item.setOrderId(savedOrder.getId());
            transferOrderItemRepository.save(item);
        }

        return savedOrder;
    }

    /**
     * 审核调拨单
     */
    @Transactional(rollbackFor = Exception.class)
    public TransferOrder auditOrder(Long orderId, String auditor, boolean approved) {
        TransferOrder order = transferOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("调拨单不存在"));

        if (order.getStatus() != 0) {
            throw new RuntimeException("只有待审核的调拨单可以审核");
        }

        if (approved) {
            order.setStatus(1);
            order.setAuditor(auditor);
            order.setAuditTime(LocalDateTime.now());
        } else {
            order.setStatus(4);
        }

        return transferOrderRepository.save(order);
    }

    /**
     * 执行出库
     */
    @Transactional(rollbackFor = Exception.class)
    public TransferOrder executeOutbound(Long orderId, String outboundPerson) {
        TransferOrder order = transferOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("调拨单不存在"));

        if (order.getStatus() != 1) {
            throw new RuntimeException("只有已审核的调拨单可以出库");
        }

        List<TransferOrderItem> items = transferOrderItemRepository.findByOrderId(orderId);
        
        // 按先进先出执行出库
        for (TransferOrderItem item : items) {
            // 从调出位置出库
            int outboundQty = inventoryService.outboundFIFO(
                    order.getFromLocationType(),
                    order.getFromLocationId(),
                    item.getSeedBatch() != null ? item.getSeedBatch().getVarietyId() : 
                            getVarietyIdFromBatch(item.getBatchId()),
                    item.getQuantity()
            );
            item.setOutboundQuantity(outboundQty);
            transferOrderItemRepository.save(item);
        }

        order.setStatus(2);
        order.setOutboundPerson(outboundPerson);
        order.setOutboundTime(LocalDateTime.now());

        return transferOrderRepository.save(order);
    }

    /**
     * 执行入库
     */
    @Transactional(rollbackFor = Exception.class)
    public TransferOrder executeInbound(Long orderId, String inboundPerson) {
        TransferOrder order = transferOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("调拨单不存在"));

        if (order.getStatus() != 2) {
            throw new RuntimeException("只有出库中的调拨单可以入库");
        }

        List<TransferOrderItem> items = transferOrderItemRepository.findByOrderId(orderId);
        
        // 执行入库
        for (TransferOrderItem item : items) {
            int inboundQty = item.getOutboundQuantity();
            inventoryService.inbound(
                    order.getToLocationType(),
                    order.getToLocationId(),
                    item.getBatchId(),
                    inboundQty,
                    order.getOrderNo()
            );
            item.setInboundQuantity(inboundQty);
            transferOrderItemRepository.save(item);
        }

        order.setStatus(3);
        order.setInboundPerson(inboundPerson);
        order.setInboundTime(LocalDateTime.now());

        return transferOrderRepository.save(order);
    }

    /**
     * 取消调拨单
     */
    @Transactional(rollbackFor = Exception.class)
    public TransferOrder cancelOrder(Long orderId) {
        TransferOrder order = transferOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("调拨单不存在"));

        if (order.getStatus() >= 2) {
            throw new RuntimeException("已出库的调拨单不能取消");
        }

        order.setStatus(4);
        return transferOrderRepository.save(order);
    }

    /**
     * 查询调拨单明细
     */
    public List<TransferOrderItem> findItemsByOrderId(Long orderId) {
        return transferOrderItemRepository.findByOrderId(orderId);
    }

    /**
     * 检查单号是否存在
     */
    public boolean existsByOrderNo(String orderNo) {
        return transferOrderRepository.existsByOrderNo(orderNo);
    }

    /**
     * 从批次ID获取品种ID（简化实现）
     */
    private Long getVarietyIdFromBatch(Long batchId) {
        return batchId;
    }
}
