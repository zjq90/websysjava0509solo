package com.agriculture.repository;

import com.agriculture.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 * 提供用户表的基本CRUD操作
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    Optional<User> findByUsername(String username);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 根据用户名和密码查找用户（简化登录）
     * 
     * @param username 用户名
     * @param password 密码
     * @return 用户对象
     */
    Optional<User> findByUsernameAndPassword(String username, String password);
}
