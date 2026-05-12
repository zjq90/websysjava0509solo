package com.lims.repository;

import com.lims.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据访问层
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查询
     */
    User findByUsername(String username);

    /**
     * 根据角色查询
     */
    List<User> findByRole(String role);

    /**
     * 根据科室ID查询
     */
    List<User> findByDepartmentId(Long departmentId);

    /**
     * 根据角色和状态查询
     */
    List<User> findByRoleAndStatus(String role, String status);

    /**
     * 根据科室ID和角色查询
     */
    List<User> findByDepartmentIdAndRole(Long departmentId, String role);
}
