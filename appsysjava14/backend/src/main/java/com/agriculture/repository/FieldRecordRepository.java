package com.agriculture.repository;

import com.agriculture.entity.FieldRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 田间记录数据访问层
 * 提供田间记录表的基本CRUD操作
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Repository
public interface FieldRecordRepository extends JpaRepository<FieldRecord, Long> {

    /**
     * 根据记录编号查找
     * 
     * @param recordNo 记录编号
     * @return 田间记录对象
     */
    Optional<FieldRecord> findByRecordNo(String recordNo);

    /**
     * 根据地块ID查找记录
     * 
     * @param plotId 地块ID
     * @return 记录列表
     */
    List<FieldRecord> findByPlotIdOrderByRecordDateDesc(Long plotId);

    /**
     * 根据作物ID查找记录
     * 
     * @param cropId 作物ID
     * @return 记录列表
     */
    List<FieldRecord> findByCropIdOrderByRecordDateDesc(Long cropId);

    /**
     * 根据观测人ID查找记录
     * 
     * @param observerId 观测人ID
     * @return 记录列表
     */
    List<FieldRecord> findByObserverIdOrderByCreatedAtDesc(Long observerId);

    /**
     * 根据地块ID和生长阶段查找
     * 
     * @param plotId 地块ID
     * @param growthStage 生长阶段
     * @return 记录列表
     */
    List<FieldRecord> findByPlotIdAndGrowthStageOrderByRecordDateDesc(Long plotId, String growthStage);

    /**
     * 检查记录编号是否存在
     * 
     * @param recordNo 记录编号
     * @return 是否存在
     */
    boolean existsByRecordNo(String recordNo);

    /**
     * 统计用户记录数量
     * 
     * @param observerId 观测人ID
     * @return 记录数量
     */
    long countByObserverId(Long observerId);

    /**
     * 查询最新的田间记录
     * 
     * @return 记录列表
     */
    @Query("SELECT fr FROM FieldRecord fr ORDER BY fr.createdAt DESC")
    List<FieldRecord> findLatestRecords();
}
