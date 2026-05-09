package com.sales.repository;

import com.sales.entity.Customer;
import com.sales.entity.CustomerLevel;
import com.sales.entity.CustomerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 客户数据访问层接口
 * 继承JpaRepository，提供基础的增删改查功能
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * 根据客户编码查找客户
     */
    Optional<Customer> findByCode(String code);

    /**
     * 查找所有启用的客户
     */
    List<Customer> findByActiveTrue();

    /**
     * 按客户类型查找
     */
    List<Customer> findByType(CustomerType type);

    /**
     * 按客户等级查找
     */
    List<Customer> findByLevel(CustomerLevel level);

    /**
     * 按省份查找
     */
    List<Customer> findByProvince(String province);

    /**
     * 根据名称模糊查找
     */
    List<Customer> findByNameContaining(String name);

    /**
     * 根据标签查找（标签用逗号分隔）
     */
    List<Customer> findByTagsContaining(String tag);

    /**
     * 分页查询所有客户，按创建时间倒序
     */
    Page<Customer> findAllByOrderByCreateTimeDesc(Pageable pageable);

    /**
     * 分页查询所有启用的客户
     */
    Page<Customer> findByActiveTrueOrderByCreateTimeDesc(Pageable pageable);

    /**
     * 按类型分页查询
     */
    Page<Customer> findByTypeOrderByCreateTimeDesc(CustomerType type, Pageable pageable);

    /**
     * 按等级分页查询
     */
    Page<Customer> findByLevelOrderByCreateTimeDesc(CustomerLevel level, Pageable pageable);

    /**
     * 按名称模糊分页查询
     */
    Page<Customer> findByNameContainingOrderByCreateTimeDesc(String name, Pageable pageable);
}
