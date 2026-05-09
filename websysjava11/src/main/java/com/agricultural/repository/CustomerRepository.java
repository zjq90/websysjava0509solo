package com.agricultural.repository;

import com.agricultural.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 客户/供应商数据访问层
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * 根据客户编号查找
     */
    Optional<Customer> findByCustomerCode(String customerCode);

    /**
     * 根据名称模糊查询
     */
    List<Customer> findByCustomerNameContaining(String name);

    /**
     * 查询所有启用的客户
     */
    List<Customer> findByEnabledTrue();

    /**
     * 根据客户类型查询
     */
    List<Customer> findByCustomerTypeAndEnabledTrue(String customerType);

    /**
     * 根据省份查询
     */
    List<Customer> findByProvince(String province);

    /**
     * 检查客户编号是否存在
     */
    boolean existsByCustomerCode(String customerCode);

    /**
     * 获取所有省份列表
     */
    @Query("SELECT DISTINCT c.province FROM Customer c WHERE c.province IS NOT NULL")
    List<String> findAllProvinces();

    /**
     * 统计客户数量
     */
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.enabled = true")
    long countEnabled();
}
