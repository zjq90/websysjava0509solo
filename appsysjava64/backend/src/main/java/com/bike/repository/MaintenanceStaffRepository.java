package com.bike.repository;

import com.bike.entity.MaintenanceStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 运维人员数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface MaintenanceStaffRepository extends JpaRepository<MaintenanceStaff, Long>, JpaSpecificationExecutor<MaintenanceStaff> {

    Optional<MaintenanceStaff> findByStaffNo(String staffNo);

    Optional<MaintenanceStaff> findByPhone(String phone);

    List<MaintenanceStaff> findByStatus(String status);

    List<MaintenanceStaff> findByWorkArea(String workArea);
}
