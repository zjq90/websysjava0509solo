package com.vending.service;

import com.vending.entity.RestockOrder;
import com.vending.entity.RestockOrderItem;
import com.vending.entity.Slot;
import com.vending.entity.VendingMachine;
import com.vending.repository.RestockOrderRepository;
import com.vending.repository.SlotRepository;
import com.vending.repository.VendingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 补货服务类
 * 提供智能补货、生成补货单、执行补货等功能
 */
@Service
public class RestockService {
    
    @Autowired
    private RestockOrderRepository restockRepository;
    
    @Autowired
    private VendingMachineRepository machineRepository;
    
    @Autowired
    private SlotRepository slotRepository;
    
    /**
     * 获取所有补货单
     */
    public List<RestockOrder> findAll() {
        return restockRepository.findAll();
    }
    
    /**
     * 根据ID查询补货单
     */
    public Optional<RestockOrder> findById(Long id) {
        return restockRepository.findById(id);
    }
    
    /**
     * 检查库存并自动生成补货单（智能补货）
     * 当库存低于商品设置的阈值时自动创建补货单
     */
    @Transactional
    public int autoGenerateRestockOrders() {
        int generatedCount = 0;
        List<Slot> lowStockSlots = slotRepository.findAllLowStockSlots();
        
        for (Slot slot : lowStockSlots) {
            if (slot.getProduct() == null || slot.getProduct().getStockThreshold() == null) {
                continue;
            }
            
            if (slot.getCurrentStock() > slot.getProduct().getStockThreshold()) {
                continue;
            }
            
            Long machineId = slot.getMachine().getId();
            Optional<RestockOrder> existing = restockRepository.findPendingByMachineId(machineId);
            
            RestockOrder order;
            if (existing.isPresent()) {
                order = existing.get();
            } else {
                order = new RestockOrder();
                order.setRestockNo(generateRestockNo());
                order.setMachine(slot.getMachine());
                order.setStatus("PENDING");
                order.setCreateType("AUTO");
                order.setNotified(false);
                generatedCount++;
            }
            
            boolean itemExists = order.getItems().stream()
                .anyMatch(item -> item.getSlot() != null && item.getSlot().getId().equals(slot.getId()));
            
            if (!itemExists) {
                RestockOrderItem item = new RestockOrderItem();
                item.setRestockOrder(order);
                item.setProduct(slot.getProduct());
                item.setProductName(slot.getProduct().getName());
                item.setSlot(slot);
                item.setSlotNumber(slot.getSlotNumber());
                item.setBeforeStock(slot.getCurrentStock());
                int quantity = slot.getMaxCapacity() - slot.getCurrentStock();
                item.setRestockQuantity(quantity > 0 ? quantity : slot.getMaxCapacity());
                item.setAfterStock(slot.getCurrentStock() + item.getRestockQuantity());
                order.getItems().add(item);
            }
            
            restockRepository.save(order);
        }
        
        return generatedCount;
    }
    
    /**
     * 手动创建补货单
     */
    @Transactional
    public RestockOrder createManualRestock(Long machineId, List<Long> slotIds, String operator, String remark) {
        VendingMachine machine = machineRepository.findById(machineId)
            .orElseThrow(() -> new RuntimeException("设备不存在"));
        
        RestockOrder order = new RestockOrder();
        order.setRestockNo(generateRestockNo());
        order.setMachine(machine);
        order.setStatus("PENDING");
        order.setCreateType("MANUAL");
        order.setOperator(operator);
        order.setRemark(remark);
        order.setNotified(false);
        
        for (Long slotId : slotIds) {
            Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("货道不存在"));
            
            if (!slot.getMachine().getId().equals(machineId)) {
                throw new RuntimeException("货道不属于目标设备");
            }
            
            if (slot.getProduct() == null) {
                continue;
            }
            
            RestockOrderItem item = new RestockOrderItem();
            item.setRestockOrder(order);
            item.setProduct(slot.getProduct());
            item.setProductName(slot.getProduct().getName());
            item.setSlot(slot);
            item.setSlotNumber(slot.getSlotNumber());
            item.setBeforeStock(slot.getCurrentStock());
            int quantity = slot.getMaxCapacity() - slot.getCurrentStock();
            item.setRestockQuantity(quantity > 0 ? quantity : slot.getMaxCapacity());
            item.setAfterStock(slot.getCurrentStock() + item.getRestockQuantity());
            order.getItems().add(item);
        }
        
        return restockRepository.save(order);
    }
    
    /**
     * 执行补货
     */
    @Transactional
    public RestockOrder completeRestock(Long restockId, String operator) {
        RestockOrder order = restockRepository.findById(restockId)
            .orElseThrow(() -> new RuntimeException("补货单不存在"));
        
        if (!"PENDING".equals(order.getStatus()) && !"IN_PROGRESS".equals(order.getStatus())) {
            throw new RuntimeException("补货单状态不允许完成");
        }
        
        for (RestockOrderItem item : order.getItems()) {
            Slot slot = item.getSlot();
            if (slot != null) {
                int newStock = slot.getCurrentStock() + item.getRestockQuantity();
                if (newStock > slot.getMaxCapacity()) {
                    newStock = slot.getMaxCapacity();
                }
                slot.setCurrentStock(newStock);
                slot.setLastRefillTime(LocalDateTime.now());
                slotRepository.save(slot);
                
                item.setAfterStock(newStock);
            }
        }
        
        order.setStatus("COMPLETED");
        order.setOperator(operator);
        order.setCompleteTime(LocalDateTime.now());
        
        return restockRepository.save(order);
    }
    
    /**
     * 取消补货单
     */
    @Transactional
    public RestockOrder cancelRestock(Long restockId, String reason) {
        RestockOrder order = restockRepository.findById(restockId)
            .orElseThrow(() -> new RuntimeException("补货单不存在"));
        
        if (!"PENDING".equals(order.getStatus()) && !"IN_PROGRESS".equals(order.getStatus())) {
            throw new RuntimeException("补货单状态不允许取消");
        }
        
        order.setStatus("CANCELLED");
        if (order.getRemark() != null) {
            order.setRemark(order.getRemark() + " | 取消原因: " + reason);
        } else {
            order.setRemark("取消原因: " + reason);
        }
        
        return restockRepository.save(order);
    }
    
    /**
     * 标记已提醒
     */
    @Transactional
    public RestockOrder markNotified(Long restockId) {
        RestockOrder order = restockRepository.findById(restockId)
            .orElseThrow(() -> new RuntimeException("补货单不存在"));
        
        order.setNotified(true);
        return restockRepository.save(order);
    }
    
    /**
     * 获取未提醒的补货单
     */
    public List<RestockOrder> getUnnotifiedOrders() {
        return restockRepository.findUnnotifiedOrders();
    }
    
    /**
     * 统计待处理补货单数量
     */
    public Long countPendingOrders() {
        return restockRepository.countPendingOrders();
    }
    
    /**
     * 生成补货单号
     */
    private String generateRestockNo() {
        return "RS" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
            + String.format("%03d", (int)(Math.random() * 1000));
    }
}
