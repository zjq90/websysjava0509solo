package com.hospital.repository;

import com.hospital.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 系统用户数据访问层
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

    /**
     * 根据用户名查询用户
     */
    Optional<SysUser> findByUsername(String username);

    /**
     * 判断用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 判断用户名是否存在（排除指定ID）
     */
    boolean existsByUsernameAndIdNot(String username, Long id);
}
