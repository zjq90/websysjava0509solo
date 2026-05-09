package com.agriculture.repository;

import com.agriculture.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 气象数据访问层
 * 提供气象数据表的基本CRUD操作
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    /**
     * 根据地块ID查找气象数据
     * 
     * @param plotId 地块ID
     * @return 气象数据列表
     */
    List<WeatherData> findByPlotIdOrderByRecordDateDesc(Long plotId);

    /**
     * 根据城市编码查找
     * 
     * @param cityCode 城市编码
     * @return 气象数据列表
     */
    List<WeatherData> findByCityCodeOrderByRecordDateDesc(String cityCode);

    /**
     * 查询某个地块在时间范围内的气象数据
     * 
     * @param plotId 地块ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 气象数据列表
     */
    @Query("SELECT w FROM WeatherData w WHERE w.plotId = :plotId AND w.recordDate BETWEEN :startTime AND :endTime ORDER BY w.recordDate DESC")
    List<WeatherData> findByPlotIdAndDateRange(
            @Param("plotId") Long plotId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    /**
     * 查询某个城市最新的气象数据
     * 
     * @param cityCode 城市编码
     * @return 最新气象数据
     */
    @Query("SELECT w FROM WeatherData w WHERE w.cityCode = :cityCode ORDER BY w.recordDate DESC")
    List<WeatherData> findLatestByCityCode(@Param("cityCode") String cityCode);
}
