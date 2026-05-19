package com.pethospital.repository;

import com.pethospital.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 接诊记录数据访问层
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    
    /**
     * 根据医生ID查询接诊记录
     * 
     * @param doctorId 医生ID
     * @return 接诊记录列表
     */
    List<Consultation> findByDoctorId(Long doctorId);
    
    /**
     * 根据医生ID和日期范围查询接诊记录
     * 
     * @param doctorId 医生ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 接诊记录列表
     */
    List<Consultation> findByDoctorIdAndConsultationDateBetween(
            Long doctorId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 统计指定日期范围内的接诊数量
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 接诊数量
     */
    @Query("SELECT COUNT(c) FROM Consultation c WHERE c.consultationDate BETWEEN ?1 AND ?2 AND c.status = 1")
    Long countByDateRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 按日期分组统计接诊数量
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计结果
     */
    @Query("SELECT c.consultationDate, COUNT(c) " +
           "FROM Consultation c WHERE c.consultationDate BETWEEN ?1 AND ?2 AND c.status = 1 " +
           "GROUP BY c.consultationDate ORDER BY c.consultationDate")
    List<Object[]> countGroupByDate(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询所有疾病诊断（用于疾病排行统计）
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 诊断ID列表
     */
    @Query("SELECT c.diagnosisIds FROM Consultation c WHERE c.consultationDate BETWEEN ?1 AND ?2 AND c.status = 1 AND c.diagnosisIds IS NOT NULL")
    List<String> findAllDiagnosisIds(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询所有药品处方（用于药品使用分析）
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 药品ID列表
     */
    @Query("SELECT c.medicineIds FROM Consultation c WHERE c.consultationDate BETWEEN ?1 AND ?2 AND c.status = 1 AND c.medicineIds IS NOT NULL")
    List<String> findAllMedicineIds(LocalDateTime startTime, LocalDateTime endTime);
}
