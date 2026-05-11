package com.medical.appointment.repository;

import com.medical.appointment.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    List<Department> findByParentIsNullOrderBySortOrderAsc();
    
    List<Department> findByParent_IdOrderBySortOrderAsc(Long parentId);
    
    List<Department> findByIsActiveTrueOrderBySortOrderAsc();
    
    @Query("SELECT d FROM Department d WHERE d.isActive = true AND (d.name LIKE %:keyword% OR d.description LIKE %:keyword%)")
    List<Department> searchByKeyword(String keyword);
    
    Optional<Department> findByName(String name);
}
