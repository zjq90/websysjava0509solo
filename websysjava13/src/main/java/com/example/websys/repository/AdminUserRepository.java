package com.example.websys.repository;

import com.example.websys.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 管理员用户数据访问接口
 * 提供管理员用户的增删改查功能
 */
@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByUsername(String username);

    Optional<AdminUser> findByUsernameAndPassword(String username, String password);
}
