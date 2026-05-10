package com.appsys.inventory.repository;

import com.appsys.inventory.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 库存数据访问接口
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {

    /**
     * 根据ID查询未删除的库存
     */
    Optional<Inventory> findByIdAndDeletedFalse(Long id);

    /**
     * 根据批次号查询
     */
    Optional<Inventory> findByBatchNo(String batchNo);

    /**
     * 检查批次号是否存在
     */
    boolean existsByBatchNo(String batchNo);

    /**
     * 分页查询未删除的库存
     */
    Page<Inventory> findByDeletedFalseOrderByCreatedTimeDesc(Pageable pageable);

    /**
     * 根据批次号或种子名称模糊查询（分页）
     */
    @Query("SELECT i FROM Inventory i WHERE i.deleted = false AND " +
           "(i.batchNo LIKE %:keyword% OR i.seedName LIKE %:keyword%)")
    Page<Inventory> searchByKeyword(String keyword, Pageable pageable);
}
