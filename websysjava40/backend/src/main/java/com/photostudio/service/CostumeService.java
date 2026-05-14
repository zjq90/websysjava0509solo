package com.photostudio.service;

import com.photostudio.entity.Costume;
import com.photostudio.entity.Costume.CleaningStatus;
import com.photostudio.entity.Costume.CostumeType;
import com.photostudio.repository.CostumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 服装服务类
 * 提供服装管理相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class CostumeService {

    private final CostumeRepository costumeRepository;

    @Autowired
    public CostumeService(CostumeRepository costumeRepository) {
        this.costumeRepository = costumeRepository;
    }

    /**
     * 获取所有服装
     * @return 服装列表
     */
    public List<Costume> getAllCostumes() {
        return costumeRepository.findAll();
    }

    /**
     * 获取可用服装
     * @return 服装列表
     */
    public List<Costume> getAvailableCostumes() {
        return costumeRepository.findByAvailableTrue();
    }

    /**
     * 获取可用且已清洁的服装
     * @return 服装列表
     */
    public List<Costume> getAvailableAndCleanCostumes() {
        return costumeRepository.findByAvailableTrueAndCleaningStatus(CleaningStatus.CLEAN);
    }

    /**
     * 根据ID获取服装
     * @param id 服装ID
     * @return 服装信息
     */
    public Optional<Costume> getCostumeById(Long id) {
        return costumeRepository.findById(id);
    }

    /**
     * 根据编号获取服装
     * @param costumeNo 服装编号
     * @return 服装信息
     */
    public Costume getCostumeByNo(String costumeNo) {
        return costumeRepository.findByCostumeNo(costumeNo);
    }

    /**
     * 根据类型获取服装
     * @param type 服装类型
     * @return 服装列表
     */
    public List<Costume> getCostumesByType(CostumeType type) {
        return costumeRepository.findByType(type);
    }

    /**
     * 获取可用的指定类型服装
     * @param type 服装类型
     * @return 服装列表
     */
    public List<Costume> getAvailableCostumesByType(CostumeType type) {
        return costumeRepository.findByTypeAndAvailableTrue(type);
    }

    /**
     * 根据清洗状态获取服装
     * @param cleaningStatus 清洗状态
     * @return 服装列表
     */
    public List<Costume> getCostumesByCleaningStatus(CleaningStatus cleaningStatus) {
        return costumeRepository.findByCleaningStatus(cleaningStatus);
    }

    /**
     * 根据名称搜索服装
     * @param name 名称关键词
     * @return 服装列表
     */
    public List<Costume> searchCostumesByName(String name) {
        return costumeRepository.findByNameContaining(name);
    }

    /**
     * 创建服装
     * @param costume 服装信息
     * @return 创建的服装
     */
    @Transactional
    public Costume createCostume(Costume costume) {
        return costumeRepository.save(costume);
    }

    /**
     * 更新服装信息
     * @param id 服装ID
     * @param costume 服装信息
     * @return 更新后的服装
     */
    @Transactional
    public Costume updateCostume(Long id, Costume costume) {
        Costume existing = costumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("服装不存在"));
        
        existing.setName(costume.getName());
        existing.setCostumeNo(costume.getCostumeNo());
        existing.setType(costume.getType());
        existing.setSize(costume.getSize());
        existing.setColor(costume.getColor());
        existing.setCleaningStatus(costume.getCleaningStatus());
        existing.setDescription(costume.getDescription());
        existing.setAvailable(costume.getAvailable());
        
        return costumeRepository.save(existing);
    }

    /**
     * 删除服装
     * @param id 服装ID
     */
    @Transactional
    public void deleteCostume(Long id) {
        if (!costumeRepository.existsById(id)) {
            throw new RuntimeException("服装不存在");
        }
        costumeRepository.deleteById(id);
    }

    /**
     * 标记服装使用
     * @param id 服装ID
     * @return 更新后的服装
     */
    @Transactional
    public Costume markAsUsed(Long id) {
        Costume costume = costumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("服装不存在"));
        costume.setUseCount(costume.getUseCount() + 1);
        costume.setCleaningStatus(CleaningStatus.TO_BE_CLEANED);
        return costumeRepository.save(costume);
    }

    /**
     * 标记服装已清洁
     * @param id 服装ID
     * @return 更新后的服装
     */
    @Transactional
    public Costume markAsCleaned(Long id) {
        Costume costume = costumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("服装不存在"));
        costume.setCleaningStatus(CleaningStatus.CLEAN);
        costume.setLastCleanTime(LocalDateTime.now());
        costume.setAvailable(true);
        return costumeRepository.save(costume);
    }

    /**
     * 标记服装清洗中
     * @param id 服装ID
     * @return 更新后的服装
     */
    @Transactional
    public Costume markAsCleaning(Long id) {
        Costume costume = costumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("服装不存在"));
        costume.setCleaningStatus(CleaningStatus.CLEANING);
        costume.setAvailable(false);
        return costumeRepository.save(costume);
    }
}
