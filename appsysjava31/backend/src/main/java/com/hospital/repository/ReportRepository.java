package com.hospital.repository;

import com.hospital.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 报告Repository接口
 * 提供报告数据访问层的基本操作
 * 
 * @author hospital
 * @version 1.0.0
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {

    /**
     * 根据报告编号查询
     * 
     * @param reportNo 报告编号
     * @return 报告对象
     */
    Optional<Report> findByReportNo(String reportNo);

    /**
     * 查询患者的报告列表
     * 
     * @param patientId 患者ID
     * @return 报告列表
     */
    List<Report> findByPatientIdOrderByReportDateDesc(Long patientId);

    /**
     * 按类型查询患者报告
     * 
     * @param patientId 患者ID
     * @param reportType 报告类型
     * @return 报告列表
     */
    List<Report> findByPatientIdAndReportTypeOrderByReportDateDesc(Long patientId, String reportType);

    /**
     * 查询待通知的报告
     * 
     * @param status 状态
     * @param notified 是否已通知
     * @return 报告列表
     */
    List<Report> findByStatusAndNotified(String status, Integer notified);
}
