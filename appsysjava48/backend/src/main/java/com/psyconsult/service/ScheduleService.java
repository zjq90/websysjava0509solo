package com.psyconsult.service;

import com.psyconsult.entity.Schedule;
import com.psyconsult.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> getSchedulesByCounselorAndDate(Long counselorId, LocalDate date) {
        return scheduleRepository.findByCounselorIdAndDate(counselorId, date);
    }

    public List<Schedule> getSchedulesByDateRange(Long counselorId, LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByCounselorIdAndDateBetween(counselorId, startDate, endDate);
    }

    public List<Schedule> getAvailableSchedules(Long counselorId, LocalDate date) {
        return scheduleRepository.findAvailableSchedules(counselorId, date);
    }

    @Transactional
    public Schedule createSchedule(Schedule schedule) {
        List<Schedule> conflicts = scheduleRepository.findConflictingSchedules(
                schedule.getCounselorId(),
                schedule.getDate(),
                schedule.getEndTime(),
                schedule.getStartTime()
        );
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("该时间段已有排班，请调整时间");
        }
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public Schedule updateSchedule(Long id, Schedule schedule) {
        Optional<Schedule> existingOpt = scheduleRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("排班记录不存在");
        }
        Schedule existing = existingOpt.get();
        existing.setDate(schedule.getDate());
        existing.setStartTime(schedule.getStartTime());
        existing.setEndTime(schedule.getEndTime());
        existing.setMaxAppointments(schedule.getMaxAppointments());
        existing.setIsAvailable(schedule.getIsAvailable());
        existing.setRemark(schedule.getRemark());
        return scheduleRepository.save(existing);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        Optional<Schedule> scheduleOpt = scheduleRepository.findById(id);
        if (scheduleOpt.isPresent()) {
            Schedule schedule = scheduleOpt.get();
            schedule.setStatus(0);
            scheduleRepository.save(schedule);
        }
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    @Transactional
    public void incrementAppointmentCount(Long scheduleId) {
        Optional<Schedule> scheduleOpt = scheduleRepository.findById(scheduleId);
        if (scheduleOpt.isPresent()) {
            Schedule schedule = scheduleOpt.get();
            schedule.setCurrentAppointments(schedule.getCurrentAppointments() + 1);
            if (schedule.getCurrentAppointments() >= schedule.getMaxAppointments()) {
                schedule.setIsAvailable(false);
            }
            scheduleRepository.save(schedule);
        }
    }
}
