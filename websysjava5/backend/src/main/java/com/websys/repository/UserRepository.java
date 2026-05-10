package com.websys.repository;

import com.websys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户Repository接口
 * 
 * @author websys
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询
     * 
     * @param username 用户名
     * @return 用户信息
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据代理商ID查询用户列表
     * 
     * @param agentId 代理商ID
     * @return 用户列表
     */
    List<User> findByAgentId(Long agentId);

    /**
     * 根据角色类型查询用户列表
     * 
     * @param roleType 角色类型
     * @return 用户列表
     */
    List<User> findByRoleType(String roleType);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查用户名是否存在（排除指定ID）
     * 
     * @param username 用户名
     * @param id 排除的ID
     * @return 是否存在
     */
    boolean existsByUsernameAndIdNot(String username, Long id);
}
