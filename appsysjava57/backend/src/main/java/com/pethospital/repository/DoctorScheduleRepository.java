package com.pethospital.repository;

import com.pethospital.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 医生排班数据访问接口
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findByDoctorIdOrderByScheduleDateAsc(Long doctorId);
}
