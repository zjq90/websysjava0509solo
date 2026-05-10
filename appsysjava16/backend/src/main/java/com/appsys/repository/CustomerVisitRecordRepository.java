package com.appsys.repository;

import com.appsys.entity.CustomerVisitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户回访记录数据访问接口
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Repository
public interface CustomerVisitRecordRepository extends JpaRepository<CustomerVisitRecord, Long> {

    /**
     * 根据客户ID查询回访记录
     * @param customerId 客户ID
     * @return 回访记录列表
     */
    List<CustomerVisitRecord> findByCustomerIdOrderByVisitTimeDesc(Long customerId);

    /**
     * 根据业务员ID查询回访记录
     * @param salespersonId 业务员ID
     * @return 回访记录列表
     */
    List<CustomerVisitRecord> findBySalespersonIdOrderByVisitTimeDesc(Long salespersonId);
}
