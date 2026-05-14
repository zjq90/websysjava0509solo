package com.photostudio.repository;

import com.photostudio.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户数据访问接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    /**
     * 根据手机号查找客户
     */
    Customer findByPhone(String phone);

    /**
     * 根据姓名模糊查询
     */
    List<Customer> findByNameContaining(String name);

    /**
     * 根据来源查询客户
     */
    List<Customer> findBySource(String source);

    /**
     * 根据VIP等级查询客户
     */
    List<Customer> findByVipLevel(Integer vipLevel);
}
