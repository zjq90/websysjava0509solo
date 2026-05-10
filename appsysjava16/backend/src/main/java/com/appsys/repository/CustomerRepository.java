package com.appsys.repository;

import com.appsys.entity.Customer;
import com.appsys.entity.CustomerLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户数据访问接口
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * 根据客户名称模糊查询
     * @param name 客户名称关键字
     * @return 客户列表
     */
    List<Customer> findByNameContaining(String name);

    /**
     * 根据客户等级查询
     * @param level 客户等级
     * @return 客户列表
     */
    List<Customer> findByLevel(CustomerLevel level);

    /**
     * 查询临时客户
     * @return 临时客户列表
     */
    List<Customer> findByIsTemporaryTrue();

    /**
     * 查询非临时客户
     * @return 正式客户列表
     */
    List<Customer> findByIsTemporaryFalse();

    /**
     * 查询所有客户并按创建时间倒序
     * @return 客户列表
     */
    @Query("SELECT c FROM Customer c ORDER BY c.createdAt DESC")
    List<Customer> findAllOrderByCreatedAtDesc();

    /**
     * 查询消费金额排行前N的客户
     * @param limit 数量限制
     * @return 客户列表
     */
    @Query("SELECT c FROM Customer c ORDER BY c.totalPurchaseAmount DESC LIMIT :limit")
    List<Customer> findTopCustomers(int limit);
}
