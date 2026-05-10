package com.vending.service;

import com.vending.entity.Category;
import com.vending.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 分类服务类
 * 提供分类的增删改查功能
 */
@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * 获取所有分类
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    
    /**
     * 根据ID查询分类
     */
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    
    /**
     * 保存分类
     */
    @Transactional
    public Category save(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("分类名称已存在");
        }
        return categoryRepository.save(category);
    }
    
    /**
     * 更新分类
     */
    @Transactional
    public Category update(Long id, Category category) {
        Category existing = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        if (!existing.getName().equals(category.getName()) 
            && categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("分类名称已存在");
        }
        
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        return categoryRepository.save(existing);
    }
    
    /**
     * 删除分类
     */
    @Transactional
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("分类不存在"));
        
        if (!category.getProducts().isEmpty()) {
            throw new RuntimeException("该分类下还有商品，无法删除");
        }
        
        categoryRepository.deleteById(id);
    }
}
