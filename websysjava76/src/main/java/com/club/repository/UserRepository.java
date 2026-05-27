package com.club.repository;

import com.club.entity.User;
import com.club.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据角色类型查询用户列表
     */
    List<User> findByRoleType(RoleType roleType);

    /**
     * 根据院系查询用户列表
     */
    List<User> findByDepartment(String department);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
}
