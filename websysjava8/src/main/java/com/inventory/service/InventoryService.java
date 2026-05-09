package com.inventory.service;

import com.inventory.entity.Inventory;
import com.inventory.entity.SeedBatch;
import com.inventory.entity.Store;
import com.inventory.entity.Warehouse;
import com.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 库存服务层
 * 功能：处理库存的增删改查业务逻辑，支持先进先出、多仓库管理
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

    public List<Inventory> findByWarehouseId(Long warehouseId) {
        return inventoryRepository.findByWarehouseId(warehouseId);
    }

    public List<Inventory> findByStoreId(Long storeId) {
        return inventoryRepository.findByStoreId(storeId);
    }

    public List<Inventory> findByBatchId(Long batchId) {
        return inventoryRepository.findBySeedBatchId(batchId);
    }

    public List<Inventory> findExpiringInventory(int days) {
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = today.plusDays(days);
        return inventoryRepository.findExpiringInventory(today, expiryDate);
    }

    public Optional<Inventory> findByWarehouseAndBatch(Long warehouseId, Long batchId) {
        return inventoryRepository.findByWarehouseAndBatch(warehouseId, batchId);
    }

    public Optional<Inventory> findByStoreAndBatch(Long storeId, Long batchId) {
        return inventoryRepository.findByStoreAndBatch(storeId, batchId);
    }

    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public void deleteById(Long id) {
        inventoryRepository.deleteById(id);
    }

    public Inventory inbound(SeedBatch batch, Warehouse warehouse, Store store, int quantity) {
        Inventory inventory;
        if (warehouse != null) {
            inventory = findByWarehouseAndBatch(warehouse.getId(), batch.getId()).orElse(new Inventory());
            inventory.setWarehouse(warehouse);
        } else {
            inventory = findByStoreAndBatch(store.getId(), batch.getId()).orElse(new Inventory());
            inventory.setStore(store);
        }
        inventory.setSeedBatch(batch);
        if (inventory.getQuantity() == null) {
            inventory.setQuantity(0);
        }
        inventory.setQuantity(inventory.getQuantity() + quantity);
        return save(inventory);
    }

    public boolean outbound(SeedBatch batch, Warehouse warehouse, Store store, int quantity) {
        List<Inventory> inventoryList;
        if (warehouse != null) {
            inventoryList = findByWarehouseId(warehouse.getId());
        } else {
            inventoryList = findByStoreId(store.getId());
        }
        inventoryList = inventoryList.stream()
                .filter(inv -> inv.getSeedBatch().getId().equals(batch.getId()))
                .sorted(Comparator.comparing(inv -> inv.getSeedBatch().getProductionDate()))
                .collect(java.util.stream.Collectors.toList());

        int remaining = quantity;
        for (Inventory inv : inventoryList) {
            if (remaining <= 0) break;
            if (inv.getQuantity() >= remaining) {
                inv.setQuantity(inv.getQuantity() - remaining);
                if (inv.getQuantity() == 0) {
                    deleteById(inv.getId());
                } else {
                    save(inv);
                }
                remaining = 0;
            } else {
                remaining -= inv.getQuantity();
                deleteById(inv.getId());
            }
        }
        return remaining == 0;
    }
}
