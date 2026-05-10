package com.appsys.system.repository;

import com.appsys.system.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 系统用户数据访问接口
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

    /**
     * 根据用户名查询用户（排除已删除）
     */
    Optional<SysUser> findByUsernameAndDeletedFalse(String username);

    /**
     * 根据用户名查询用户
     */
    Optional<SysUser> findByUsername(String username);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 根据ID和删除状态查询用户
     */
    Optional<SysUser> findByIdAndDeletedFalse(Long id);
}
