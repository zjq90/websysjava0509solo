package com.management.service;

import com.management.entity.Inventory;
import com.management.entity.Product;
import com.management.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 库存服务类
 * 提供库存的增删改查功能
 */
@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> findById(Long id) {
        return inventoryRepository.findById(id);
    }

    public Optional<Inventory> findByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    public Inventory save(Inventory inventory) {
        if (inventory.getTotalValue() == null && inventory.getQuantity() != null && inventory.getAvgCostPrice() != null) {
            inventory.setTotalValue(inventory.getAvgCostPrice().multiply(BigDecimal.valueOf(inventory.getQuantity())));
        }
        return inventoryRepository.save(inventory);
    }

    public void deleteById(Long id) {
        inventoryRepository.deleteById(id);
    }

    public List<Inventory> findLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }

    public BigDecimal getTotalInventoryValue() {
        BigDecimal value = inventoryRepository.findTotalInventoryValue();
        return value != null ? value : BigDecimal.ZERO;
    }

    public void updateStockIn(Long productId, int quantity, BigDecimal unitPrice, Product product) {
        Optional<Inventory> inventoryOpt = findByProductId(productId);
        Inventory inventory;
        if (inventoryOpt.isPresent()) {
            inventory = inventoryOpt.get();
            int oldQuantity = inventory.getQuantity();
            BigDecimal oldValue = inventory.getTotalValue() != null ? inventory.getTotalValue() : BigDecimal.ZERO;
            
            int newQuantity = oldQuantity + quantity;
            BigDecimal newValue = oldValue.add(unitPrice.multiply(BigDecimal.valueOf(quantity)));
            
            inventory.setQuantity(newQuantity);
            inventory.setTotalValue(newValue);
            if (newQuantity > 0) {
                inventory.setAvgCostPrice(newValue.divide(BigDecimal.valueOf(newQuantity), 2, BigDecimal.ROUND_HALF_UP));
            }
            inventory.setLastInTime(LocalDateTime.now());
        } else {
            inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setQuantity(quantity);
            inventory.setAvgCostPrice(unitPrice);
            inventory.setTotalValue(unitPrice.multiply(BigDecimal.valueOf(quantity)));
            inventory.setLastInTime(LocalDateTime.now());
        }
        save(inventory);
    }

    public void updateStockOut(Long productId, int quantity) {
        Optional<Inventory> inventoryOpt = findByProductId(productId);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            int newQuantity = inventory.getQuantity() - quantity;
            if (newQuantity < 0) {
                throw new RuntimeException("库存不足");
            }
            
            inventory.setQuantity(newQuantity);
            if (inventory.getAvgCostPrice() != null) {
                inventory.setTotalValue(inventory.getAvgCostPrice().multiply(BigDecimal.valueOf(newQuantity)));
            }
            inventory.setLastOutTime(LocalDateTime.now());
            save(inventory);
        }
    }
}
