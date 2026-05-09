package com.inventory.service;

import com.inventory.entity.Variety;
import com.inventory.repository.VarietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 品种服务层
 * 功能：处理品种的增删改查业务逻辑
 */
@Service
@Transactional
public class VarietyService {

    @Autowired
    private VarietyRepository varietyRepository;

    public List<Variety> findAll() {
        return varietyRepository.findAll();
    }

    public Optional<Variety> findById(Long id) {
        return varietyRepository.findById(id);
    }

    public Optional<Variety> findByCode(String code) {
        return varietyRepository.findByVarietyCode(code);
    }

    public List<Variety> findByCategoryId(Long categoryId) {
        return varietyRepository.findByCategoryId(categoryId);
    }

    public Variety save(Variety variety) {
        return varietyRepository.save(variety);
    }

    public void deleteById(Long id) {
        varietyRepository.deleteById(id);
    }
}
