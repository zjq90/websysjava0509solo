package com.inventory.repository;

import com.inventory.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 品类Repository接口
 * 继承JpaRepository，提供基本的CRUD操作
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 根据状态查询品类列表
     * @param status 状态（1-启用，0-禁用）
     * @return 品类列表
     */
    List<Category> findByStatus(Integer status);

    /**
     * 根据名称模糊查询
     * @param name 名称关键字
     * @return 品类列表
     */
    List<Category> findByNameContaining(String name);

    /**
     * 根据编码查询
     * @param code 编码
     * @return 品类对象
     */
    Category findByCode(String code);

    /**
     * 检查名称是否存在
     * @param name 名称
     * @return 是否存在
     */
    boolean existsByName(String name);

    /**
     * 检查编码是否存在
     * @param code 编码
     * @return 是否存在
     */
    boolean existsByCode(String code);
}
