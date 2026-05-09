package com.agriculture.service;

import com.agriculture.entity.Crop;
import com.agriculture.repository.CropRepository;
import com.agriculture.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 作物品种服务类
 * 提供作物品种管理的业务逻辑，包含数据验证
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Service
@Transactional
public class CropService {

    @Autowired
    private CropRepository cropRepository;

    /**
     * 创建作物品种
     * 
     * @param crop 作物信息
     * @return 创建后的作物
     */
    public Crop create(Crop crop) {
        ValidationUtil.ValidationResult batchCodeResult = ValidationUtil.validateBatchCode(crop.getBatchCode());
        if (!batchCodeResult.isValid()) {
            throw new RuntimeException(batchCodeResult.getMessage());
        }

        if (cropRepository.existsByBatchCode(crop.getBatchCode())) {
            throw new RuntimeException("批次编号已存在，必须全局唯一");
        }

        ValidationUtil.ValidationResult shelfLifeResult = ValidationUtil.validateShelfLife(crop.getShelfLife());
        if (!shelfLifeResult.isValid()) {
            throw new RuntimeException(shelfLifeResult.getMessage());
        }

        ValidationUtil.ValidationResult germinationResult = ValidationUtil.validateGerminationRate(crop.getGerminationRate());
        if (!germinationResult.isValid()) {
            throw new RuntimeException(germinationResult.getMessage());
        }

        return cropRepository.save(crop);
    }

    /**
     * 更新作物信息
     * 
     * @param crop 作物信息
     * @return 更新后的作物
     */
    public Crop update(Crop crop) {
        Optional<Crop> existingOpt = cropRepository.findById(crop.getId());
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("作物品种不存在");
        }

        if (crop.getBatchCode() != null && !crop.getBatchCode().equals(existingOpt.get().getBatchCode())) {
            ValidationUtil.ValidationResult batchCodeResult = ValidationUtil.validateBatchCode(crop.getBatchCode());
            if (!batchCodeResult.isValid()) {
                throw new RuntimeException(batchCodeResult.getMessage());
            }
            if (cropRepository.existsByBatchCode(crop.getBatchCode())) {
                throw new RuntimeException("批次编号已存在，必须全局唯一");
            }
        }

        if (crop.getShelfLife() != null) {
            ValidationUtil.ValidationResult shelfLifeResult = ValidationUtil.validateShelfLife(crop.getShelfLife());
            if (!shelfLifeResult.isValid()) {
                throw new RuntimeException(shelfLifeResult.getMessage());
            }
        }

        if (crop.getGerminationRate() != null) {
            ValidationUtil.ValidationResult germinationResult = ValidationUtil.validateGerminationRate(crop.getGerminationRate());
            if (!germinationResult.isValid()) {
                throw new RuntimeException(germinationResult.getMessage());
            }
        }

        return cropRepository.save(crop);
    }

    /**
     * 根据ID删除作物
     * 
     * @param id 作物ID
     */
    public void deleteById(Long id) {
        cropRepository.deleteById(id);
    }

    /**
     * 根据ID查找作物
     * 
     * @param id 作物ID
     * @return 作物对象
     */
    public Optional<Crop> findById(Long id) {
        return cropRepository.findById(id);
    }

    /**
     * 根据批次编号查找
     * 
     * @param batchCode 批次编号
     * @return 作物对象
     */
    public Optional<Crop> findByBatchCode(String batchCode) {
        return cropRepository.findByBatchCode(batchCode);
    }

    /**
     * 查询所有作物
     * 
     * @return 作物列表
     */
    public List<Crop> findAll() {
        return cropRepository.findAll();
    }

    /**
     * 根据作物类型查询
     * 
     * @param cropType 作物类型
     * @return 作物列表
     */
    public List<Crop> findByCropType(String cropType) {
        return cropRepository.findByCropTypeOrderByCreatedAtDesc(cropType);
    }

    /**
     * 查询启用状态的作物
     * 
     * @return 作物列表
     */
    public List<Crop> findActiveCrops() {
        return cropRepository.findByStatusOrderByCreatedAtDesc("ACTIVE");
    }

    /**
     * 检查批次编号是否存在
     * 
     * @param batchCode 批次编号
     * @return 是否存在
     */
    public boolean existsByBatchCode(String batchCode) {
        return cropRepository.existsByBatchCode(batchCode);
    }
}
