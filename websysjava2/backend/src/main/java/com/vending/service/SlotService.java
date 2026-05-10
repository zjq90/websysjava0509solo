package com.vending.service;

import com.vending.entity.Product;
import com.vending.entity.Slot;
import com.vending.entity.VendingMachine;
import com.vending.repository.ProductRepository;
import com.vending.repository.SlotRepository;
import com.vending.repository.VendingMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 货道服务类
 * 提供货道绑定商品、库存管理等功能
 */
@Service
public class SlotService {
    
    @Autowired
    private SlotRepository slotRepository;
    
    @Autowired
    private VendingMachineRepository machineRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    /**
     * 根据设备ID获取所有货道
     */
    public List<Slot> findByMachineId(Long machineId) {
        return slotRepository.findByMachineId(machineId);
    }
    
    /**
     * 根据ID查询货道
     */
    public Optional<Slot> findById(Long id) {
        return slotRepository.findById(id);
    }
    
    /**
     * 绑定商品到货道
     */
    @Transactional
    public Slot bindProduct(Long slotId, Long productId) {
        Slot slot = slotRepository.findById(slotId)
            .orElseThrow(() -> new RuntimeException("货道不存在"));
        
        if (productId == null) {
            slot.setProduct(null);
            return slotRepository.save(slot);
        }
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("商品不存在"));
        
        slot.setProduct(product);
        return slotRepository.save(slot);
    }
    
    /**
     * 批量绑定商品到多个货道
     */
    @Transactional
    public void batchBindProduct(Long machineId, List<Integer> slotNumbers, Long productId) {
        VendingMachine machine = machineRepository.findById(machineId)
            .orElseThrow(() -> new RuntimeException("设备不存在"));
        
        Product product = null;
        if (productId != null) {
            product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        }
        
        for (Integer slotNumber : slotNumbers) {
            Optional<Slot> slotOpt = slotRepository.findByMachineIdAndSlotNumber(machineId, slotNumber);
            if (slotOpt.isPresent()) {
                Slot slot = slotOpt.get();
                slot.setProduct(product);
                slotRepository.save(slot);
            }
        }
    }
    
    /**
     * 更新货道库存
     */
    @Transactional
    public Slot updateStock(Long slotId, Integer stock) {
        Slot slot = slotRepository.findById(slotId)
            .orElseThrow(() -> new RuntimeException("货道不存在"));
        
        if (stock < 0) {
            throw new RuntimeException("库存不能为负数");
        }
        
        if (stock > slot.getMaxCapacity()) {
            throw new RuntimeException("库存不能超过最大容量");
        }
        
        slot.setCurrentStock(stock);
        return slotRepository.save(slot);
    }
    
    /**
     * 扣减库存（销售或传感器触发）
     */
    @Transactional
    public Slot deductStock(Long slotId, Integer quantity) {
        Slot slot = slotRepository.findById(slotId)
            .orElseThrow(() -> new RuntimeException("货道不存在"));
        
        if (slot.getCurrentStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        
        slot.setCurrentStock(slot.getCurrentStock() - quantity);
        return slotRepository.save(slot);
    }
    
    /**
     * 补货
     */
    @Transactional
    public Slot refillStock(Long slotId, Integer quantity) {
        Slot slot = slotRepository.findById(slotId)
            .orElseThrow(() -> new RuntimeException("货道不存在"));
        
        int newStock = slot.getCurrentStock() + quantity;
        if (newStock > slot.getMaxCapacity()) {
            throw new RuntimeException("补货后库存不能超过最大容量");
        }
        
        slot.setCurrentStock(newStock);
        slot.setLastRefillTime(LocalDateTime.now());
        return slotRepository.save(slot);
    }
    
    /**
     * 获取低库存货道
     */
    public List<Slot> findLowStockSlots() {
        return slotRepository.findAllLowStockSlots();
    }
    
    /**
     * 获取指定设备的低库存货道
     */
    public List<Slot> findLowStockSlotsByMachine(Long machineId) {
        return slotRepository.findLowStockSlotsByMachineId(machineId);
    }
    
    /**
     * 获取商品总库存
     */
    public Integer getTotalStockByProduct(Long productId) {
        Integer total = slotRepository.getTotalStockByProductId(productId);
        return total != null ? total : 0;
    }
}
