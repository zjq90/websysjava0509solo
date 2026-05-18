package com.secondhand.repository;

import com.secondhand.entity.BargainRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 砍价记录Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface BargainRecordRepository extends JpaRepository<BargainRecord, Long> {

    List<BargainRecord> findByInitiatorIdOrderByCreateTimeDesc(Long initiatorId);

    boolean existsByActivityIdAndInitiatorId(Long activityId, Long initiatorId);

}
