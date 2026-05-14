package com.photostudio.repository;

import com.photostudio.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 客户数据访问层
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    /**
     * 根据手机号查询客户（排除已删除）
     */
    Optional<Customer> findByPhoneAndDeletedFalse(String phone);

    /**
     * 查询所有未删除的客户
     */
    List<Customer> findAllByDeletedFalse();

    /**
     * 根据生命周期阶段查询客户
     */
    List<Customer> findByLifecycleAndDeletedFalse(Customer.CustomerLifecycle lifecycle);

    /**
     * 根据姓名模糊查询客户
     */
    List<Customer> findByNameContainingAndDeletedFalse(String name);

    /**
     * 根据拍摄类型查询客户
     */
    List<Customer> findByPhotoTypeAndDeletedFalse(Customer.PhotoType photoType);

    /**
     * 检查手机号是否已存在
     */
    boolean existsByPhoneAndDeletedFalse(String phone);
}
