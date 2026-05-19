package com.pethospital.repository;

import com.pethospital.entity.MedicationReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用药提醒数据访问接口
 * 
 * @author Pet Hospital Team
 */
@Repository
public interface MedicationReminderRepository extends JpaRepository<MedicationReminder, Long> {

    List<MedicationReminder> findByUserIdOrderByReminderTimeAsc(Long userId);
}
