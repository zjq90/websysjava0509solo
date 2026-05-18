package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.PointRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 积分记录数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface PointRecordRepository extends JpaRepository<PointRecord, Long> {

    /**
     * 查询用户的积分记录
     */
    List<PointRecord> findByUserIdOrderByCreateTimeDesc(Long userId);
}
