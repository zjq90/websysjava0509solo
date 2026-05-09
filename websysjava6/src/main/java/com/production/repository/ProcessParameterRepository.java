package com.production.repository;

import com.production.entity.ProcessParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工艺参数数据访问层
 */
@Repository
public interface ProcessParameterRepository extends JpaRepository<ProcessParameter, Long> {

    /**
     * 根据加工记录ID查询
     */
    List<ProcessParameter> findByProcessingRecordIdOrderByRecordTime(Long recordId);

    /**
     * 根据加工记录ID分页查询
     */
    Page<ProcessParameter> findByProcessingRecordId(Long recordId, Pageable pageable);

    /**
     * 根据参数名称和记录ID查询
     */
    List<ProcessParameter> findByProcessingRecordIdAndParameterName(Long recordId, String parameterName);

    /**
     * 查询指定时间范围内的参数记录
     */
    @Query("SELECT p FROM ProcessParameter p WHERE p.processingRecord.id = :recordId AND p.recordTime BETWEEN :startTime AND :endTime ORDER BY p.recordTime")
    List<ProcessParameter> findByTimeRange(@Param("recordId") Long recordId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询异常参数
     */
    @Query("SELECT p FROM ProcessParameter p WHERE p.valueStatus = 'ABNORMAL' ORDER BY p.recordTime DESC")
    List<ProcessParameter> findAllAbnormal();
}