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
    List<User> findByEnabled(Boolean enabled);

    List<User> findByMemberLevel(Integer memberLevel);

    @Query("SELECT u FROM User u WHERE u.memberLevel >= :memberLevel")
    List<User> findByMemberLevelGreaterThanEqual(@Param("memberLevel") Integer memberLevel);

    @Query("SELECT u FROM User u WHERE u.lastActiveTime >= :activeSince")
    List<User> findByLastActiveTimeAfter(@Param("activeSince") LocalDateTime activeSince);

    @Query("SELECT u FROM User u WHERE u.memberLevel >= :memberLevel AND u.lastActiveTime >= :activeSince")
    List<User> findByMemberLevelAndLastActiveTime(
            @Param("memberLevel") Integer memberLevel,
            @Param("activeSince") LocalDateTime activeSince);
}
