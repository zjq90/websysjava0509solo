package com.hospital.management.service;

import com.hospital.management.entity.MedicalQuality;
import com.hospital.management.repository.MedicalQualityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 医疗质量服务层
 *
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Service
@Transactional
public class MedicalQualityService {

    @Autowired
    private MedicalQualityRepository medicalQualityRepository;

    /**
     * 查询所有医疗质量数据
     */
    public List<MedicalQuality> findAll() {
        return medicalQualityRepository.findAll();
    }

    /**
     * 根据ID查询医疗质量数据
     */
    public Optional<MedicalQuality> findById(Long id) {
        return medicalQualityRepository.findById(id);
    }

    /**
     * 新增医疗质量数据
     */
    public MedicalQuality save(MedicalQuality medicalQuality) {
        return medicalQualityRepository.save(medicalQuality);
    }

    /**
     * 更新医疗质量数据
     */
    public MedicalQuality update(MedicalQuality medicalQuality) {
        return medicalQualityRepository.save(medicalQuality);
    }

    /**
     * 删除医疗质量数据
     */
    public void deleteById(Long id) {
        medicalQualityRepository.deleteById(id);
    }

    /**
     * 根据日期范围查询医疗质量数据
     */
    public List<MedicalQuality> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return medicalQualityRepository.findByStatDateBetween(startDate, endDate);
    }

    /**
     * 根据科室ID和日期范围查询医疗质量数据
     */
    public List<MedicalQuality> findByDepartmentAndDateRange(Long departmentId, LocalDate startDate, LocalDate endDate) {
        return medicalQualityRepository.findByDepartmentIdAndStatDateBetween(departmentId, startDate, endDate);
    }

    /**
     * 获取医疗质量统计汇总
     */
    public Map<String, Object> getStatisticsSummary(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> summary = new HashMap<>();
        
        Double avgRecordQualificationRate = medicalQualityRepository.avgRecordQualificationRateByDateRange(startDate, endDate);
        Double avgRationalDrugUseRate = medicalQualityRepository.avgRationalDrugUseRateByDateRange(startDate, endDate);
        Double avgNosocomialInfectionRate = medicalQualityRepository.avgNosocomialInfectionRateByDateRange(startDate, endDate);
        Integer totalAdverseEvents = medicalQualityRepository.sumAdverseEventCountByDateRange(startDate, endDate);
        
        summary.put("avgRecordQualificationRate", avgRecordQualificationRate != null ? String.format("%.2f", avgRecordQualificationRate) : "0.00");
        summary.put("avgRationalDrugUseRate", avgRationalDrugUseRate != null ? String.format("%.2f", avgRationalDrugUseRate) : "0.00");
        summary.put("avgNosocomialInfectionRate", avgNosocomialInfectionRate != null ? String.format("%.2f", avgNosocomialInfectionRate) : "0.00");
        summary.put("totalAdverseEvents", totalAdverseEvents != null ? totalAdverseEvents : 0);
        
        return summary;
    }

    /**
     * 按科室分组统计
     */
    public List<Object[]> aggregateByDepartment(LocalDate startDate, LocalDate endDate) {
        return medicalQualityRepository.aggregateByDepartment(startDate, endDate);
    }
}
