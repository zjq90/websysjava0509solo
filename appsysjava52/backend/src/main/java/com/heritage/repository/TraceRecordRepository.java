package com.heritage.repository;

import com.heritage.entity.TraceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 溯源记录Repository
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Repository
public interface TraceRecordRepository extends JpaRepository<TraceRecord, Long> {

    /**
     * 根据文物ID查询溯源记录
     */
    List<TraceRecord> findByHeritageIdOrderByOccurTimeDesc(Long heritageId);
}