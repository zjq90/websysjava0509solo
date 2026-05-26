package com.personal.accounting.service;

import com.personal.accounting.entity.Category;
import com.personal.accounting.entity.enums.CategoryType;
import com.personal.accounting.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类服务类
 * 提供分类管理功能
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 获取所有分类
     */
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findByEnabledTrueOrderBySortOrderAsc();
    }

    /**
     * 根据类型获取分类
     */
    @Transactional(readOnly = true)
    public List<Category> findByType(CategoryType type) {
        return categoryRepository.findByTypeAndEnabledTrueOrderBySortOrderAsc(type);
    }

    /**
     * 根据ID获取分类
     */
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("分类不存在: " + id));
    }

    /**
     * 创建分类
     */
    @Transactional
    public Category create(Category category) {
        log.debug("创建分类: {}", category.getName());
        Category saved = categoryRepository.save(category);
        log.info("分类创建成功: {}", saved.getId());
        return saved;
    }

    /**
     * 更新分类
     */
    @Transactional
    public Category update(Long id, Category category) {
        log.debug("更新分类: {}", id);
        
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("分类不存在: " + id));
        
        existing.setName(category.getName());
        existing.setType(category.getType());
        existing.setColor(category.getColor());
        existing.setIcon(category.getIcon());
        existing.setDescription(category.getDescription());
        existing.setSortOrder(category.getSortOrder());
        
        Category saved = categoryRepository.save(existing);
        log.info("分类更新成功: {}", saved.getId());
        return saved;
    }

    /**
     * 删除分类（逻辑删除）
     */
    @Transactional
    public void delete(Long id) {
        log.debug("删除分类: {}", id);
        
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("分类不存在: " + id));
        
        category.setEnabled(false);
        categoryRepository.save(category);
        log.info("分类已禁用: {}", id);
    }
}
