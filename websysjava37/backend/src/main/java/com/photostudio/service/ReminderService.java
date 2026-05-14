package com.photostudio.service;

import com.photostudio.entity.Reminder;
import com.photostudio.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 提醒服务层
 * 处理提醒相关的业务逻辑，包括定时任务
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
@Transactional
public class ReminderService {

    @Autowired
    private ReminderRepository reminderRepository;

    /**
     * 创建提醒
     */
    public Reminder createReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    /**
     * 更新提醒
     */
    public Reminder updateReminder(Long id, Reminder reminderDetails) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("提醒不存在"));

        reminder.setTitle(reminderDetails.getTitle());
        reminder.setContent(reminderDetails.getContent());
        reminder.setReminderTime(reminderDetails.getReminderTime());
        reminder.setType(reminderDetails.getType());
        reminder.setProcessed(reminderDetails.getProcessed());

        return reminderRepository.save(reminder);
    }

    /**
     * 删除提醒（软删除）
     */
    public void deleteReminder(Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("提醒不存在"));
        reminder.setDeleted(true);
        reminderRepository.save(reminder);
    }

    /**
     * 标记提醒为已处理
     */
    public Reminder markAsProcessed(Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("提醒不存在"));
        reminder.setProcessed(true);
        return reminderRepository.save(reminder);
    }

    /**
     * 根据ID查询提醒
     */
    @Transactional(readOnly = true)
    public Optional<Reminder> getReminderById(Long id) {
        return reminderRepository.findById(id).filter(r -> !r.getDeleted());
    }

    /**
     * 查询客户的所有提醒
     */
    @Transactional(readOnly = true)
    public List<Reminder> getRemindersByCustomerId(Long customerId) {
        return reminderRepository.findByCustomerIdAndDeletedFalseOrderByReminderTimeDesc(customerId);
    }

    /**
     * 查询所有待处理的提醒
     */
    @Transactional(readOnly = true)
    public List<Reminder> getPendingReminders() {
        return reminderRepository.findByProcessedFalseAndDeletedFalseAndReminderTimeBeforeOrderByReminderTimeAsc(LocalDateTime.now());
    }

    /**
     * 查询所有提醒
     */
    @Transactional(readOnly = true)
    public List<Reminder> getAllReminders() {
        return reminderRepository.findAllByDeletedFalse();
    }

    /**
     * 定时任务：每分钟检查待处理的提醒
     */
    @Scheduled(fixedRate = 60000)
    @Transactional(readOnly = true)
    public void checkReminders() {
        List<Reminder> pendingReminders = getPendingReminders();
        if (!pendingReminders.isEmpty()) {
            System.out.println("发现 " + pendingReminders.size() + " 条待处理的提醒");
            for (Reminder reminder : pendingReminders) {
                System.out.println("提醒: " + reminder.getTitle() + " - " + reminder.getContent());
            }
        }
    }
}
