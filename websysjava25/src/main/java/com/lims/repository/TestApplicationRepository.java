package com.lims.repository;

import com.lims.entity.TestApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 检验检查申请数据访问层
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Repository
public interface TestApplicationRepository extends JpaRepository<TestApplication, Long> {

    /**
     * 根据申请编号查询
     */
    TestApplication findByApplicationNo(String applicationNo);

    /**
     * 根据状态查询
     */
    List<TestApplication> findByStatus(String status);

    /**
     * 根据申请科室ID查询
     */
    List<TestApplication> findByApplyDepartmentId(Long applyDepartmentId);

    /**
     * 根据执行科室ID查询
     */
    List<TestApplication> findByExecuteDepartmentId(Long executeDepartmentId);

    /**
     * 根据执行科室ID和状态查询
     */
    List<TestApplication> findByExecuteDepartmentIdAndStatus(Long executeDepartmentId, String status);

    /**
     * 根据患者ID查询
     */
    List<TestApplication> findByPatientId(Long patientId);

    /**
     * 根据分配的技师ID查询
     */
    List<TestApplication> findByAssignedTechnicianId(Long assignedTechnicianId);

    /**
     * 根据分配的技师ID和状态查询
     */
    List<TestApplication> findByAssignedTechnicianIdAndStatus(Long assignedTechnicianId, String status);
}
