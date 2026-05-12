package com.hospital.repository;

import com.hospital.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医护人员Repository
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Staff findByStaffNo(String staffNo);

    List<Staff> findByNameContaining(String name);

    List<Staff> findByRole(String role);

    List<Staff> findByDepartment(String department);

    List<Staff> findByStatus(String status);

    @Query("SELECT s FROM Staff s WHERE s.role = '医生' AND s.status = '在职'")
    List<Staff> findAllDoctors();

    @Query("SELECT s FROM Staff s WHERE s.role = '护士' AND s.status = '在职'")
    List<Staff> findAllNurses();
}
