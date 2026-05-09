package com.traceability.service;

import com.traceability.entity.SeedBatch;
import com.traceability.repository.SeedBatchRepository;
import com.traceability.util.QrCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 种子批次业务逻辑服务类
 */
@Service
@Transactional
public class SeedBatchService {

    @Autowired
    private SeedBatchRepository seedBatchRepository;

    /**
     * 获取所有种子批次
     */
    public List<SeedBatch> findAll() {
        return seedBatchRepository.findAll();
    }

    /**
     * 分页获取种子批次
     */
    public Page<SeedBatch> findAll(Pageable pageable) {
        return seedBatchRepository.findAll(pageable);
    }

    /**
     * 根据ID查询种子批次
     */
    public Optional<SeedBatch> findById(Long id) {
        return seedBatchRepository.findById(id);
    }

    /**
     * 根据批次号查询种子批次
     */
    public Optional<SeedBatch> findByBatchNo(String batchNo) {
        return seedBatchRepository.findByBatchNo(batchNo);
    }

    /**
     * 保存种子批次
     */
    public SeedBatch save(SeedBatch seedBatch) {
        if (seedBatch.getBatchNo() == null || seedBatch.getBatchNo().isEmpty()) {
            seedBatch.setBatchNo(QrCodeUtil.generateBatchNo());
        }
        return seedBatchRepository.save(seedBatch);
    }

    /**
     * 更新种子批次
     */
    public SeedBatch update(SeedBatch seedBatch) {
        return seedBatchRepository.save(seedBatch);
    }

    /**
     * 根据ID删除种子批次
     */
    public void deleteById(Long id) {
        seedBatchRepository.deleteById(id);
    }

    /**
     * 检查批次号是否存在
     */
    public boolean existsByBatchNo(String batchNo) {
        return seedBatchRepository.existsByBatchNo(batchNo);
    }
}
