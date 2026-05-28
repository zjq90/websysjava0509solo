package com.club.management.repository;

import com.club.management.entity.SignIn;
import com.club.management.entity.enums.SignInStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 签到记录数据访问接口
 * 
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface SignInRepository extends JpaRepository<SignIn, Long> {

    /**
     * 根据活动ID查询签到记录
     */
    List<SignIn> findByActivityIdAndDeletedFalse(Long activityId);

    /**
     * 根据活动ID和用户ID查询签到记录
     */
    Optional<SignIn> findByActivityIdAndUserIdAndDeletedFalse(Long activityId, Long userId);

    /**
     * 根据报名ID查询签到记录
     */
    Optional<SignIn> findByRegistrationIdAndDeletedFalse(Long registrationId);

    /**
     * 统计活动签到人数
     */
    @Query("SELECT COUNT(s) FROM SignIn s WHERE s.activityId = :activityId " +
           "AND s.status IN (:statuses) AND s.deleted = false")
    long countByActivityIdAndStatusIn(
            @Param("activityId") Long activityId,
            @Param("statuses") List<SignInStatus> statuses);

    /**
     * 根据活动ID和状态查询签到记录
     */
    List<SignIn> findByActivityIdAndStatusAndDeletedFalse(Long activityId, SignInStatus status);

    /**
     * 检查用户是否已签到
     */
    boolean existsByActivityIdAndUserIdAndStatusInAndDeletedFalse(
            Long activityId, Long userId, List<SignInStatus> statuses);

    /**
     * 根据活动ID导出签到记录
     */
    @Query("SELECT s FROM SignIn s WHERE s.activityId = :activityId " +
           "AND (:department IS NULL OR s.department LIKE CONCAT('%', :department, '%')) " +
           "AND (:studentNo IS NULL OR s.studentNo LIKE CONCAT('%', :studentNo, '%')) " +
           "AND s.deleted = false ORDER BY s.signInTime DESC")
    List<SignIn> findByActivityIdWithFilters(
            @Param("activityId") Long activityId,
            @Param("department") String department,
            @Param("studentNo") String studentNo);
}
