package com.photostudio.repository;

import com.photostudio.entity.InteractionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 互动记录数据访问层
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface InteractionRecordRepository extends JpaRepository<InteractionRecord, Long>, JpaSpecificationExecutor<InteractionRecord> {

    /**
     * 根据客户ID查询互动记录
     */
    List<InteractionRecord> findByCustomerIdAndDeletedFalseOrderByCreateTimeDesc(Long customerId);

    /**
     * 查询所有未删除的互动记录
     */
    List<InteractionRecord> findAllByDeletedFalse();
}
