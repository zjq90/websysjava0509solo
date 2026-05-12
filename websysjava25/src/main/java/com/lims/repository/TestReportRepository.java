package com.lims.repository;

import com.lims.entity.TestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 检验检查报告数据访问层
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Repository
public interface TestReportRepository extends JpaRepository<TestReport, Long> {

    /**
     * 根据报告编号查询
     */
    TestReport findByReportNo(String reportNo);

    /**
     * 根据申请ID查询
     */
    List<TestReport> findByApplicationId(Long applicationId);

    /**
     * 根据状态查询
     */
    List<TestReport> findByStatus(String status);

    /**
     * 根据一级审核人ID查询
     */
    List<TestReport> findByFirstAuditorId(Long firstAuditorId);

    /**
     * 根据二级审核人ID查询
     */
    List<TestReport> findBySecondAuditorId(Long secondAuditorId);

    /**
     * 根据报告生成人ID查询
     */
    List<TestReport> findByCreatedBy(Long createdBy);
}
