package com.petclinic.repository;

import com.petclinic.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 科室Repository
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * 根据状态查询科室列表
     */
    List<Department> findByStatus(Integer status);

    /**
     * 根据编码查询科室
     */
    Department findByCode(String code);
}
