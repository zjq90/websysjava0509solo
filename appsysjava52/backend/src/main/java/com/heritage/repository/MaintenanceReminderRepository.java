package com.heritage.repository;

import com.heritage.entity.MaintenanceReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 保养提醒Repository
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@Repository
public interface MaintenanceReminderRepository extends JpaRepository<MaintenanceReminder, Long> {

    /**
     * 根据收藏项ID查询保养提醒
     */
    List<MaintenanceReminder> findByCollectionItemIdOrderByScheduledDateDesc(Long collectionItemId);

    /**
     * 查询指定日期需要提醒的保养
     */
    List<MaintenanceReminder> findByRemindDateAndStatus(LocalDate remindDate, Integer status);

    /**
     * 根据状态查询
     */
    List<MaintenanceReminder> findByCollectionItemIdAndStatus(Long collectionItemId, Integer status);
}