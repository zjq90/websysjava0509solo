package com.seedinventory.repository;

import com.seedinventory.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 仓库数据访问层
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    
    /**
     * 根据仓库编号查询
     */
    Optional<Warehouse> findByWarehouseCode(String warehouseCode);
    
    /**
     * 根据状态查询仓库列表
     */
    List<Warehouse> findByStatus(String status);
    
    /**
     * 判断仓库编号是否存在
     */
    boolean existsByWarehouseCode(String warehouseCode);
}
