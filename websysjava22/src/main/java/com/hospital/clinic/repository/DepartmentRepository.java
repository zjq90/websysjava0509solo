package com.hospital.clinic.repository;

import com.hospital.clinic.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 科室Repository
 * 科室数据访问接口
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * 根据科室编码查询
     */
    Department findByDeptCode(String deptCode);

    /**
     * 根据科室名称模糊查询
     */
    List<Department> findByDeptNameContaining(String deptName);

    /**
     * 根据状态查询
     */
    List<Department> findByStatus(Integer status);
}
