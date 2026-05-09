package com.inventory.repository;

import com.inventory.entity.StockTake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 盘点单Repository接口
 */
@Repository
public interface StockTakeRepository extends JpaRepository<StockTake, Long> {

    /**
     * 根据盘点单号查询
     */
    StockTake findByOrderNo(String orderNo);

    /**
     * 根据状态查询盘点单列表
     */
    List<StockTake> findByStatus(Integer status);

    /**
     * 根据盘点位置查询
     */
    List<StockTake> findByLocationIdAndLocationType(Long locationId, Integer locationType);

    /**
     * 根据盘点类型查询
     */
    List<StockTake> findByTakeType(Integer takeType);

    /**
     * 查询某个时间范围内的盘点单
     */
    @Query("SELECT s FROM StockTake s WHERE s.createdAt BETWEEN :startTime AND :endTime ORDER BY s.createdAt DESC")
    List<StockTake> findByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 检查单号是否存在
     */
    boolean existsByOrderNo(String orderNo);
}
