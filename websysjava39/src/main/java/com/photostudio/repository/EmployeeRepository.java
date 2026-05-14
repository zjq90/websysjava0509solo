package com.photostudio.repository;

import com.photostudio.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 员工Repository
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    List<Employee> findByPositionAndActive(String position, Boolean active);
    
    List<Employee> findByStoreIdAndPositionAndActive(Long storeId, String position, Boolean active);
    
    List<Employee> findByActive(Boolean active);
}
