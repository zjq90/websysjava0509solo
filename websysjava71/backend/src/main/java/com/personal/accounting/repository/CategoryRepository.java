package com.personal.accounting.repository;

import com.personal.accounting.entity.Category;
import com.personal.accounting.entity.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 分类数据访问接口
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 根据类型查询分类列表
     */
    List<Category> findByTypeAndEnabledTrueOrderBySortOrderAsc(CategoryType type);

    /**
     * 查询所有启用的分类
     */
    List<Category> findByEnabledTrueOrderBySortOrderAsc();

    /**
     * 根据名称和类型查询分类
     */
    Optional<Category> findByNameAndType(String name, CategoryType type);
}
