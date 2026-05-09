package com.inventory.service;

import com.inventory.entity.SeedBatch;
import com.inventory.repository.SeedBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 种子批次Service类
 * 管理批次的生命周期，包括近效期和过期状态更新
 */
@Service
public class SeedBatchService {

    @Autowired
    private SeedBatchRepository seedBatchRepository;

    /**
     * 查询所有批次
     */
    public List<SeedBatch> findAll() {
        return seedBatchRepository.findAll();
    }

    /**
     * 根据ID查询
     */
    public SeedBatch findById(Long id) {
        Optional<SeedBatch> optional = seedBatchRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 根据批次号查询
     */
    public SeedBatch findByBatchNo(String batchNo) {
        return seedBatchRepository.findByBatchNo(batchNo);
    }

    /**
     * 保存批次
     */
    @Transactional(rollbackFor = Exception.class)
    public SeedBatch save(SeedBatch seedBatch) {
        if (seedBatch.getAvailableQuantity() == null) {
            seedBatch.setAvailableQuantity(seedBatch.getTotalQuantity());
        }
        return seedBatchRepository.save(seedBatch);
    }

    /**
     * 删除批次
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        seedBatchRepository.deleteById(id);
    }

    /**
     * 根据品种ID查询批次
     */
    public List<SeedBatch> findByVarietyId(Long varietyId) {
        return seedBatchRepository.findByVarietyId(varietyId);
    }

    /**
     * 查询近效期批次
     * @param warningDays 警告天数
     */
    public List<SeedBatch> findNearExpiryBatches(int warningDays) {
        LocalDate currentDate = LocalDate.now();
        LocalDate warningDate = currentDate.plusDays(warningDays);
        return seedBatchRepository.findNearExpiryBatches(warningDate, currentDate);
    }

    /**
     * 查询已过期批次
     */
    public List<SeedBatch> findExpiredBatches() {
        return seedBatchRepository.findExpiredBatches(LocalDate.now());
    }

    /**
     * 查询某个品种的可用批次（先进先出排序）
     */
    public List<SeedBatch> findAvailableBatchesFIFO(Long varietyId) {
        return seedBatchRepository.findAvailableBatchesFIFO(varietyId);
    }

    /**
     * 检查批次号是否存在
     */
    public boolean existsByBatchNo(String batchNo) {
        return seedBatchRepository.existsByBatchNo(batchNo);
    }

    /**
     * 更新批次状态（检查近效期和过期）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBatchStatus(int warningDays) {
        LocalDate currentDate = LocalDate.now();
        LocalDate warningDate = currentDate.plusDays(warningDays);
        
        // 更新已过期批次状态
        List<SeedBatch> expiredBatches = seedBatchRepository.findExpiredBatches(currentDate);
        for (SeedBatch batch : expiredBatches) {
            if (batch.getStatus() != 2) {
                batch.setStatus(2);
                seedBatchRepository.save(batch);
            }
        }
        
        // 更新近效期批次状态（排除已过期的）
        List<SeedBatch> nearExpiryBatches = seedBatchRepository.findNearExpiryBatches(warningDate, currentDate);
        for (SeedBatch batch : nearExpiryBatches) {
            if (batch.getStatus() == 0) {
                batch.setStatus(1);
                seedBatchRepository.save(batch);
            }
        }
    }
}
