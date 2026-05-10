package com.management.platform.repository;

import com.management.platform.entity.UserBehavior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户行为记录数据访问层
 * 提供用户行为记录的增删改查和统计查询功能
 */
@Repository
public interface UserBehaviorRepository extends JpaRepository<UserBehavior, Long> {

    /**
     * 按用户ID查询行为记录
     * @param userId 用户ID
     * @return 行为记录列表
     */
    List<UserBehavior> findByUserId(Long userId);

    /**
     * 按行为类型统计
     * @param behaviorType 行为类型
     * @return 记录数量
     */
    long countByBehaviorType(String behaviorType);

    /**
     * 统计指定时间范围内的活跃用户数（去重）
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 活跃用户数
     */
    @Query("SELECT COUNT(DISTINCT ub.userId) FROM UserBehavior ub WHERE ub.behaviorTime BETWEEN :startTime AND :endTime")
    long countActiveUsersBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 按行为类型和时间范围统计
     * @param behaviorType 行为类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 记录数量
     */
    @Query("SELECT COUNT(ub) FROM UserBehavior ub WHERE ub.behaviorType = :behaviorType " +
           "AND ub.behaviorTime BETWEEN :startTime AND :endTime")
    long countByBehaviorTypeAndTime(@Param("behaviorType") String behaviorType,
                                    @Param("startTime") LocalDateTime startTime,
                                    @Param("endTime") LocalDateTime endTime);
}
