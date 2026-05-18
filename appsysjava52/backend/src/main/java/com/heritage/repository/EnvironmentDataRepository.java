package com.heritage.repository;

import com.heritage.entity.EnvironmentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 环境监测数据Repository
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Repository
public interface EnvironmentDataRepository extends JpaRepository<EnvironmentData, Long> {

    /**
     * 根据收藏项ID查询环境数据
     */
    List<EnvironmentData> findByCollectionItemIdOrderByRecordTimeDesc(Long collectionItemId);

    /**
     * 查询指定时间范围内的环境数据
     */
    List<EnvironmentData> findByCollectionItemIdAndRecordTimeBetweenOrderByRecordTimeAsc(
            Long collectionItemId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询异常数据
     */
    List<EnvironmentData> findByCollectionItemIdAndIsAbnormalOrderByRecordTimeDesc(Long collectionItemId, Integer isAbnormal);
}