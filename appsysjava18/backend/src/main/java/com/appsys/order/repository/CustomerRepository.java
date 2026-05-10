package com.appsys.order.repository;

import com.appsys.order.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 客户数据访问接口
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    /**
     * 根据ID查询未删除的客户
     */
    Optional<Customer> findByIdAndDeletedFalse(Long id);

    /**
     * 查询所有未删除的客户
     */
    List<Customer> findByDeletedFalseOrderByCreatedTimeDesc();

    /**
     * 根据名称模糊查询
     */
    List<Customer> findByCustomerNameContainingAndDeletedFalse(String customerName);
}
