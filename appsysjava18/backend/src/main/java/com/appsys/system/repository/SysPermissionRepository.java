package com.appsys.system.repository;

import com.appsys.system.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 系统权限数据访问接口
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long>, JpaSpecificationExecutor<SysPermission> {

    /**
     * 根据权限编码查询
     */
    Optional<SysPermission> findByPermissionCode(String permissionCode);

    /**
     * 检查权限编码是否存在
     */
    boolean existsByPermissionCode(String permissionCode);
}
