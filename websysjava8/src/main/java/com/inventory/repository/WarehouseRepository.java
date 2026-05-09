package com.inventory.repository;

import com.inventory.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 仓库Repository接口
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    /**
     * 根据状态查询仓库列表
     */
    List<Warehouse> findByStatus(Integer status);

    /**
     * 根据名称模糊查询
     */
    List<Warehouse> findByNameContaining(String name);

    /**
     * 根据编码查询
     */
    Warehouse findByCode(String code);

    /**
     * 根据仓库类型查询
     */
    List<Warehouse> findByType(Integer type);

    /**
     * 检查名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 检查编码是否存在
     */
    boolean existsByCode(String code);
}
