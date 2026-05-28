package com.club.repository;

import com.club.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户Repository接口
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsernameAndDeletedFalse(String username);

    Optional<User> findByPhoneAndDeletedFalse(String phone);

    boolean existsByUsernameAndDeletedFalse(String username);

    boolean existsByPhoneAndDeletedFalse(String phone);
}
