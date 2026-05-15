package com.referee.repository;

import com.referee.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户数据访问接口
 *
 * @author Referee System
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据角色查询用户列表
     *
     * @param role 角色
     * @return 用户列表
     */
    List<User> findByRole(String role);

    /**
     * 根据状态查询用户列表
     *
     * @param status 状态
     * @return 用户列表
     */
    List<User> findByStatus(Integer status);

    /**
     * 根据角色和状态查询用户列表
     *
     * @param role   角色
     * @param status 状态
     * @return 用户列表
     */
    List<User> findByRoleAndStatus(String role, Integer status);

    /**
     * 根据裁判ID查询用户
     *
     * @param refereeId 裁判ID
     * @return 用户信息
     */
    Optional<User> findByRefereeId(Long refereeId);

    /**
     * 根据运动员ID查询用户
     *
     * @param athleteId 运动员ID
     * @return 用户信息
     */
    Optional<User> findByAthleteId(Long athleteId);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 模糊查询用户名或真实姓名
     *
     * @param keyword 关键词
     * @return 用户列表
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE %?1% OR u.realName LIKE %?1%")
    List<User> findByKeyword(String keyword);
}
