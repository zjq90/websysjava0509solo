package com.bikeshare.repository;

import com.bikeshare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据访问层
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIsBlacklistedTrue();

    List<User> findByUserLevel(String userLevel);

    List<User> findByRideCountGreaterThanEqual(Integer rideCount);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createTime >= :startTime")
    Long countNewUsers(LocalDateTime startTime);

    @Query("SELECT u FROM User u WHERE u.rideCount >= :minRides AND u.isVip = false AND u.isBlacklisted = false")
    List<User> findHighFrequencyUsers(Integer minRides);
}
