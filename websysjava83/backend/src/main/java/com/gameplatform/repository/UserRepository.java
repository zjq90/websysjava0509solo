package com.gameplatform.repository;

import com.gameplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(DISTINCT u.id) FROM User u WHERE u.lastLoginAt >= :dateTime")
    Long countActiveUsersSince(@Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :start AND u.createdAt <= :end")
    Long countNewUsersBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT u.region, COUNT(u) as cnt FROM User u GROUP BY u.region ORDER BY cnt DESC")
    List<Object[]> countUsersByRegion();
}
