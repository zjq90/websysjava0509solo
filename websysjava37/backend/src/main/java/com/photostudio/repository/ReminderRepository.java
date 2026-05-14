package com.photostudio.repository;

import com.photostudio.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒数据访问层
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long>, JpaSpecificationExecutor<Reminder> {

    /**
     * 根据客户ID查询提醒
     */
    List<Reminder> findByCustomerIdAndDeletedFalseOrderByReminderTimeDesc(Long customerId);

    /**
     * 查询待处理的提醒
     */
    List<Reminder> findByProcessedFalseAndDeletedFalseAndReminderTimeBeforeOrderByReminderTimeAsc(LocalDateTime time);

    /**
     * 查询所有未删除的提醒
     */
    List<Reminder> findAllByDeletedFalse();
}
