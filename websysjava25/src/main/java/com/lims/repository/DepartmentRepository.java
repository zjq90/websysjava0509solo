package com.lims.repository;

import com.lims.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 科室数据访问层
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * 根据科室编号查询
     */
    Department findByDeptCode(String deptCode);

    /**
     * 根据科室类型查询
     */
    List<Department> findByDeptType(String deptType);

    /**
     * 根据状态查询
     */
    List<Department> findByStatus(String status);

    /**
     * 根据科室类型和状态查询
     */
    List<Department> findByDeptTypeAndStatus(String deptType, String status);
}
