package com.medical.registration.repository;

import com.medical.registration.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    List<Department> findByStatusOrderBySortOrderAsc(Integer status);
    
    Optional<Department> findByDeptCode(String deptCode);
}
