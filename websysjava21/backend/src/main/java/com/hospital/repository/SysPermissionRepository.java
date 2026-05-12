package com.hospital.repository;

import com.hospital.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 系统权限数据访问层
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long>, JpaSpecificationExecutor<SysPermission> {

    /**
     * 根据权限编码查询权限
     */
    Optional<SysPermission> findByPermissionCode(String permissionCode);

    /**
     * 根据父权限ID查询子权限
     */
    List<SysPermission> findByParentIdOrderBySortOrder(Long parentId);

    /**
     * 判断权限编码是否存在
     */
    boolean existsByPermissionCode(String permissionCode);
}
