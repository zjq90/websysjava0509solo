package com.heritage.repository;

import com.heritage.entity.UserBehaviorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBehaviorLogRepository extends JpaRepository<UserBehaviorLog, Long>, JpaSpecificationExecutor<UserBehaviorLog> {

    List<UserBehaviorLog> findByUserId(Long userId);

    List<UserBehaviorLog> findByBehaviorType(String behaviorType);

    List<UserBehaviorLog> findByTargetTypeAndTargetId(String targetType, Long targetId);
}
