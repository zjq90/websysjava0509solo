package com.heritage.repository;

import com.heritage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户Repository
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询
     */
    Optional<User> findByUsernameAndDeleted(String username, Integer deleted);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsernameAndDeleted(String username, Integer deleted);
}