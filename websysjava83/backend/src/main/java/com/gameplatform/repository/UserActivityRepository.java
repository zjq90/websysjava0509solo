package com.gameplatform.repository;

import com.gameplatform.entity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    @Query("SELECT COUNT(DISTINCT ua.userId) FROM UserActivity ua WHERE ua.activityTime >= :start AND ua.activityTime <= :end")
    Long countDistinctUsersBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query(value = "SELECT CAST(activity_time AS DATE) as dt, COUNT(DISTINCT user_id) as cnt " +
           "FROM user_activities WHERE activity_time >= :start AND activity_time <= :end " +
           "GROUP BY CAST(activity_time AS DATE) ORDER BY dt", nativeQuery = true)
    List<Object[]> countDailyActiveUsers(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
