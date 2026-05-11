package com.medical.appointment.service;

import com.medical.appointment.entity.*;
import com.medical.appointment.repository.AppointmentRepository;
import com.medical.appointment.repository.DoctorScheduleRepository;
import com.medical.appointment.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorScheduleRepository scheduleRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AuditLogService auditLogService;

    @Transactional
    public Map<String, Object> createAppointment(Long userId, Long patientId, Long doctorId, 
                                                 Long scheduleId, String symptoms, String remark) {
        User user = userService.findById(userId);
        Doctor doctor = doctorService.findById(doctorId);
        
        Patient patient = null;
        if (patientId != null) {
            Map<String, Object> patientMap = patientService.getPatientById(userId, patientId);
            patient = new Patient();
            patient.setId(Long.valueOf(patientMap.get("id").toString()));
        } else {
            List<Map<String, Object>> patients = patientService.getPatientsByUserId(userId);
            if (patients.isEmpty()) {
                throw new RuntimeException("请先添加就诊人");
            }
            patient = new Patient();
            patient.setId(Long.valueOf(patients.get(0).get("id").toString()));
        }

        DoctorSchedule schedule = null;
        LocalDate appointmentDate;
        LocalTime startTime;
        LocalTime endTime;

        if (scheduleId != null) {
            schedule = scheduleRepository.findById(scheduleId)
                    .orElseThrow(() -> new RuntimeException("排班不存在"));
            if (schedule.getAvailableSlots() <= 0) {
                throw new RuntimeException("该时段已约满");
            }
            appointmentDate = schedule.getScheduleDate();
            startTime = schedule.getStartTime();
            endTime = schedule.getEndTime();
        } else {
            appointmentDate = LocalDate.now().plusDays(1);
            startTime = LocalTime.of(9, 0);
            endTime = LocalTime.of(9, 30);
        }

        Appointment appointment = Appointment.builder()
                .appointmentNo("AP" + System.currentTimeMillis())
                .user(user)
                .patient(patient)
                .doctor(doctor)
                .department(doctor.getDepartment())
                .schedule(schedule)
                .appointmentDate(appointmentDate)
                .startTime(startTime)
                .endTime(endTime)
                .amount(doctor.getConsultationFee())
                .symptoms(symptoms)
                .remark(remark)
                .build();

        appointment = appointmentRepository.save(appointment);

        if (schedule != null) {
            schedule.setBookedSlots(schedule.getBookedSlots() + 1);
            scheduleRepository.save(schedule);
        }

        auditLogService.logCreateAppointment(userId, user.getPhone(), appointment.getId());

        return convertToVO(appointment);
    }

    @Transactional
    public Appointment payAppointment(Long userId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndUser_Id(appointmentId, userId)
                .orElseThrow(() -> new RuntimeException("预约不存在或无权限"));

        if (appointment.getStatus() != Appointment.AppointmentStatus.PENDING_PAYMENT) {
            throw new RuntimeException("当前状态不支持支付");
        }

        appointment.setStatus(Appointment.AppointmentStatus.PAID);
        appointment.setPaymentStatus(Appointment.PaymentStatus.PAID);
        appointment.setPaymentTime(java.time.LocalDateTime.now());

        Appointment saved = appointmentRepository.save(appointment);

        User user = userService.findById(userId);
        auditLogService.logPayAppointment(userId, user.getPhone(), appointmentId);

        return saved;
    }

    @Transactional
    public void cancelAppointment(Long userId, Long appointmentId, String reason) {
        Appointment appointment = appointmentRepository.findByIdAndUser_Id(appointmentId, userId)
                .orElseThrow(() -> new RuntimeException("预约不存在或无权限"));

        if (appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED ||
            appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED) {
            throw new RuntimeException("当前状态不支持取消");
        }

        if (appointment.getSchedule() != null) {
            DoctorSchedule schedule = appointment.getSchedule();
            if (schedule.getBookedSlots() > 0) {
                schedule.setBookedSlots(schedule.getBookedSlots() - 1);
                scheduleRepository.save(schedule);
            }
        }

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointment.setCancelReason(reason);
        appointmentRepository.save(appointment);

        User user = userService.findById(userId);
        auditLogService.logCancelAppointment(userId, user.getPhone(), appointmentId);
    }

    public List<Map<String, Object>> getUserAppointments(Long userId, String status) {
        List<Appointment> appointments;
        if (status != null && !status.isEmpty()) {
            List<Appointment.AppointmentStatus> statusList = Arrays.stream(status.split(","))
                    .map(Appointment.AppointmentStatus::valueOf)
                    .collect(Collectors.toList());
            appointments = appointmentRepository.findByUser_IdAndStatusInOrderByCreatedAtDesc(userId, statusList);
        } else {
            appointments = appointmentRepository.findByUser_IdOrderByCreatedAtDesc(userId);
        }
        return appointments.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    public Map<String, Object> getAppointmentDetail(Long userId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndUser_Id(appointmentId, userId)
                .orElseThrow(() -> new RuntimeException("预约不存在或无权限"));
        return convertToDetailVO(appointment);
    }

    @Transactional
    public Map<String, Object> createReview(Long userId, Long appointmentId, Double rating, 
                                            String comment, String tags, Boolean isAnonymous) {
        Appointment appointment = appointmentRepository.findByIdAndUser_Id(appointmentId, userId)
                .orElseThrow(() -> new RuntimeException("预约不存在或无权限"));

        if (appointment.getStatus() != Appointment.AppointmentStatus.COMPLETED) {
            throw new RuntimeException("只能对已完成的预约进行评价");
        }

        if (reviewRepository.existsByAppointment_Id(appointmentId)) {
            throw new RuntimeException("该预约已评价过");
        }

        Review review = Review.builder()
                .appointment(appointment)
                .patient(appointment.getPatient())
                .doctor(appointment.getDoctor())
                .user(appointment.getUser())
                .rating(java.math.BigDecimal.valueOf(rating))
                .comment(comment)
                .tags(tags)
                .isAnonymous(isAnonymous != null ? isAnonymous : false)
                .build();

        review = reviewRepository.save(review);

        updateDoctorRating(appointment.getDoctor().getId());

        Map<String, Object> result = new HashMap<>();
        result.put("id", review.getId());
        result.put("rating", review.getRating());
        result.put("comment", review.getComment());
        result.put("createdAt", review.getCreatedAt());

        return result;
    }

    private void updateDoctorRating(Long doctorId) {
        java.math.BigDecimal avgRating = reviewRepository.findAverageRatingByDoctorId(doctorId);
        long count = reviewRepository.countByDoctorId(doctorId);
        
        Doctor doctor = doctorService.findById(doctorId);
        if (avgRating != null) {
            doctor.setRating(avgRating);
        }
        doctor.setReviewCount((int) count);
    }

    private Map<String, Object> convertToVO(Appointment appointment) {
        Map<String, Object> vo = new HashMap<>();
        vo.put("id", appointment.getId());
        vo.put("appointmentNo", appointment.getAppointmentNo());
        vo.put("appointmentDate", appointment.getAppointmentDate());
        vo.put("startTime", appointment.getStartTime());
        vo.put("endTime", appointment.getEndTime());
        vo.put("status", appointment.getStatus());
        vo.put("statusText", getStatusText(appointment.getStatus()));
        vo.put("amount", appointment.getAmount());
        vo.put("paymentStatus", appointment.getPaymentStatus());
        vo.put("symptoms", appointment.getSymptoms());
        
        if (appointment.getDoctor() != null) {
            vo.put("doctorId", appointment.getDoctor().getId());
            vo.put("doctorName", appointment.getDoctor().getRealName());
        }
        if (appointment.getDepartment() != null) {
            vo.put("departmentName", appointment.getDepartment().getName());
        }
        if (appointment.getPatient() != null) {
            vo.put("patientName", appointment.getPatient().getRealName());
        }
        
        return vo;
    }

    private Map<String, Object> convertToDetailVO(Appointment appointment) {
        Map<String, Object> vo = convertToVO(appointment);
        vo.put("remark", appointment.getRemark());
        vo.put("cancelReason", appointment.getCancelReason());
        vo.put("paymentTime", appointment.getPaymentTime());
        vo.put("createdAt", appointment.getCreatedAt());
        return vo;
    }

    private String getStatusText(Appointment.AppointmentStatus status) {
        switch (status) {
            case PENDING_PAYMENT: return "待支付";
            case PAID: return "已支付";
            case CONFIRMED: return "已确认";
            case COMPLETED: return "已完成";
            case CANCELLED: return "已取消";
            case NO_SHOW: return "未就诊";
            default: return status.name();
        }
    }
}
