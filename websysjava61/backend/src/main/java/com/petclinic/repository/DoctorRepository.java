package com.petclinic.repository;

import com.petclinic.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医生Repository
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /**
     * 根据科室ID查询医生列表
     */
    List<Doctor> findByDepartmentId(Long departmentId);

    /**
     * 根据审核状态查询医生列表
     */
    List<Doctor> findByAuditStatus(Integer auditStatus);

    /**
     * 根据状态查询医生列表
     */
    List<Doctor> findByStatus(Integer status);
}
