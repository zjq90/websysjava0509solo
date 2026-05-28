package com.club.management.repository;

import com.club.management.entity.Registration;
import com.club.management.entity.enums.RegistrationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 报名记录数据访问接口
 * 
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    /**
     * 根据活动ID和用户ID查询报名记录
     */
    Optional<Registration> findByActivityIdAndUserId(Long activityId, Long userId);

    /**
     * 根据活动ID查询报名记录
     */
    List<Registration> findByActivityIdAndDeletedFalse(Long activityId);

    /**
     * 根据活动ID和状态查询报名记录
     */
    List<Registration> findByActivityIdAndStatusAndDeletedFalse(Long activityId, RegistrationStatus status);

    /**
     * 分页查询活动报名记录
     */
    Page<Registration> findByActivityIdAndDeletedFalse(Long activityId, Pageable pageable);

    /**
     * 根据用户ID查询报名记录
     */
    Page<Registration> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    /**
     * 统计活动报名人数
     */
    long countByActivityIdAndStatusAndDeletedFalse(Long activityId, RegistrationStatus status);

    /**
     * 统计活动已通过报名人数
     */
    @Query("SELECT COUNT(r) FROM Registration r WHERE r.activityId = :activityId " +
           "AND r.status IN (:statuses) AND r.deleted = false")
    long countByActivityIdAndStatusIn(
            @Param("activityId") Long activityId,
            @Param("statuses") List<RegistrationStatus> statuses);

    /**
     * 检查用户是否已报名
     */
    boolean existsByActivityIdAndUserIdAndDeletedFalse(Long activityId, Long userId);

    /**
     * 按院系筛选报名记录
     */
    @Query("SELECT r FROM Registration r WHERE r.activityId = :activityId " +
           "AND (:department IS NULL OR r.department LIKE CONCAT('%', :department, '%')) " +
           "AND (:studentNo IS NULL OR r.studentNo LIKE CONCAT('%', :studentNo, '%')) " +
           "AND r.status IN (:statuses) AND r.deleted = false")
    List<Registration> findByActivityIdWithFilters(
            @Param("activityId") Long activityId,
            @Param("department") String department,
            @Param("studentNo") String studentNo,
            @Param("statuses") List<RegistrationStatus> statuses);

    /**
     * 查询用户报名的活动ID列表
     */
    @Query("SELECT r.activityId FROM Registration r WHERE r.userId = :userId AND r.deleted = false")
    List<Long> findActivityIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据活动ID查询已通过报名的用户ID列表
     */
    @Query("SELECT r.userId FROM Registration r WHERE r.activityId = :activityId " +
           "AND r.status = :status AND r.deleted = false")
    List<Long> findUserIdsByActivityIdAndStatus(
            @Param("activityId") Long activityId,
            @Param("status") RegistrationStatus status);
}
