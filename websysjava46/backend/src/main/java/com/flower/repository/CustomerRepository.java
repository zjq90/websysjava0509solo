package com.flower.repository;

import com.flower.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户数据访问层
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    /**
     * 根据手机号查询客户
     */
    Customer findByPhone(String phone);

    /**
     * 根据标签查询客户
     */
    @Query("SELECT c FROM Customer c WHERE c.tags LIKE %:tag%")
    List<Customer> findByTagsContaining(String tag);

    /**
     * 根据状态查询客户
     */
    List<Customer> findByStatus(Integer status);
}
