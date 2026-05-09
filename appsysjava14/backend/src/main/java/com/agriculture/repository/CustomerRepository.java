package com.agriculture.repository;

import com.agriculture.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 客户数据访问层
 * 提供客户表的基本CRUD操作
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * 根据客户编号查找
     * 
     * @param customerCode 客户编号
     * @return 客户对象
     */
    Optional<Customer> findByCustomerCode(String customerCode);

    /**
     * 根据状态查找客户列表
     * 
     * @param status 状态
     * @return 客户列表
     */
    List<Customer> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * 根据客户类型查找
     * 
     * @param customerType 客户类型
     * @return 客户列表
     */
    List<Customer> findByCustomerTypeOrderByCreatedAtDesc(String customerType);

    /**
     * 检查客户编号是否存在
     * 
     * @param customerCode 客户编号
     * @return 是否存在
     */
    boolean existsByCustomerCode(String customerCode);

    /**
     * 根据客户等级查找
     * 
     * @param customerLevel 客户等级
     * @return 客户列表
     */
    List<Customer> findByCustomerLevelAndStatus(String customerLevel, String status);
}
