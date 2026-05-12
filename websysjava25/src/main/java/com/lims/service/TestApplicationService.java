package com.lims.service;

import com.lims.entity.TestApplication;
import com.lims.repository.TestApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 检验检查申请业务逻辑层
 * 实现申请管理、任务确认与分配功能
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Service
public class TestApplicationService {

    @Autowired
    private TestApplicationRepository testApplicationRepository;

    /**
     * 查询所有申请
     */
    public List<TestApplication> findAll() {
        return testApplicationRepository.findAll();
    }

    /**
     * 根据ID查询申请
     */
    public Optional<TestApplication> findById(Long id) {
        return testApplicationRepository.findById(id);
    }

    /**
     * 根据申请编号查询
     */
    public TestApplication findByApplicationNo(String applicationNo) {
        return testApplicationRepository.findByApplicationNo(applicationNo);
    }

    /**
     * 根据状态查询
     */
    public List<TestApplication> findByStatus(String status) {
        return testApplicationRepository.findByStatus(status);
    }

    /**
     * 根据执行科室ID查询
     */
    public List<TestApplication> findByExecuteDepartmentId(Long executeDepartmentId) {
        return testApplicationRepository.findByExecuteDepartmentId(executeDepartmentId);
    }

    /**
     * 根据执行科室ID和状态查询
     */
    public List<TestApplication> findByExecuteDepartmentIdAndStatus(Long executeDepartmentId, String status) {
        return testApplicationRepository.findByExecuteDepartmentIdAndStatus(executeDepartmentId, status);
    }

    /**
     * 根据分配的技师ID查询
     */
    public List<TestApplication> findByAssignedTechnicianId(Long assignedTechnicianId) {
        return testApplicationRepository.findByAssignedTechnicianId(assignedTechnicianId);
    }

    /**
     * 根据分配的技师ID和状态查询
     */
    public List<TestApplication> findByAssignedTechnicianIdAndStatus(Long assignedTechnicianId, String status) {
        return testApplicationRepository.findByAssignedTechnicianIdAndStatus(assignedTechnicianId, status);
    }

    /**
     * 根据患者ID查询
     */
    public List<TestApplication> findByPatientId(Long patientId) {
        return testApplicationRepository.findByPatientId(patientId);
    }

    /**
     * 新增申请
     */
    public TestApplication save(TestApplication testApplication) {
        return testApplicationRepository.save(testApplication);
    }

    /**
     * 更新申请
     */
    public TestApplication update(TestApplication testApplication) {
        return testApplicationRepository.save(testApplication);
    }

    /**
     * 确认申请
     */
    public TestApplication confirmApplication(Long id, Long confirmedBy) {
        Optional<TestApplication> optional = testApplicationRepository.findById(id);
        if (optional.isPresent()) {
            TestApplication application = optional.get();
            application.setStatus("CONFIRMED");
            application.setConfirmedBy(confirmedBy);
            application.setConfirmedTime(LocalDateTime.now());
            return testApplicationRepository.save(application);
        }
        return null;
    }

    /**
     * 分配任务给技师
     */
    public TestApplication assignTechnician(Long id, Long technicianId) {
        Optional<TestApplication> optional = testApplicationRepository.findById(id);
        if (optional.isPresent()) {
            TestApplication application = optional.get();
            application.setAssignedTechnicianId(technicianId);
            application.setStatus("ASSIGNED");
            return testApplicationRepository.save(application);
        }
        return null;
    }

    /**
     * 开始处理
     */
    public TestApplication startProcessing(Long id) {
        Optional<TestApplication> optional = testApplicationRepository.findById(id);
        if (optional.isPresent()) {
            TestApplication application = optional.get();
            application.setStatus("PROCESSING");
            return testApplicationRepository.save(application);
        }
        return null;
    }

    /**
     * 完成检验/检查，待出报告
     */
    public TestApplication completeTest(Long id) {
        Optional<TestApplication> optional = testApplicationRepository.findById(id);
        if (optional.isPresent()) {
            TestApplication application = optional.get();
            application.setStatus("REPORTED");
            return testApplicationRepository.save(application);
        }
        return null;
    }

    /**
     * 删除申请
     */
    public void deleteById(Long id) {
        testApplicationRepository.deleteById(id);
    }
}
