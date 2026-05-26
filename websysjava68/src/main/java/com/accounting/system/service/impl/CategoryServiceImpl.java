package com.accounting.system.service.impl;

import com.accounting.system.common.BusinessException;
import com.accounting.system.common.ResultCode;
import com.accounting.system.dto.CategoryDTO;
import com.accounting.system.entity.Category;
import com.accounting.system.mapper.CategoryMapper;
import com.accounting.system.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类Service实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Override
    public List<Category> listByType(String categoryType) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getCategoryType, categoryType)
                .orderByAsc(Category::getSortOrder);
        return list(wrapper);
    }

    @Override
    public List<Category> listAll() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getCategoryType)
                .orderByAsc(Category::getSortOrder);
        return list(wrapper);
    }

    @Override
    public Category getDetailById(Long id) {
        Category category = getById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category addCategory(CategoryDTO dto) {
        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        save(category);
        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Category updateCategory(CategoryDTO dto) {
        Category existCategory = getById(dto.getId());
        if (existCategory == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        BeanUtils.copyProperties(dto, existCategory);
        updateById(existCategory);
        return existCategory;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        Category existCategory = getById(id);
        if (existCategory == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        removeById(id);
    }
}
