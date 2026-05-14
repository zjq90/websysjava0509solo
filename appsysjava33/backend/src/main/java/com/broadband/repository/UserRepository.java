package com.broadband.repository;

import com.broadband.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 * 
 * @author broadband
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    Optional<User> findByWxOpenId(String wxOpenId);

    Optional<User> findByAlipayUserId(String alipayUserId);

    boolean existsByPhone(String phone);
}
