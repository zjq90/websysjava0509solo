package com.photostudio.service;

import com.photostudio.entity.Employee;
import com.photostudio.entity.Schedule;
import com.photostudio.entity.Schedule.ScheduleStatus;
import com.photostudio.repository.EmployeeRepository;
import com.photostudio.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * 排班服务类
 * 提供排班管理相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, EmployeeRepository employeeRepository) {
        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * 获取所有排班
     * @return 排班列表
     */
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    /**
     * 根据ID获取排班
     * @param id 排班ID
     * @return 排班信息
     */
    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    /**
     * 根据员工获取排班
     * @param employeeId 员工ID
     * @return 排班列表
     */
    public List<Schedule> getSchedulesByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("员工不存在"));
        return scheduleRepository.findByEmployee(employee);
    }

    /**
     * 根据日期获取排班
     * @param date 日期
     * @return 排班列表
     */
    public List<Schedule> getSchedulesByDate(LocalDate date) {
        return scheduleRepository.findByScheduleDate(date);
    }

    /**
     * 获取日期范围内的排班
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班列表
     */
    public List<Schedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByScheduleDateBetween(startDate, endDate);
    }

    /**
     * 根据状态获取排班
     * @param status 状态
     * @return 排班列表
     */
    public List<Schedule> getSchedulesByStatus(ScheduleStatus status) {
        return scheduleRepository.findByStatus(status);
    }

    /**
     * 创建排班
     * @param schedule 排班信息
     * @return 创建的排班
     */
    @Transactional
    public Schedule createSchedule(Schedule schedule) {
        Employee employee = employeeRepository.findById(schedule.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("员工不存在"));
        
        if (scheduleRepository.existsConflict(employee, schedule.getScheduleDate(), 
                schedule.getStartTime(), schedule.getEndTime(), null)) {
            throw new RuntimeException("该员工在指定时间已有排班");
        }
        
        schedule.setEmployee(employee);
        return scheduleRepository.save(schedule);
    }

    /**
     * 更新排班
     * @param id 排班ID
     * @param schedule 排班信息
     * @return 更新后的排班
     */
    @Transactional
    public Schedule updateSchedule(Long id, Schedule schedule) {
        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("排班不存在"));
        
        Employee employee = employeeRepository.findById(schedule.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("员工不存在"));
        
        if (scheduleRepository.existsConflict(employee, schedule.getScheduleDate(), 
                schedule.getStartTime(), schedule.getEndTime(), id)) {
            throw new RuntimeException("该员工在指定时间已有排班");
        }
        
        existing.setEmployee(employee);
        existing.setScheduleDate(schedule.getScheduleDate());
        existing.setStartTime(schedule.getStartTime());
        existing.setEndTime(schedule.getEndTime());
        existing.setCustomerName(schedule.getCustomerName());
        existing.setCustomerPhone(schedule.getCustomerPhone());
        existing.setShootingTheme(schedule.getShootingTheme());
        existing.setStatus(schedule.getStatus());
        existing.setRemark(schedule.getRemark());
        
        return scheduleRepository.save(existing);
    }

    /**
     * 删除排班
     * @param id 排班ID
     */
    @Transactional
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("排班不存在");
        }
        scheduleRepository.deleteById(id);
    }

    /**
     * 完成排班
     * @param id 排班ID
     * @return 更新后的排班
     */
    @Transactional
    public Schedule completeSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("排班不存在"));
        schedule.setStatus(ScheduleStatus.COMPLETED);
        return scheduleRepository.save(schedule);
    }

    /**
     * 取消排班
     * @param id 排班ID
     * @return 更新后的排班
     */
    @Transactional
    public Schedule cancelSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("排班不存在"));
        schedule.setStatus(ScheduleStatus.CANCELLED);
        return scheduleRepository.save(schedule);
    }

    /**
     * 标记已提醒
     * @param id 排班ID
     * @return 更新后的排班
     */
    @Transactional
    public Schedule markAsReminded(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("排班不存在"));
        schedule.setReminded(true);
        schedule.setRemindTime(LocalDateTime.now());
        return scheduleRepository.save(schedule);
    }

    /**
     * 获取未提醒的排班
     * @return 排班列表
     */
    public List<Schedule> getUnremindedSchedules() {
        return scheduleRepository.findUnremindedSchedules(LocalDate.now());
    }

    /**
     * 检查排班冲突
     * @param employeeId 员工ID
     * @param date 日期
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否存在冲突
     */
    public boolean checkConflict(Long employeeId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("员工不存在"));
        return scheduleRepository.existsConflict(employee, date, startTime, endTime, null);
    }
}
