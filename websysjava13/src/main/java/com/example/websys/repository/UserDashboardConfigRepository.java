package com.example.websys.repository;

import com.example.websys.entity.UserDashboardConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户看板配置数据访问接口
 * 提供用户看板配置的增删改查功能
 */
@Repository
public interface UserDashboardConfigRepository extends JpaRepository<UserDashboardConfig, Long> {

    List<UserDashboardConfig> findByUserIdOrderBySortOrderAsc(Long userId);

    List<UserDashboardConfig> findByUserIdAndIsDisplayTrueOrderBySortOrderAsc(Long userId);

    Optional<UserDashboardConfig> findByUserIdAndStatItemId(Long userId, Long statItemId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserDashboardConfig u WHERE u.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserDashboardConfig u SET u.isDisplay = :isDisplay WHERE u.userId = :userId AND u.statItemId = :statItemId")
    int updateDisplayStatus(@Param("userId") Long userId,
                            @Param("statItemId") Long statItemId,
                            @Param("isDisplay") Boolean isDisplay);
}
