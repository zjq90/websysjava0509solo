package com.accounting.system.service;

import com.accounting.system.dto.CategoryDTO;
import com.accounting.system.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 分类Service接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 根据类型查询分类列表
     *
     * @param categoryType 分类类型：INCOME-收入，EXPENSE-支出
     * @return 分类列表
     */
    List<Category> listByType(String categoryType);

    /**
     * 查询所有分类列表
     *
     * @return 分类列表
     */
    List<Category> listAll();

    /**
     * 根据ID获取分类详情
     *
     * @param id 分类ID
     * @return 分类详情
     */
    Category getDetailById(Long id);

    /**
     * 新增分类
     *
     * @param dto 分类请求DTO
     * @return 新增的分类
     */
    Category addCategory(CategoryDTO dto);

    /**
     * 更新分类
     *
     * @param dto 分类请求DTO
     * @return 更新后的分类
     */
    Category updateCategory(CategoryDTO dto);

    /**
     * 删除分类
     *
     * @param id 分类ID
     */
    void deleteCategory(Long id);
}
