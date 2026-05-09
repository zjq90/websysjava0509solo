package com.agricultural.service;

import com.agricultural.entity.Variety;
import com.agricultural.repository.VarietyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 品种管理服务类
 * 提供品种的增删改查功能
 */
@Service
public class VarietyService {

    private final VarietyRepository varietyRepository;

    public VarietyService(VarietyRepository varietyRepository) {
        this.varietyRepository = varietyRepository;
    }

    /**
     * 查询所有品种
     */
    public List<Variety> findAll() {
        return varietyRepository.findAll();
    }

    /**
     * 分页查询所有品种
     */
    public Page<Variety> findAll(Pageable pageable) {
        return varietyRepository.findAll(pageable);
    }

    /**
     * 查询所有启用的品种
     */
    public List<Variety> findAllEnabled() {
        return varietyRepository.findByEnabledTrue();
    }

    /**
     * 根据ID查询品种
     */
    public Optional<Variety> findById(Long id) {
        return varietyRepository.findById(id);
    }

    /**
     * 根据品种编号查询
     */
    public Optional<Variety> findByCode(String code) {
        return varietyRepository.findByVarietyCode(code);
    }

    /**
     * 根据名称模糊查询
     */
    public List<Variety> searchByName(String name) {
        return varietyRepository.findByVarietyNameContaining(name);
    }

    /**
     * 保存品种（新增/更新）
     */
    @Transactional
    public Variety save(Variety variety) {
        return varietyRepository.save(variety);
    }

    /**
     * 根据ID删除品种
     */
    @Transactional
    public void deleteById(Long id) {
        varietyRepository.deleteById(id);
    }

    /**
     * 检查品种编号是否存在
     */
    public boolean existsByCode(String code) {
        return varietyRepository.existsByVarietyCode(code);
    }

    /**
     * 切换启用状态
     */
    @Transactional
    public boolean toggleEnabled(Long id) {
        return varietyRepository.findById(id).map(variety -> {
            variety.setEnabled(!variety.getEnabled());
            varietyRepository.save(variety);
            return variety.getEnabled();
        }).orElse(false);
    }
}
