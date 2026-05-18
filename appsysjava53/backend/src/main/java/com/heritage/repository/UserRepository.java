package com.heritage.repository;

import com.heritage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByPhone(String phone);

    List<User> findByUserType(Integer userType);

    @Query("SELECT u FROM User u WHERE u.userType = 2 AND u.status = 1")
    List<User> findAllExperts();

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);
}
