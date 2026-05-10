package com.appsys.inventory.service;

import com.appsys.common.exception.BusinessException;
import com.appsys.inventory.dto.SeedDTO;
import com.appsys.inventory.entity.Seed;
import com.appsys.inventory.repository.SeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 种子管理服务类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Service
public class SeedService {

    @Autowired
    private SeedRepository seedRepository;

    /**
     * 查询所有种子列表
     */
    public List<Seed> list() {
        return seedRepository.findByDeletedFalseOrderByCreatedTimeDesc();
    }

    /**
     * 根据ID查询种子详情
     */
    public Seed getById(Long id) {
        return seedRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("种子不存在"));
    }

    /**
     * 新增种子
     */
    @Transactional
    public Seed create(SeedDTO dto) {
        Seed seed = new Seed();
        seed.setSeedName(dto.getSeedName());
        seed.setCategory(dto.getCategory());
        seed.setSpecification(dto.getSpecification());
        seed.setUnit(dto.getUnit());
        seed.setPurchasePrice(dto.getPurchasePrice());
        seed.setSalePrice(dto.getSalePrice());
        seed.setGerminationRate(dto.getGerminationRate());
        seed.setExpiryDate(dto.getExpiryDate());
        seed.setSupplier(dto.getSupplier());
        seed.setRemark(dto.getRemark());
        return seedRepository.save(seed);
    }

    /**
     * 更新种子信息
     */
    @Transactional
    public Seed update(Long id, SeedDTO dto) {
        Seed seed = getById(id);
        seed.setSeedName(dto.getSeedName());
        seed.setCategory(dto.getCategory());
        seed.setSpecification(dto.getSpecification());
        seed.setUnit(dto.getUnit());
        seed.setPurchasePrice(dto.getPurchasePrice());
        seed.setSalePrice(dto.getSalePrice());
        seed.setGerminationRate(dto.getGerminationRate());
        seed.setExpiryDate(dto.getExpiryDate());
        seed.setSupplier(dto.getSupplier());
        seed.setRemark(dto.getRemark());
        return seedRepository.save(seed);
    }

    /**
     * 删除种子（逻辑删除）
     */
    @Transactional
    public void delete(Long id) {
        Seed seed = getById(id);
        seed.setDeleted(true);
        seedRepository.save(seed);
    }
}
