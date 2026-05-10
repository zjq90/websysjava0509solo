package com.seedtrace.service;

import com.seedtrace.dto.SeedBatchRequest;
import com.seedtrace.entity.SeedBatch;
import com.seedtrace.repository.SeedBatchRepository;
import com.seedtrace.security.AesEncryptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 种子批次管理服务类
 * 
 * <p>提供种子批次的CRUD操作，包括数据验证。</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Service
public class SeedBatchService {

    private static final Logger logger = LoggerFactory.getLogger(SeedBatchService.class);

    @Autowired
    private SeedBatchRepository seedBatchRepository;

    @Autowired
    private AesEncryptionService aesEncryptionService;

    /**
     * 批次编号格式校验正则：8位数字+字母组合
     */
    private static final Pattern BATCH_CODE_PATTERN = Pattern.compile("^[A-Za-z0-9]{8}$");

    /**
     * 手机号格式校验正则：1开头的11位数字
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 查询所有批次
     */
    public List<SeedBatch> getAllBatches() {
        return seedBatchRepository.findAll();
    }

    /**
     * 根据ID查询批次
     */
    public Optional<SeedBatch> getBatchById(Long id) {
        return seedBatchRepository.findById(id);
    }

    /**
     * 根据批次编号查询批次
     */
    public Optional<SeedBatch> getBatchByCode(String batchCode) {
        return seedBatchRepository.findByBatchCode(batchCode);
    }

    /**
     * 创建新批次
     */
    @Transactional
    public SeedBatch createBatch(SeedBatchRequest request) {
        // 1. 验证数据
        validateSeedBatchRequest(request);

        // 2. 检查批次号唯一性
        if (seedBatchRepository.existsByBatchCode(request.getBatchCode())) {
            throw new IllegalArgumentException("批次编号已存在：" + request.getBatchCode());
        }

        // 3. 构建实体
        SeedBatch batch = SeedBatch.builder()
                .batchCode(request.getBatchCode())
                .seedName(request.getSeedName())
                .seedVariety(request.getSeedVariety())
                .germinationRate(request.getGerminationRate())
                .purity(request.getPurity())
                .moistureContent(request.getMoistureContent())
                .productionDate(request.getProductionDate())
                .shelfLife(request.getShelfLife())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .createdBy(request.getCreatedBy())
                .build();

        // 4. 保存
        SeedBatch saved = seedBatchRepository.save(batch);
        logger.info("创建批次成功，批次号：{}", saved.getBatchCode());
        return saved;
    }

    /**
     * 更新批次
     */
    @Transactional
    public SeedBatch updateBatch(Long id, SeedBatchRequest request) {
        SeedBatch existing = seedBatchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("批次不存在，ID：" + id));

        // 如果修改了批次号，需要检查唯一性
        if (!existing.getBatchCode().equals(request.getBatchCode())) {
            if (seedBatchRepository.existsByBatchCode(request.getBatchCode())) {
                throw new IllegalArgumentException("批次编号已存在：" + request.getBatchCode());
            }
        }

        // 验证数据
        validateSeedBatchRequest(request);

        // 更新字段
        existing.setBatchCode(request.getBatchCode());
        existing.setSeedName(request.getSeedName());
        existing.setSeedVariety(request.getSeedVariety());
        existing.setGerminationRate(request.getGerminationRate());
        existing.setPurity(request.getPurity());
        existing.setMoistureContent(request.getMoistureContent());
        existing.setProductionDate(request.getProductionDate());
        existing.setShelfLife(request.getShelfLife());
        existing.setQuantity(request.getQuantity());
        existing.setUnitPrice(request.getUnitPrice());
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }

        SeedBatch updated = seedBatchRepository.save(existing);
        logger.info("更新批次成功，批次号：{}", updated.getBatchCode());
        return updated;
    }

    /**
     * 删除批次
     */
    @Transactional
    public void deleteBatch(Long id) {
        if (!seedBatchRepository.existsById(id)) {
            throw new IllegalArgumentException("批次不存在，ID：" + id);
        }
        seedBatchRepository.deleteById(id);
        logger.info("删除批次成功，ID：{}", id);
    }

    /**
     * 验证批次数据
     * 
     * <p>验证规则：
     * <ul>
     *   <li>批次编号：8位数字+字母组合</li>
     *   <li>保质期：不得早于当前日期+6个月</li>
     *   <li>发芽率：0-100%，精度保留1位小数</li>
     * </ul>
     * </p>
     */
    private void validateSeedBatchRequest(SeedBatchRequest request) {
        // 1. 验证批次编号格式
        if (request.getBatchCode() == null || !BATCH_CODE_PATTERN.matcher(request.getBatchCode()).matches()) {
            throw new IllegalArgumentException("批次编号格式不正确，必须为8位数字+字母组合");
        }

        // 2. 验证保质期：不得早于当前日期+6个月
        LocalDate minShelfLife = LocalDate.now().plusMonths(6);
        if (request.getShelfLife() != null && request.getShelfLife().isBefore(minShelfLife)) {
            throw new IllegalArgumentException("保质期不得早于当前日期+6个月，最早为：" + minShelfLife);
        }

        // 3. 验证发芽率范围：0-100%
        if (request.getGerminationRate() != null) {
            double rate = request.getGerminationRate().doubleValue();
            if (rate < 0 || rate > 100) {
                throw new IllegalArgumentException("发芽率必须在0-100%之间");
            }
            // 验证精度：只能保留1位小数
            String rateStr = request.getGerminationRate().toPlainString();
            if (rateStr.contains(".")) {
                String decimalPart = rateStr.split("\\.")[1];
                if (decimalPart.length() > 1) {
                    throw new IllegalArgumentException("发芽率精度只能保留1位小数");
                }
            }
        }

        // 4. 验证生产日期
        if (request.getProductionDate() == null) {
            throw new IllegalArgumentException("生产日期不能为空");
        }

        // 5. 验证保质期
        if (request.getShelfLife() == null) {
            throw new IllegalArgumentException("保质期不能为空");
        }

        // 6. 保质期必须晚于生产日期
        if (request.getShelfLife().isBefore(request.getProductionDate())) {
            throw new IllegalArgumentException("保质期必须晚于生产日期");
        }
    }

    /**
     * 验证手机号格式
     */
    public boolean validatePhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
}
