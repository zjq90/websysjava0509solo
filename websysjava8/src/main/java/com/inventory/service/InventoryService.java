package com.inventory.service;

import com.inventory.entity.Inventory;
import com.inventory.entity.SeedBatch;
import com.inventory.repository.InventoryRepository;
import com.inventory.repository.SeedBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 库存Service类
 * 核心库存管理逻辑，包括：
 * - 入库管理
 * - 出库管理（先进先出）
 * - 库存预警检查
 * - 近效期和过期检查
 */
@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private SeedBatchRepository seedBatchRepository;

    @Value("${inventory.expiry.warning.days:30}")
    private int expiryWarningDays;

    /**
     * 查询所有库存
     */
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    /**
     * 根据ID查询
     */
    public Inventory findById(Long id) {
        Optional<Inventory> optional = inventoryRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 保存库存
     */
    @Transactional(rollbackFor = Exception.class)
    public Inventory save(Inventory inventory) {
        if (inventory.getAvailableQuantity() == null) {
            inventory.setAvailableQuantity(inventory.getQuantity() - (inventory.getLockedQuantity() == null ? 0 : inventory.getLockedQuantity()));
        }
        return inventoryRepository.save(inventory);
    }

    /**
     * 删除库存
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        inventoryRepository.deleteById(id);
    }

    /**
     * 根据位置查询所有库存
     * @param inventoryType 1-仓库，2-门店
     * @param locationId 位置ID
     */
    public List<Inventory> findByLocation(Integer inventoryType, Long locationId) {
        return inventoryRepository.findByInventoryTypeAndLocationId(inventoryType, locationId);
    }

    /**
     * 根据位置和批次查询唯一库存
     */
    public Inventory findByLocationAndBatch(Integer inventoryType, Long locationId, Long batchId) {
        return inventoryRepository.findByInventoryTypeAndLocationIdAndBatchId(inventoryType, locationId, batchId);
    }

    /**
     * 入库操作
     * @param inventoryType 库存类型
     * @param locationId 位置ID
     * @param batchId 批次ID
     * @param quantity 入库数量
     * @param inboundNo 入库单号
     */
    @Transactional(rollbackFor = Exception.class)
    public Inventory inbound(Integer inventoryType, Long locationId, Long batchId, Integer quantity, String inboundNo) {
        // 检查批次是否存在
        SeedBatch batch = seedBatchRepository.findById(batchId).orElse(null);
        if (batch == null) {
            throw new RuntimeException("批次不存在");
        }

        // 查询是否已有该批次的库存记录
        Inventory existing = inventoryRepository.findByInventoryTypeAndLocationIdAndBatchId(inventoryType, locationId, batchId);
        
        if (existing != null) {
            // 更新现有库存
            existing.setQuantity(existing.getQuantity() + quantity);
            existing.setAvailableQuantity(existing.getAvailableQuantity() + quantity);
            if (inboundNo != null) {
                existing.setInboundNo(inboundNo);
            }
            return inventoryRepository.save(existing);
        } else {
            // 创建新库存记录
            Inventory inventory = new Inventory();
            inventory.setInventoryType(inventoryType);
            inventory.setLocationId(locationId);
            inventory.setBatchId(batchId);
            inventory.setQuantity(quantity);
            inventory.setLockedQuantity(0);
            inventory.setAvailableQuantity(quantity);
            inventory.setInboundDate(LocalDate.now());
            inventory.setInboundNo(inboundNo);
            inventory.setStatus(0);
            return inventoryRepository.save(inventory);
        }
    }

    /**
     * 出库操作（先进先出）
     * @param inventoryType 库存类型
     * @param locationId 位置ID
     * @param varietyId 品种ID
     * @param quantity 出库数量
     * @return 实际出库数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int outboundFIFO(Integer inventoryType, Long locationId, Long varietyId, Integer quantity) {
        int remaining = quantity;
        
        // 按先进先出顺序获取可用库存（按入库日期排序）
        List<Inventory> inventoryList = inventoryRepository.findInventoryFIFO(inventoryType, locationId, varietyId);
        
        if (inventoryList == null || inventoryList.isEmpty()) {
            throw new RuntimeException("该品种没有可用库存");
        }

        int totalOutbound = 0;
        
        for (Inventory inventory : inventoryList) {
            if (remaining <= 0) break;
            
            int available = inventory.getAvailableQuantity();
            if (available <= 0) continue;
            
            int toOutbound = Math.min(available, remaining);
            
            // 更新库存
            inventory.setQuantity(inventory.getQuantity() - toOutbound);
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() - toOutbound);
            
            // 如果库存为0，删除记录或标记状态
            if (inventory.getQuantity() <= 0) {
                inventoryRepository.delete(inventory);
            } else {
                inventoryRepository.save(inventory);
            }
            
            // 更新批次可用数量
            SeedBatch batch = seedBatchRepository.findById(inventory.getBatchId()).orElse(null);
            if (batch != null) {
                batch.setAvailableQuantity(batch.getAvailableQuantity() - toOutbound);
                seedBatchRepository.save(batch);
            }
            
            remaining -= toOutbound;
            totalOutbound += toOutbound;
        }
        
        if (remaining > 0) {
            throw new RuntimeException("库存不足，还需 " + remaining + " 单位");
        }
        
        return totalOutbound;
    }

    /**
     * 出库操作（先进先出）- 返回出库详情列表
     * @param inventoryType 库存类型
     * @param locationId 位置ID
     * @param varietyId 品种ID
     * @param quantity 出库数量
     * @return 出库详情列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<Inventory> outboundFIFODetail(Integer inventoryType, Long locationId, Long varietyId, Integer quantity) {
        int remaining = quantity;
        List<Inventory> result = new ArrayList<>();
        
        // 按先进先出顺序获取可用库存（按入库日期排序）
        List<Inventory> inventoryList = inventoryRepository.findInventoryFIFO(inventoryType, locationId, varietyId);
        
        if (inventoryList == null || inventoryList.isEmpty()) {
            throw new RuntimeException("该品种没有可用库存");
        }
        
        for (Inventory inventory : inventoryList) {
            if (remaining <= 0) break;
            
            int available = inventory.getAvailableQuantity();
            if (available <= 0) continue;
            
            int toOutbound = Math.min(available, remaining);
            
            // 创建出库详情副本
            Inventory detail = new Inventory();
            detail.setId(inventory.getId());
            detail.setBatchId(inventory.getBatchId());
            detail.setQuantity(toOutbound);
            detail.setInboundDate(inventory.getInboundDate());
            result.add(detail);
            
            // 更新库存
            inventory.setQuantity(inventory.getQuantity() - toOutbound);
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() - toOutbound);
            
            // 如果库存为0，删除记录或标记状态
            if (inventory.getQuantity() <= 0) {
                inventoryRepository.delete(inventory);
            } else {
                inventoryRepository.save(inventory);
            }
            
            // 更新批次可用数量
            SeedBatch batch = seedBatchRepository.findById(inventory.getBatchId()).orElse(null);
            if (batch != null) {
                batch.setAvailableQuantity(batch.getAvailableQuantity() - toOutbound);
                seedBatchRepository.save(batch);
            }
            
            remaining -= toOutbound;
        }
        
        if (remaining > 0) {
            throw new RuntimeException("库存不足，还需 " + remaining + " 单位");
        }
        
        return result;
    }

    /**
     * 查询近效期库存（所有位置）
     */
    public List<Inventory> findNearExpiryInventory() {
        LocalDate currentDate = LocalDate.now();
        LocalDate warningDate = currentDate.plusDays(expiryWarningDays);
        return inventoryRepository.findAllNearExpiryInventory(warningDate, currentDate);
    }

    /**
     * 查询近效期库存（按位置）
     */
    public List<Inventory> findNearExpiryInventory(Integer inventoryType, Long locationId) {
        LocalDate currentDate = LocalDate.now();
        LocalDate warningDate = currentDate.plusDays(expiryWarningDays);
        return inventoryRepository.findNearExpiryInventory(inventoryType, locationId, warningDate, currentDate);
    }

    /**
     * 查询已过期库存
     */
    public List<Inventory> findExpiredInventory() {
        return inventoryRepository.findExpiredInventory(LocalDate.now());
    }

    /**
     * 查询低库存预警
     */
    public List<Inventory> findLowStockInventory() {
        return inventoryRepository.findLowStockInventory();
    }

    /**
     * 查询高库存预警
     */
    public List<Inventory> findHighStockInventory() {
        return inventoryRepository.findHighStockInventory();
    }

    /**
     * 统计位置总库存
     */
    public Integer sumQuantityByLocation(Integer inventoryType, Long locationId) {
        return inventoryRepository.sumQuantityByLocation(inventoryType, locationId);
    }

    /**
     * 统计位置可用库存
     */
    public Integer sumAvailableQuantityByLocation(Integer inventoryType, Long locationId) {
        return inventoryRepository.sumAvailableQuantityByLocation(inventoryType, locationId);
    }

    /**
     * 按先进先出获取某个品种的可用库存列表
     */
    public List<Inventory> getFIFOInventory(Integer inventoryType, Long locationId, Long varietyId) {
        return inventoryRepository.findInventoryFIFO(inventoryType, locationId, varietyId);
    }
}
