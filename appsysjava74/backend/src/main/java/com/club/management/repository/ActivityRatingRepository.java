package com.club.management.repository;

import com.club.management.entity.ActivityRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 活动评分数据访问接口
 * 
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ActivityRatingRepository extends JpaRepository<ActivityRating, Long> {

    /**
     * 根据活动ID和用户ID查询评分
     */
    Optional<ActivityRating> findByActivityIdAndUserIdAndDeletedFalse(Long activityId, Long userId);

    /**
     * 根据活动ID分页查询评分
     */
    Page<ActivityRating> findByActivityIdAndVisibleAndDeletedFalse(
            Long activityId, Boolean visible, Pageable pageable);

    /**
     * 根据活动ID查询所有评分
     */
    List<ActivityRating> findByActivityIdAndDeletedFalse(Long activityId);

    /**
     * 检查用户是否已评分
     */
    boolean existsByActivityIdAndUserIdAndDeletedFalse(Long activityId, Long userId);

    /**
     * 计算活动平均评分
     */
    @Query("SELECT AVG(r.rating) FROM ActivityRating r WHERE r.activityId = :activityId " +
           "AND r.visible = true AND r.deleted = false")
    Double calculateAverageRating(@Param("activityId") Long activityId);

    /**
     * 统计活动评分人数
     */
    @Query("SELECT COUNT(r) FROM ActivityRating r WHERE r.activityId = :activityId " +
           "AND r.visible = true AND r.deleted = false")
    long countByActivityIdAndVisible(@Param("activityId") Long activityId);
}
