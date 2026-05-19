package com.petclinic.repository;

import com.petclinic.entity.CreditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 信用分记录Repository
 */
@Repository
public interface CreditRecordRepository extends JpaRepository<CreditRecord, Long> {

    /**
     * 根据用户ID和用户类型查询信用记录
     */
    List<CreditRecord> findByUserIdAndUserTypeOrderByCreateTimeDesc(Long userId, Integer userType);
}
