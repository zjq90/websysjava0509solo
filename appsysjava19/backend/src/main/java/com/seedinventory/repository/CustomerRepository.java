package com.seedinventory.repository;

import com.seedinventory.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 客户数据访问层
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * 根据客户编号查询
     */
    Optional<Customer> findByCustomerCode(String customerCode);
    
    /**
     * 根据状态查询
     */
    List<Customer> findByStatus(String status);
    
    /**
     * 判断客户编号是否存在
     */
    boolean existsByCustomerCode(String customerCode);
}
