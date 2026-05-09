package com.inventory.service;

import com.inventory.entity.Inventory;
import com.inventory.entity.InventoryCheck;
import com.inventory.repository.InventoryCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 盘点服务层
 * 功能：处理库存盘点业务逻辑
 */
@Service
@Transactional
public class InventoryCheckService {

    @Autowired
    private InventoryCheckRepository inventoryCheckRepository;

    @Autowired
    private InventoryService inventoryService;

    public List<InventoryCheck> findAll() {
        return inventoryCheckRepository.findAll();
    }

    public Optional<InventoryCheck> findById(Long id) {
        return inventoryCheckRepository.findById(id);
    }

    public Optional<InventoryCheck> findByCode(String code) {
        return inventoryCheckRepository.findByCheckCode(code);
    }

    public List<InventoryCheck> findByInventoryId(Long inventoryId) {
        return inventoryCheckRepository.findByInventoryId(inventoryId);
    }

    public InventoryCheck createCheck(Long inventoryId, Integer actualQuantity, String remark) {
        Inventory inventory = inventoryService.findById(inventoryId).orElse(null);
        if (inventory == null) {
            return null;
        }

        InventoryCheck check = new InventoryCheck();
        check.setCheckCode("PD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        check.setInventory(inventory);
        check.setSystemQuantity(inventory.getQuantity());
        check.setActualQuantity(actualQuantity);
        int diff = actualQuantity - inventory.getQuantity();
        check.setDiffQuantity(Math.abs(diff));
        if (diff > 0) {
            check.setDiffType("OVER");
        } else if (diff < 0) {
            check.setDiffType("SHORT");
        } else {
            check.setDiffType("NORMAL");
        }
        check.setRemark(remark);
        check.setStatus("COMPLETED");

        return inventoryCheckRepository.save(check);
    }

    public void deleteById(Long id) {
        inventoryCheckRepository.deleteById(id);
    }
}
