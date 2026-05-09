package com.inventory.repository;

import com.inventory.entity.SeedBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 种子批次Repository接口
 */
@Repository
public interface SeedBatchRepository extends JpaRepository<SeedBatch, Long> {

    /**
     * 根据批次号查询
     */
    SeedBatch findByBatchNo(String batchNo);

    /**
     * 根据品种ID查询批次列表
     */
    List<SeedBatch> findByVarietyId(Long varietyId);

    /**
     * 根据品种ID和状态查询
     */
    List<SeedBatch> findByVarietyIdAndStatus(Long varietyId, Integer status);

    /**
     * 根据状态查询
     */
    List<SeedBatch> findByStatus(Integer status);

    /**
     * 查询近效期批次（即将过期的）
     * @param warningDate 警告日期（当前日期 + 警告天数）
     * @return 近效期批次列表
     */
    @Query("SELECT sb FROM SeedBatch sb WHERE sb.expiryDate <= :warningDate AND sb.expiryDate > :currentDate AND sb.status <> 2 AND sb.availableQuantity > 0")
    List<SeedBatch> findNearExpiryBatches(@Param("warningDate") LocalDate warningDate, @Param("currentDate") LocalDate currentDate);

    /**
     * 查询已过期批次
     */
    @Query("SELECT sb FROM SeedBatch sb WHERE sb.expiryDate <= :currentDate AND sb.availableQuantity > 0")
    List<SeedBatch> findExpiredBatches(@Param("currentDate") LocalDate currentDate);

    /**
     * 查询某个品种按先进先出排序的可用批次（按生产日期排序）
     */
    @Query("SELECT sb FROM SeedBatch sb WHERE sb.varietyId = :varietyId AND sb.availableQuantity > 0 AND sb.status <> 2 ORDER BY sb.productionDate ASC")
    List<SeedBatch> findAvailableBatchesFIFO(@Param("varietyId") Long varietyId);

    /**
     * 检查批次号是否存在
     */
    boolean existsByBatchNo(String batchNo);
}
