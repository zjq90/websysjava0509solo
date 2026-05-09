package com.inventory.repository;

import com.inventory.entity.Variety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 品种Repository接口
 */
@Repository
public interface VarietyRepository extends JpaRepository<Variety, Long> {

    /**
     * 根据状态查询品种列表
     */
    List<Variety> findByStatus(Integer status);

    /**
     * 根据品类ID查询品种列表
     */
    List<Variety> findByCategoryId(Long categoryId);

    /**
     * 根据品类ID和状态查询
     */
    List<Variety> findByCategoryIdAndStatus(Long categoryId, Integer status);

    /**
     * 根据名称模糊查询
     */
    List<Variety> findByNameContaining(String name);

    /**
     * 根据编码查询
     */
    Variety findByCode(String code);

    /**
     * 检查名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 检查编码是否存在
     */
    boolean existsByCode(String code);
}
