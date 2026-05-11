package com.hospital.repository;

import com.hospital.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 预约Repository接口
 * 提供预约数据访问层的基本操作
 * 
 * @author hospital
 * @version 1.0.0
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    /**
     * 根据预约编号查询
     * 
     * @param appointmentNo 预约编号
     * @return 预约对象
     */
    Optional<Appointment> findByAppointmentNo(String appointmentNo);

    /**
     * 查询患者的预约列表
     * 
     * @param patientId 患者ID
     * @return 预约列表
     */
    List<Appointment> findByPatientIdOrderByCreateTimeDesc(Long patientId);

    /**
     * 查询医生的预约列表
     * 
     * @param doctorId 医生ID
     * @return 预约列表
     */
    List<Appointment> findByDoctorIdOrderByStartTimeAsc(Long doctorId);

    /**
     * 查询某个时间范围内的预约
     * 
     * @param doctorId 医生ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 预约列表
     */
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = ?1 AND a.startTime >= ?2 AND a.endTime <= ?3 AND a.status NOT IN ('CANCELLED', 'NO_SHOW')")
    List<Appointment> findByDoctorAndTimeRange(Long doctorId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询需要发送就诊提醒的预约（就诊时间前1小时）
     * 
     * @param now 当前时间
     * @param reminderTime 提醒时间
     * @return 预约列表
     */
    @Query("SELECT a FROM Appointment a WHERE a.status = 'PENDING' AND a.notifiedReminder = 0 AND a.startTime BETWEEN ?1 AND ?2")
    List<Appointment> findAppointmentsForReminder(LocalDateTime now, LocalDateTime reminderTime);
}
