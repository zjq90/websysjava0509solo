package com.secondhand.repository;

import com.secondhand.entity.PointRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 积分记录Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface PointRecordRepository extends JpaRepository<PointRecord, Long> {

    List<PointRecord> findByUserIdOrderByCreatedTimeDesc(Long userId);

}
