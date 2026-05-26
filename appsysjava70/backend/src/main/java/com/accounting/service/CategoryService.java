package com.accounting.service;

import com.accounting.entity.Category;
import com.accounting.enums.BillType;
import com.accounting.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Cacheable(value = "categories", key = "#type")
    public List<Category> getCategoriesByType(BillType type) {
        log.info("获取{}分类列表", type.getDescription());
        return categoryRepository.findByTypeOrderBySortOrderAsc(type);
    }

    @Cacheable(value = "categories", key = "'all'")
    public List<Category> getAllCategories() {
        log.info("获取全部分类列表");
        return categoryRepository.findAllByOrderBySortOrderAsc();
    }

    public Category getCategoryById(Long id) {
        log.info("根据ID获取分类: {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("分类不存在: " + id));
    }

    public Optional<Category> findByNameAndType(String name, BillType type) {
        log.debug("根据名称和类型查找分类: name={}, type={}", name, type);
        return categoryRepository.findByNameAndType(name, type);
    }

    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public Category createCategory(Category category) {
        log.info("创建分类: {}", category.getName());
        category.setId(null);
        return categoryRepository.save(category);
    }

    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public Category updateCategory(Long id, Category category) {
        log.info("更新分类: {}", id);
        Category existing = getCategoryById(id);
        existing.setName(category.getName());
        existing.setIcon(category.getIcon());
        existing.setType(category.getType());
        existing.setParentName(category.getParentName());
        existing.setSortOrder(category.getSortOrder());
        return categoryRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public void deleteCategory(Long id) {
        log.info("删除分类: {}", id);
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("分类不存在: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
