package com.appsys.inventory.service;

import com.appsys.common.exception.BusinessException;
import com.appsys.common.result.PageResult;
import com.appsys.inventory.dto.InventoryDTO;
import com.appsys.inventory.entity.Inventory;
import com.appsys.inventory.entity.Seed;
import com.appsys.inventory.repository.InventoryRepository;
import com.appsys.inventory.repository.SeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 库存管理服务类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private SeedRepository seedRepository;

    /**
     * 分页查询库存列表
     */
    public PageResult<Inventory> list(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Inventory> inventoryPage;
        
        if (StringUtils.hasText(keyword)) {
            inventoryPage = inventoryRepository.searchByKeyword(keyword, pageable);
        } else {
            inventoryPage = inventoryRepository.findByDeletedFalseOrderByCreatedTimeDesc(pageable);
        }
        
        return PageResult.of(inventoryPage);
    }

    /**
     * 根据ID查询库存详情
     */
    public Inventory getById(Long id) {
        return inventoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("库存记录不存在"));
    }

    /**
     * 根据批次号查询库存
     */
    public Inventory getByBatchNo(String batchNo) {
        return inventoryRepository.findByBatchNo(batchNo)
                .orElseThrow(() -> new BusinessException("批次号不存在"));
    }

    /**
     * 新增库存入库
     */
    @Transactional
    public Inventory create(InventoryDTO dto) {
        // 检查批次号是否已存在
        if (inventoryRepository.existsByBatchNo(dto.getBatchNo())) {
            throw new BusinessException("批次号已存在，必须全局唯一");
        }

        // 检查种子是否存在
        Seed seed = seedRepository.findByIdAndDeletedFalse(dto.getSeedId())
                .orElseThrow(() -> new BusinessException("种子不存在"));

        Inventory inventory = new Inventory();
        inventory.setBatchNo(dto.getBatchNo());
        inventory.setSeedId(dto.getSeedId());
        inventory.setSeedName(seed.getSeedName());
        inventory.setQuantity(dto.getQuantity());
        inventory.setRemainingQuantity(dto.getQuantity());
        inventory.setInDate(dto.getInDate() != null ? dto.getInDate() : LocalDate.now());
        inventory.setExpiryDate(dto.getExpiryDate() != null ? dto.getExpiryDate() : seed.getExpiryDate());
        inventory.setGerminationRate(dto.getGerminationRate() != null ? dto.getGerminationRate() : seed.getGerminationRate());
        inventory.setUnitPrice(dto.getUnitPrice() != null ? dto.getUnitPrice() : seed.getPurchasePrice());
        
        // 计算总价
        if (inventory.getUnitPrice() != null && inventory.getQuantity() != null) {
            inventory.setTotalPrice(inventory.getUnitPrice().multiply(inventory.getQuantity()));
        }
        
        inventory.setWarehouseLocation(dto.getWarehouseLocation());
        inventory.setRemark(dto.getRemark());
        inventory.setStatus(1);

        return inventoryRepository.save(inventory);
    }

    /**
     * 更新库存信息
     */
    @Transactional
    public Inventory update(Long id, InventoryDTO dto) {
        Inventory inventory = getById(id);

        // 如果批次号改变，检查是否已存在
        if (!inventory.getBatchNo().equals(dto.getBatchNo()) 
                && inventoryRepository.existsByBatchNo(dto.getBatchNo())) {
            throw new BusinessException("批次号已存在，必须全局唯一");
        }

        // 检查种子是否存在
        Seed seed = seedRepository.findByIdAndDeletedFalse(dto.getSeedId())
                .orElseThrow(() -> new BusinessException("种子不存在"));

        inventory.setBatchNo(dto.getBatchNo());
        inventory.setSeedId(dto.getSeedId());
        inventory.setSeedName(seed.getSeedName());
        
        // 更新入库数量时，重新计算剩余数量
        BigDecimal originalQuantity = inventory.getQuantity();
        BigDecimal usedQuantity = originalQuantity.subtract(inventory.getRemainingQuantity());
        inventory.setQuantity(dto.getQuantity());
        inventory.setRemainingQuantity(dto.getQuantity().subtract(usedQuantity));
        
        if (inventory.getRemainingQuantity().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("剩余数量不能为负数");
        }
        
        if (dto.getInDate() != null) {
            inventory.setInDate(dto.getInDate());
        }
        if (dto.getExpiryDate() != null) {
            inventory.setExpiryDate(dto.getExpiryDate());
        }
        if (dto.getGerminationRate() != null) {
            inventory.setGerminationRate(dto.getGerminationRate());
        }
        if (dto.getUnitPrice() != null) {
            inventory.setUnitPrice(dto.getUnitPrice());
            inventory.setTotalPrice(dto.getUnitPrice().multiply(inventory.getQuantity()));
        }
        inventory.setWarehouseLocation(dto.getWarehouseLocation());
        inventory.setRemark(dto.getRemark());

        return inventoryRepository.save(inventory);
    }

    /**
     * 删除库存记录（逻辑删除）
     */
    @Transactional
    public void delete(Long id) {
        Inventory inventory = getById(id);
        inventory.setDeleted(true);
        inventoryRepository.save(inventory);
    }

    /**
     * 扣减库存数量
     */
    @Transactional
    public void deductQuantity(Long id, BigDecimal quantity) {
        Inventory inventory = getById(id);
        if (inventory.getRemainingQuantity().compareTo(quantity) < 0) {
            throw new BusinessException("库存数量不足");
        }
        inventory.setRemainingQuantity(inventory.getRemainingQuantity().subtract(quantity));
        inventoryRepository.save(inventory);
    }
}
