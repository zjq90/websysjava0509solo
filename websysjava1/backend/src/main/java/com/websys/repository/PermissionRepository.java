package com.websys.repository;

import com.websys.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 权限数据访问接口
 * 提供权限的增删改查操作
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {

    /**
     * 根据权限编码查找权限
     * @param code 权限编码
     * @return 权限对象
     */
    Optional<Permission> findByCode(String code);

    /**
     * 判断权限编码是否存在
     * @param code 权限编码
     * @return 是否存在
     */
    boolean existsByCode(String code);
}
