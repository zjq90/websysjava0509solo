package com.websys.repository;

import com.websys.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色数据访问接口
 * 提供角色的增删改查操作
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    /**
     * 根据角色编码查找角色
     * @param code 角色编码
     * @return 角色对象
     */
    Optional<Role> findByCode(String code);

    /**
     * 判断角色编码是否存在
     * @param code 角色编码
     * @return 是否存在
     */
    boolean existsByCode(String code);

    /**
     * 根据角色名称查找角色
     * @param name 角色名称
     * @return 角色对象
     */
    Optional<Role> findByName(String name);
}
