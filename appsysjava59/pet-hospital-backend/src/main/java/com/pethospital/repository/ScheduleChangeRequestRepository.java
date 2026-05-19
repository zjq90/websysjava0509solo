package com.pethospital.repository;

import com.pethospital.entity.ScheduleChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleChangeRequestRepository extends JpaRepository<ScheduleChangeRequest, Long> {
    List<ScheduleChangeRequest> findByDoctorId(Long doctorId);
    List<ScheduleChangeRequest> findByStatus(String status);
}
