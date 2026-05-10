package com.appsys.system.repository;

import com.appsys.system.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 系统角色数据访问接口
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

    /**
     * 根据角色编码查询
     */
    Optional<SysRole> findByRoleCode(String roleCode);

    /**
     * 根据角色编码查询（排除已删除）
     */
    Optional<SysRole> findByRoleCodeAndDeletedFalse(String roleCode);

    /**
     * 检查角色编码是否存在
     */
    boolean existsByRoleCode(String roleCode);
}
