package com.hospital.repository;

import com.hospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 医生Repository接口
 * 提供医生数据访问层的基本操作
 * 
 * @author hospital
 * @version 1.0.0
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {

    /**
     * 根据医生编号查询
     * 
     * @param doctorCode 医生编号
     * @return 医生对象
     */
    Optional<Doctor> findByDoctorCode(String doctorCode);

    /**
     * 根据科室ID查询医生列表
     * 
     * @param deptId 科室ID
     * @return 医生列表
     */
    List<Doctor> findByDeptIdAndStatusOrderByConsultationCountDesc(Long deptId, Integer status);

    /**
     * 根据用户ID查询医生
     * 
     * @param userId 用户ID
     * @return 医生对象
     */
    Optional<Doctor> findByUserId(Long userId);

    /**
     * 查询推荐医生
     * 
     * @return 推荐医生列表
     */
    List<Doctor> findByIsRecommendedAndStatusOrderByRatingDesc(Integer isRecommended, Integer status);

    /**
     * 当医生停诊时查询同科室的替代医生
     * 
     * @param deptId 科室ID
     * @param excludeDoctorId 排除的医生ID
     * @param status 状态
     * @return 替代医生列表
     */
    @Query("SELECT d FROM Doctor d WHERE d.deptId = ?1 AND d.id != ?2 AND d.status = ?3 ORDER BY d.rating DESC")
    List<Doctor> findAlternativeDoctors(Long deptId, Long excludeDoctorId, Integer status);
}
