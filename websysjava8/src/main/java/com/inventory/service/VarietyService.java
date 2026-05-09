package com.inventory.service;

import com.inventory.entity.Variety;
import com.inventory.repository.VarietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 品种Service类
 */
@Service
public class VarietyService {

    @Autowired
    private VarietyRepository varietyRepository;

    /**
     * 查询所有品种
     */
    public List<Variety> findAll() {
        return varietyRepository.findAll();
    }

    /**
     * 根据ID查询
     */
    public Variety findById(Long id) {
        Optional<Variety> optional = varietyRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 保存品种
     */
    @Transactional(rollbackFor = Exception.class)
    public Variety save(Variety variety) {
        return varietyRepository.save(variety);
    }

    /**
     * 删除品种
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        varietyRepository.deleteById(id);
    }

    /**
     * 根据品类ID查询
     */
    public List<Variety> findByCategoryId(Long categoryId) {
        return varietyRepository.findByCategoryId(categoryId);
    }

    /**
     * 根据品类ID和状态查询
     */
    public List<Variety> findByCategoryIdAndStatus(Long categoryId, Integer status) {
        return varietyRepository.findByCategoryIdAndStatus(categoryId, status);
    }

    /**
     * 检查名称是否存在
     */
    public boolean existsByName(String name) {
        return varietyRepository.existsByName(name);
    }

    /**
     * 检查编码是否存在
     */
    public boolean existsByCode(String code) {
        return varietyRepository.existsByCode(code);
    }
}
