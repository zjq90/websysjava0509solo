package com.photostudio.repository;

import com.photostudio.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 客户Repository
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * 统计老客户数量
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.oldCustomer = true")
    Long countOldCustomers();
    
    /**
     * 统计转介绍客户数量
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.referral IS NOT NULL")
    Long countReferralCustomers();
    
    /**
     * 根据时间范围统计新增客户数量
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.createTime BETWEEN :startTime AND :endTime")
    Long countByTimeRange(@Param("startTime") LocalDateTime startTime, 
                           @Param("endTime") LocalDateTime endTime);
}
