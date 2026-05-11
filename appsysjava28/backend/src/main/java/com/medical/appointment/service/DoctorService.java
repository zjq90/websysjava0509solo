package com.medical.appointment.service;

import com.medical.appointment.entity.Doctor;
import com.medical.appointment.entity.DoctorSchedule;
import com.medical.appointment.entity.Review;
import com.medical.appointment.repository.DoctorRepository;
import com.medical.appointment.repository.DoctorScheduleRepository;
import com.medical.appointment.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorScheduleRepository scheduleRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Map<String, Object>> getDoctorsByDepartment(Long departmentId) {
        List<Doctor> doctors = doctorRepository.findByDepartment_IdAndIsActiveTrueOrderByRatingDesc(departmentId);
        return doctors.stream().map(this::convertToListVO).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findByIsActiveTrueOrderByRatingDescVisitCountDesc();
        return doctors.stream().map(this::convertToListVO).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPopularDoctors() {
        List<Doctor> doctors = doctorRepository.findPopularDoctors();
        return doctors.stream().limit(10).map(this::convertToListVO).collect(Collectors.toList());
    }

    public Map<String, Object> getDoctorDetail(Long doctorId) {
        Doctor doctor = doctorRepository.findByIdAndIsActiveTrue(doctorId)
                .orElseThrow(() -> new RuntimeException("医生不存在"));
        
        Map<String, Object> result = convertToDetailVO(doctor);
        
        List<Map<String, Object>> schedules = getDoctorSchedules(doctorId);
        result.put("schedules", schedules);
        
        List<Map<String, Object>> reviews = getDoctorReviews(doctorId);
        result.put("reviews", reviews);
        
        return result;
    }

    public List<Map<String, Object>> searchDoctors(String keyword) {
        List<Doctor> doctors = doctorRepository.searchByKeyword(keyword);
        return doctors.stream().map(this::convertToListVO).collect(Collectors.toList());
    }

    private List<Map<String, Object>> getDoctorSchedules(Long doctorId) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(14);
        List<DoctorSchedule> schedules = scheduleRepository.findByDoctor_IdAndScheduleDateBetweenOrderByScheduleDateAsc(
                doctorId, startDate, endDate);
        
        return schedules.stream().map(s -> {
            Map<String, Object> vo = new HashMap<>();
            vo.put("id", s.getId());
            vo.put("scheduleDate", s.getScheduleDate());
            vo.put("timeSlot", s.getTimeSlot());
            vo.put("startTime", s.getStartTime());
            vo.put("endTime", s.getEndTime());
            vo.put("totalSlots", s.getTotalSlots());
            vo.put("bookedSlots", s.getBookedSlots());
            vo.put("availableSlots", s.getAvailableSlots());
            vo.put("isActive", s.getIsActive());
            return vo;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> getDoctorReviews(Long doctorId) {
        List<Review> reviews = reviewRepository.findByDoctor_IdOrderByCreatedAtDesc(doctorId);
        return reviews.stream().limit(10).map(r -> {
            Map<String, Object> vo = new HashMap<>();
            vo.put("id", r.getId());
            vo.put("rating", r.getRating());
            vo.put("comment", r.getComment());
            vo.put("tags", r.getTags());
            vo.put("isAnonymous", r.getIsAnonymous());
            vo.put("helpfulCount", r.getHelpfulCount());
            vo.put("createdAt", r.getCreatedAt());
            if (!r.getIsAnonymous() && r.getPatient() != null) {
                vo.put("patientName", r.getPatient().getRealName());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("医生不存在"));
    }

    private Map<String, Object> convertToListVO(Doctor doctor) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("id", doctor.getId());
        vo.put("realName", doctor.getRealName());
        vo.put("title", getTitleDisplayName(doctor.getTitle()));
        vo.put("titleCode", doctor.getTitle().name());
        vo.put("specialty", doctor.getSpecialty());
        vo.put("avatar", doctor.getAvatar());
        vo.put("rating", doctor.getRating());
        vo.put("reviewCount", doctor.getReviewCount());
        vo.put("visitCount", doctor.getVisitCount());
        vo.put("consultationFee", doctor.getConsultationFee());
        if (doctor.getDepartment() != null) {
            vo.put("departmentId", doctor.getDepartment().getId());
            vo.put("departmentName", doctor.getDepartment().getName());
        }
        return vo;
    }

    private Map<String, Object> convertToDetailVO(Doctor doctor) {
        Map<String, Object> vo = convertToListVO(doctor);
        vo.put("introduction", doctor.getIntroduction());
        return vo;
    }

    private String getTitleDisplayName(Doctor.Title title) {
        switch (title) {
            case INTERN: return "实习医师";
            case RESIDENT: return "住院医师";
            case ATTENDING: return "主治医师";
            case ASSOCIATE_PROFESSOR: return "副主任医师";
            case PROFESSOR: return "主任医师";
            default: return title.name();
        }
    }
}
