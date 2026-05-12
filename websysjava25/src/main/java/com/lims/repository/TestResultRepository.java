package com.lims.repository;

import com.lims.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 检验检查结果数据访问层
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    /**
     * 根据申请ID查询
     */
    List<TestResult> findByApplicationId(Long applicationId);

    /**
     * 根据技师ID查询
     */
    List<TestResult> findByTechnicianId(Long technicianId);

    /**
     * 根据项目ID查询
     */
    List<TestResult> findByItemId(Long itemId);

    /**
     * 根据设备编号查询
     */
    List<TestResult> findByDeviceCode(String deviceCode);
}
