package com.photostudio.controller;

import com.photostudio.entity.Reminder;
import com.photostudio.service.ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 提醒管理控制器
 * 提供提醒相关的REST API
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/reminders")
@Tag(name = "提醒管理", description = "客户提醒的增删改查")
@CrossOrigin(origins = "*")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    /**
     * 创建提醒
     */
    @PostMapping
    @Operation(summary = "创建提醒", description = "创建新的客户提醒")
    public ResponseEntity<Reminder> createReminder(@Valid @RequestBody Reminder reminder) {
        Reminder createdReminder = reminderService.createReminder(reminder);
        return ResponseEntity.ok(createdReminder);
    }

    /**
     * 更新提醒
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新提醒", description = "根据ID更新提醒信息")
    public ResponseEntity<Reminder> updateReminder(
            @Parameter(description = "提醒ID") @PathVariable Long id,
            @Valid @RequestBody Reminder reminderDetails) {
        Reminder updatedReminder = reminderService.updateReminder(id, reminderDetails);
        return ResponseEntity.ok(updatedReminder);
    }

    /**
     * 删除提醒
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除提醒", description = "根据ID删除提醒")
    public ResponseEntity<Void> deleteReminder(
            @Parameter(description = "提醒ID") @PathVariable Long id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 标记提醒为已处理
     */
    @PatchMapping("/{id}/processed")
    @Operation(summary = "标记已处理", description = "标记提醒为已处理状态")
    public ResponseEntity<Reminder> markAsProcessed(
            @Parameter(description = "提醒ID") @PathVariable Long id) {
        Reminder reminder = reminderService.markAsProcessed(id);
        return ResponseEntity.ok(reminder);
    }

    /**
     * 根据ID查询提醒
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询提醒", description = "根据ID查询提醒信息")
    public ResponseEntity<Reminder> getReminderById(
            @Parameter(description = "提醒ID") @PathVariable Long id) {
        return reminderService.getReminderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 查询客户的所有提醒
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "查询客户提醒", description = "根据客户ID查询所有提醒")
    public ResponseEntity<List<Reminder>> getRemindersByCustomerId(
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        List<Reminder> reminders = reminderService.getRemindersByCustomerId(customerId);
        return ResponseEntity.ok(reminders);
    }

    /**
     * 查询所有待处理的提醒
     */
    @GetMapping("/pending")
    @Operation(summary = "查询待处理提醒", description = "获取所有待处理的提醒列表")
    public ResponseEntity<List<Reminder>> getPendingReminders() {
        List<Reminder> reminders = reminderService.getPendingReminders();
        return ResponseEntity.ok(reminders);
    }

    /**
     * 查询所有提醒
     */
    @GetMapping
    @Operation(summary = "查询所有提醒", description = "获取所有提醒列表")
    public ResponseEntity<List<Reminder>> getAllReminders() {
        List<Reminder> reminders = reminderService.getAllReminders();
        return ResponseEntity.ok(reminders);
    }
}
