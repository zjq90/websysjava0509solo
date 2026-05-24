package com.bikeshare.repository;

import com.bikeshare.entity.UserBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户黑名单数据访问层
 *
 * @author BikeShare Team
 * @version 1.0.0
 */
@Repository
public interface UserBlacklistRepository extends JpaRepository<UserBlacklist, Long> {

    Optional<UserBlacklist> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
