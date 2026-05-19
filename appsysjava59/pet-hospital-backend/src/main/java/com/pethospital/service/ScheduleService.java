package com.pethospital.service;

import com.pethospital.entity.Holiday;
import com.pethospital.entity.Schedule;
import com.pethospital.entity.ScheduleChangeRequest;
import com.pethospital.repository.HolidayRepository;
import com.pethospital.repository.ScheduleChangeRequestRepository;
import com.pethospital.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleChangeRequestRepository scheduleChangeRequestRepository;

    @Autowired
    private HolidayRepository holidayRepository;

    public List<Schedule> getDoctorSchedule(Long doctorId, LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByDoctorIdAndScheduleDateBetween(doctorId, startDate, endDate);
    }

    public List<Schedule> getAllSchedule(LocalDate startDate, LocalDate endDate) {
        return scheduleRepository.findByScheduleDateBetween(startDate, endDate);
    }

    @Transactional
    public Schedule addSchedule(Schedule schedule) {
        schedule.setStatus("CONFIRMED");
        return scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    public List<ScheduleChangeRequest> getChangeRequestsByDoctor(Long doctorId) {
        return scheduleChangeRequestRepository.findByDoctorId(doctorId);
    }

    public List<ScheduleChangeRequest> getPendingChangeRequests() {
        return scheduleChangeRequestRepository.findByStatus("PENDING");
    }

    @Transactional
    public ScheduleChangeRequest createChangeRequest(ScheduleChangeRequest request) {
        request.setStatus("PENDING");
        return scheduleChangeRequestRepository.save(request);
    }

    @Transactional
    public ScheduleChangeRequest approveChangeRequest(Long id, Long approverId, String approveNotes) {
        Optional<ScheduleChangeRequest> requestOpt = scheduleChangeRequestRepository.findById(id);
        if (requestOpt.isPresent()) {
            ScheduleChangeRequest request = requestOpt.get();
            request.setStatus("APPROVED");
            request.setApproverId(approverId);
            request.setApproveTime(LocalDateTime.now());
            request.setApproveNotes(approveNotes);
            
            if (request.getOriginalScheduleId() != null) {
                scheduleRepository.deleteById(request.getOriginalScheduleId());
            }
            
            Schedule newSchedule = new Schedule();
            newSchedule.setDoctorId(request.getDoctorId());
            newSchedule.setScheduleDate(request.getTargetDate());
            newSchedule.setShiftType(request.getTargetShift());
            newSchedule.setStatus("CONFIRMED");
            scheduleRepository.save(newSchedule);
            
            return scheduleChangeRequestRepository.save(request);
        }
        return null;
    }

    @Transactional
    public ScheduleChangeRequest rejectChangeRequest(Long id, Long approverId, String approveNotes) {
        Optional<ScheduleChangeRequest> requestOpt = scheduleChangeRequestRepository.findById(id);
        if (requestOpt.isPresent()) {
            ScheduleChangeRequest request = requestOpt.get();
            request.setStatus("REJECTED");
            request.setApproverId(approverId);
            request.setApproveTime(LocalDateTime.now());
            request.setApproveNotes(approveNotes);
            return scheduleChangeRequestRepository.save(request);
        }
        return null;
    }

    public List<Holiday> getDoctorHolidays(Long doctorId, LocalDate startDate, LocalDate endDate) {
        return holidayRepository.findByDoctorIdAndHolidayDateBetween(doctorId, startDate, endDate);
    }

    @Transactional
    public Holiday addHoliday(Holiday holiday) {
        holiday.setStatus("APPROVED");
        return holidayRepository.save(holiday);
    }

    @Transactional
    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }
}
