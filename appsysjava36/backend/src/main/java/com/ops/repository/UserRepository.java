package com.ops.repository;

import com.ops.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据访问接口
 * 
 * @author ops-admin
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);

    /**
     * 根据手机号查找用户
     */
    User findByPhone(String phone);

    /**
     * 查找即将到期的用户
     */
    List<User> findByExpireDateBeforeAndIsActive(LocalDateTime date, Boolean isActive);

    /**
     * 查找低活跃用户
     */
    List<User> findByLastLoginTimeBeforeAndIsActive(LocalDateTime date, Boolean isActive);

    /**
     * 根据套餐类型统计用户数
     */
    long countByPackageType(String packageType);

    /**
     * 根据状态统计用户数
     */
    long countByStatus(String status);
}
