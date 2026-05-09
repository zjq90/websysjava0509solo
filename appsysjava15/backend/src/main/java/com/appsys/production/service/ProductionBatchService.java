package com.appsys.production.service;

import com.appsys.production.config.EncryptionConfig;
import com.appsys.production.dto.ProductionBatchDTO;
import com.appsys.production.entity.BatchStage;
import com.appsys.production.entity.ProductionBatch;
import com.appsys.production.exception.BusinessException;
import com.appsys.production.repository.BatchStageRepository;
import com.appsys.production.repository.ProductionBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductionBatchService {

    @Autowired
    private ProductionBatchRepository batchRepository;

    @Autowired
    private BatchStageRepository stageRepository;

    @Autowired
    private EncryptionConfig encryptionConfig;

    private static final List<String> STAGES = Arrays.asList("CLEANING", "COATING", "PACKAGING", "INSPECTION");
    private static final List<String> STAGE_NAMES = Arrays.asList("清选", "包衣", "分装", "质检");

    public List<ProductionBatch> list() {
        List<ProductionBatch> batches = batchRepository.findAll();
        batches.forEach(this::decryptBatchPhone);
        return batches;
    }

    public ProductionBatch getById(Long id) {
        ProductionBatch batch = batchRepository.findById(id).orElseThrow(() -> new BusinessException("批次不存在"));
        decryptBatchPhone(batch);
        return batch;
    }

    public ProductionBatch getByBatchNo(String batchNo) {
        ProductionBatch batch = batchRepository.findByBatchNo(batchNo).orElseThrow(() -> new BusinessException("批次不存在"));
        decryptBatchPhone(batch);
        return batch;
    }

    @Transactional
    public ProductionBatch create(ProductionBatchDTO dto) {
        validateBatchNo(dto.getBatchNo());
        validateShelfLife(dto.getShelfLife());

        if (batchRepository.existsByBatchNo(dto.getBatchNo())) {
            throw new BusinessException("批次编号已存在，必须全局唯一");
        }

        ProductionBatch batch = new ProductionBatch();
        batch.setBatchNo(dto.getBatchNo());
        batch.setProductName(dto.getProductName());
        batch.setQuantity(dto.getQuantity());
        batch.setUnit(dto.getUnit() != null ? dto.getUnit() : "公斤");
        batch.setShelfLife(dto.getShelfLife());
        batch.setCustomerName(dto.getCustomerName());
        if (dto.getCustomerPhone() != null) {
            batch.setCustomerPhoneEncrypted(encryptionConfig.encrypt(dto.getCustomerPhone()));
        }
        batch.setRemark(dto.getRemark());
        batch.setOperatorId(dto.getOperatorId());
        batch.setStatus("PENDING");
        batch.setCurrentStage(STAGES.get(0));

        ProductionBatch savedBatch = batchRepository.save(batch);
        createBatchStages(savedBatch.getId());
        return savedBatch;
    }

    @Transactional
    public ProductionBatch update(Long id, ProductionBatchDTO dto) {
        ProductionBatch batch = batchRepository.findById(id).orElseThrow(() -> new BusinessException("批次不存在"));

        if (!batch.getBatchNo().equals(dto.getBatchNo())) {
            validateBatchNo(dto.getBatchNo());
            if (batchRepository.existsByBatchNo(dto.getBatchNo())) {
                throw new BusinessException("批次编号已存在，必须全局唯一");
            }
            batch.setBatchNo(dto.getBatchNo());
        }

        validateShelfLife(dto.getShelfLife());

        batch.setProductName(dto.getProductName());
        batch.setQuantity(dto.getQuantity());
        batch.setUnit(dto.getUnit() != null ? dto.getUnit() : "公斤");
        batch.setShelfLife(dto.getShelfLife());
        batch.setCustomerName(dto.getCustomerName());
        if (dto.getCustomerPhone() != null) {
            batch.setCustomerPhoneEncrypted(encryptionConfig.encrypt(dto.getCustomerPhone()));
        }
        batch.setRemark(dto.getRemark());
        batch.setOperatorId(dto.getOperatorId());

        return batchRepository.save(batch);
    }

    @Transactional
    public void delete(Long id) {
        ProductionBatch batch = batchRepository.findById(id).orElseThrow(() -> new BusinessException("批次不存在"));
        batchRepository.delete(batch);
    }

    private void createBatchStages(Long batchId) {
        for (int i = 0; i < STAGES.size(); i++) {
            BatchStage stage = new BatchStage();
            stage.setBatchId(batchId);
            stage.setStageCode(STAGES.get(i));
            stage.setStageName(STAGE_NAMES.get(i));
            stage.setSortOrder(i + 1);
            stage.setStatus("PENDING");
            stageRepository.save(stage);
        }
    }

    private void validateBatchNo(String batchNo) {
        if (batchNo == null || !batchNo.matches("^[A-Za-z0-9]{8}$")) {
            throw new BusinessException("批次编号必须为8位数字+字母组合");
        }
    }

    private void validateShelfLife(LocalDate shelfLife) {
        LocalDate minShelfLife = LocalDate.now().plusMonths(6);
        if (shelfLife == null || shelfLife.isBefore(minShelfLife)) {
            throw new BusinessException("保质期不得早于当前日期+6个月");
        }
    }

    private void decryptBatchPhone(ProductionBatch batch) {
        if (batch.getCustomerPhoneEncrypted() != null) {
            try {
                batch.setCustomerPhoneEncrypted(encryptionConfig.decrypt(batch.getCustomerPhoneEncrypted()));
            } catch (Exception e) {
            }
        }
    }
}
