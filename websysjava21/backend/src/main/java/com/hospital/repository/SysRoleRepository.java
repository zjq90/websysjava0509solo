package com.hospital.repository;

import com.hospital.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 系统角色数据访问层
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

    /**
     * 根据角色编码查询角色
     */
    Optional<SysRole> findByRoleCode(String roleCode);

    /**
     * 判断角色编码是否存在
     */
    boolean existsByRoleCode(String roleCode);

    /**
     * 判断角色编码是否存在（排除指定ID）
     */
    boolean existsByRoleCodeAndIdNot(String roleCode, Long id);
}
