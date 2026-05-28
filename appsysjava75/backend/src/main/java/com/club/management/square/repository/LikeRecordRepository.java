package com.club.management.square.repository;

import com.club.management.square.entity.LikeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 点赞Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface LikeRecordRepository extends JpaRepository<LikeRecord, Long>, JpaSpecificationExecutor<LikeRecord> {

    LikeRecord findByBusinessTypeAndBusinessIdAndUserId(Integer businessType, Long businessId, Long userId);

    void deleteByBusinessTypeAndBusinessIdAndUserId(Integer businessType, Long businessId, Long userId);
}
