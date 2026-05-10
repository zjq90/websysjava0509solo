package com.management.platform.repository;

import com.management.platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据访问层
 * 提供用户的增删改查和统计查询功能
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 按用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(String username);

    /**
     * 按状态统计用户数量
     * @param status 用户状态
     * @return 用户数量
     */
    long countByStatus(String status);

    /**
     * 统计指定时间范围内的新用户数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 新用户数量
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt BETWEEN :startTime AND :endTime")
    long countNewUsersBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 查询活跃用户（最近登录过的用户）
     * @param since 时间范围
     * @return 活跃用户列表
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginTime >= :since")
    List<User> findActiveUsers(@Param("since") LocalDateTime since);
}
