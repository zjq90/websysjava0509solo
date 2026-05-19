package com.pethospital.repository;

import com.pethospital.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 医生数据访问层
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    /**
     * 根据手机号查询医生
     * 
     * @param phone 手机号
     * @return 医生信息
     */
    Optional<Doctor> findByPhone(String phone);
    
    /**
     * 根据手机号和状态查询医生
     * 
     * @param phone 手机号
     * @param status 状态
     * @return 医生信息
     */
    Optional<Doctor> findByPhoneAndStatus(String phone, Integer status);
}
