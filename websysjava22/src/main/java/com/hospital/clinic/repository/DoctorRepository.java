package com.hospital.clinic.repository;

import com.hospital.clinic.entity.Department;
import com.hospital.clinic.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医生Repository
 * 医生数据访问接口
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /**
     * 根据医生工号查询
     */
    Doctor findByDoctorNo(String doctorNo);

    /**
     * 根据姓名模糊查询
     */
    List<Doctor> findByNameContaining(String name);

    /**
     * 根据科室查询
     */
    List<Doctor> findByDepartment(Department department);

    /**
     * 根据科室和状态查询
     */
    List<Doctor> findByDepartmentAndStatus(Department department, Integer status);

    /**
     * 根据状态查询
     */
    List<Doctor> findByStatus(Integer status);
}
