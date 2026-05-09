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
 * 种子批次服务层
 * 功能：处理种子批次的增删改查业务逻辑，支持近效期查询
 */
@Service
@Transactional
public class SeedBatchService {

    @Autowired
    private SeedBatchRepository seedBatchRepository;

    public List<SeedBatch> findAll() {
        return seedBatchRepository.findAll();
    }

    public Optional<SeedBatch> findById(Long id) {
        return seedBatchRepository.findById(id);
    }

    public Optional<SeedBatch> findByCode(String code) {
        return seedBatchRepository.findByBatchCode(code);
    }

    public List<SeedBatch> findByVarietyId(Long varietyId) {
        return seedBatchRepository.findByVarietyId(varietyId);
    }

    public List<SeedBatch> findExpiringBatches(int days) {
        LocalDate today = LocalDate.now();
        LocalDate expiryDate = today.plusDays(days);
        return seedBatchRepository.findExpiringBatches(today, expiryDate);
    }

    public List<SeedBatch> findExpiredBatches() {
        return seedBatchRepository.findByExpiryDateLessThan(LocalDate.now());
    }

    public SeedBatch save(SeedBatch seedBatch) {
        return seedBatchRepository.save(seedBatch);
    }

    public void deleteById(Long id) {
        seedBatchRepository.deleteById(id);
    }
}
