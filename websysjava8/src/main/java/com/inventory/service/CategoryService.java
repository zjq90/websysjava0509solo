package com.inventory.service;

import com.inventory.entity.Category;
import com.inventory.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 品类Service类
 * 处理品类相关的业务逻辑
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 查询所有品类
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * 根据ID查询品类
     */
    public Category findById(Long id) {
        Optional<Category> optional = categoryRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 保存品类（新增或更新）
     */
    @Transactional(rollbackFor = Exception.class)
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * 删除品类
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * 检查名称是否存在
     */
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    /**
     * 检查编码是否存在
     */
    public boolean existsByCode(String code) {
        return categoryRepository.existsByCode(code);
    }

    /**
     * 根据状态查询品类
     */
    public List<Category> findByStatus(Integer status) {
        return categoryRepository.findByStatus(status);
    }

    /**
     * 根据名称模糊查询
     */
    public List<Category> searchByName(String keyword) {
        return categoryRepository.findByNameContaining(keyword);
    }
}
