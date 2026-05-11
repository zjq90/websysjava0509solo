package com.hospital.appointment.service;

import com.hospital.appointment.common.BusinessException;
import com.hospital.appointment.common.ResultCode;
import com.hospital.appointment.entity.*;
import com.hospital.appointment.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 预约挂号服务类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Autowired
    private ScheduleSlotRepository scheduleSlotRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private PatientRepository patientRepository;

    @Value("${appointment.lock-minutes:10}")
    private Integer lockMinutes;

    @Transactional
    public ScheduleSlot lockSlot(Long slotId, Long userId) {
        ScheduleSlot slot = scheduleSlotRepository.findById(slotId)
                .orElseThrow(() -> new BusinessException("号源不存在"));
        
        if (!"available".equals(slot.getStatus())) {
            if ("locked".equals(slot.getStatus())) {
                if (slot.getLockUserId() != null && slot.getLockUserId().equals(userId)) {
                    if (slot.getLockExpireTime() != null && slot.getLockExpireTime().isAfter(LocalDateTime.now())) {
                        return slot;
                    }
                }
                throw new BusinessException(ResultCode.SLOT_LOCKED);
            }
            throw new BusinessException(ResultCode.SLOT_BOOKED);
        }
        
        slot.setStatus("locked");
        slot.setLockUserId(userId);
        slot.setLockExpireTime(LocalDateTime.now().plusMinutes(lockMinutes));
        
        return scheduleSlotRepository.save(slot);
    }

    @Transactional
    public ScheduleSlot unlockSlot(Long slotId, Long userId) {
        ScheduleSlot slot = scheduleSlotRepository.findById(slotId)
                .orElseThrow(() -> new BusinessException("号源不存在"));
        
        if ("locked".equals(slot.getStatus()) && 
            slot.getLockUserId() != null && slot.getLockUserId().equals(userId)) {
            slot.setStatus("available");
            slot.setLockUserId(null);
            slot.setLockExpireTime(null);
            return scheduleSlotRepository.save(slot);
        }
        
        return slot;
    }

    @Transactional
    public Appointment createAppointment(Long userId, Long slotId, Long patientId, String symptoms) {
        ScheduleSlot slot = scheduleSlotRepository.findById(slotId)
                .orElseThrow(() -> new BusinessException("号源不存在"));
        
        if (!"locked".equals(slot.getStatus()) || 
            slot.getLockUserId() == null || !slot.getLockUserId().equals(userId)) {
            throw new BusinessException("请先锁定号源");
        }
        
        if (slot.getLockExpireTime() != null && slot.getLockExpireTime().isBefore(LocalDateTime.now())) {
            slot.setStatus("available");
            slot.setLockUserId(null);
            slot.setLockExpireTime(null);
            scheduleSlotRepository.save(slot);
            throw new BusinessException(ResultCode.LOCK_EXPIRED);
        }
        
        Schedule schedule = scheduleRepository.findById(slot.getScheduleId())
                .orElseThrow(() -> new BusinessException("排班信息不存在"));
        
        Doctor doctor = doctorRepository.findById(schedule.getDoctorId())
                .orElseThrow(() -> new BusinessException("医生信息不存在"));
        
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new BusinessException("就诊人信息不存在"));
        
        if (!patient.getUserId().equals(userId)) {
            throw new BusinessException("无权使用该就诊人");
        }
        
        String appointmentNo = generateAppointmentNo();
        
        Appointment appointment = new Appointment();
        appointment.setUserId(userId);
        appointment.setScheduleId(schedule.getId());
        appointment.setSlotId(slotId);
        appointment.setDoctorId(schedule.getDoctorId());
        appointment.setDeptId(schedule.getDeptId());
        appointment.setAppointmentNo(appointmentNo);
        appointment.setAppointmentDate(schedule.getScheduleDate());
        appointment.setSlotTime(slot.getSlotTime());
        appointment.setPatientName(patient.getPatientName());
        appointment.setPatientPhone(patient.getPhone());
        appointment.setPatientIdCard(patient.getIdCard());
        appointment.setPatientRelation(patient.getRelation());
        appointment.setSymptoms(symptoms);
        appointment.setConsultationFee(doctor.getConsultationFee());
        appointment.setStatus("pending");
        
        appointment = appointmentRepository.save(appointment);
        
        slot.setStatus("booked");
        slot.setAppointmentId(appointment.getId());
        scheduleSlotRepository.save(slot);
        
        schedule.setBookedSlots(schedule.getBookedSlots() + 1);
        schedule.setAvailableSlots(Math.max(0, schedule.getAvailableSlots() - 1));
        scheduleRepository.save(schedule);
        
        return appointment;
    }

    @Transactional
    public Payment payAppointment(Long appointmentId, Long userId, String paymentMethod, Boolean useInsurance, String insuranceNo) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("预约记录不存在"));
        
        if (!appointment.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该预约");
        }
        
        if (!"pending".equals(appointment.getStatus())) {
            if ("paid".equals(appointment.getStatus())) {
                throw new BusinessException(ResultCode.APPOINTMENT_ALREADY_PAID);
            }
            throw new BusinessException("预约状态不支持支付");
        }
        
        Payment existingPayment = paymentRepository.findByAppointmentId(appointmentId).orElse(null);
        if (existingPayment != null && "paid".equals(existingPayment.getPayStatus())) {
            throw new BusinessException(ResultCode.APPOINTMENT_ALREADY_PAID);
        }
        
        BigDecimal totalAmount = appointment.getConsultationFee();
        BigDecimal insuranceAmount = BigDecimal.ZERO;
        BigDecimal selfPayAmount = totalAmount;
        
        if (useInsurance != null && useInsurance && insuranceNo != null && !insuranceNo.isEmpty()) {
            insuranceAmount = totalAmount.multiply(new BigDecimal("0.6"));
            selfPayAmount = totalAmount.subtract(insuranceAmount);
        }
        
        Payment payment = new Payment();
        payment.setPaymentNo(generatePaymentNo());
        payment.setAppointmentId(appointmentId);
        payment.setUserId(userId);
        payment.setAmount(totalAmount);
        payment.setInsuranceAmount(insuranceAmount);
        payment.setSelfPayAmount(selfPayAmount);
        payment.setPayMethod(paymentMethod);
        payment.setInsuranceNo(insuranceNo);
        payment.setPayStatus("paid");
        payment.setPaidTime(LocalDateTime.now());
        payment.setThirdPartyNo("TP" + System.currentTimeMillis());
        
        payment = paymentRepository.save(payment);
        
        appointment.setStatus("paid");
        appointmentRepository.save(appointment);
        
        return payment;
    }

    @Transactional
    public Appointment cancelAppointment(Long appointmentId, Long userId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("预约记录不存在"));
        
        if (!appointment.getUserId().equals(userId)) {
            throw new BusinessException("无权操作该预约");
        }
        
        if ("cancelled".equals(appointment.getStatus())) {
            throw new BusinessException("预约已取消");
        }
        
        if ("completed".equals(appointment.getStatus())) {
            throw new BusinessException("预约已完成，无法取消");
        }
        
        Optional<Payment> paymentOpt = paymentRepository.findByAppointmentId(appointmentId);
        if (paymentOpt.isPresent() && "paid".equals(paymentOpt.get().getPayStatus())) {
            Payment payment = paymentOpt.get();
            payment.setPayStatus("refunded");
            payment.setRefundReason(reason);
            paymentRepository.save(payment);
        }
        
        if (appointment.getSlotId() != null) {
            Optional<ScheduleSlot> slotOpt = scheduleSlotRepository.findById(appointment.getSlotId());
            if (slotOpt.isPresent()) {
                ScheduleSlot slot = slotOpt.get();
                slot.setStatus("available");
                slot.setAppointmentId(null);
                slot.setLockUserId(null);
                slot.setLockExpireTime(null);
                scheduleSlotRepository.save(slot);
            }
        }
        
        Optional<Schedule> scheduleOpt = scheduleRepository.findById(appointment.getScheduleId());
        if (scheduleOpt.isPresent()) {
            Schedule schedule = scheduleOpt.get();
            schedule.setBookedSlots(Math.max(0, schedule.getBookedSlots() - 1));
            schedule.setAvailableSlots(schedule.getAvailableSlots() + 1);
            scheduleRepository.save(schedule);
        }
        
        appointment.setStatus("cancelled");
        appointment.setCancelReason(reason);
        
        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointmentDetail(Long appointmentId, Long userId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("预约记录不存在"));
        
        if (!appointment.getUserId().equals(userId)) {
            throw new BusinessException("无权查看该预约");
        }
        
        doctorRepository.findById(appointment.getDoctorId()).ifPresent(appointment::setDoctor);
        departmentRepository.findById(appointment.getDeptId()).ifPresent(dept -> appointment.setDeptName(dept.getDeptName()));
        paymentRepository.findByAppointmentId(appointmentId).ifPresent(appointment::setPayment);
        
        return appointment;
    }

    public List<Appointment> getUserAppointments(Long userId, String status) {
        List<Appointment> appointments = appointmentRepository.findByUserIdAndStatus(userId, status);
        for (Appointment app : appointments) {
            doctorRepository.findById(app.getDoctorId()).ifPresent(app::setDoctor);
            departmentRepository.findById(app.getDeptId()).ifPresent(dept -> app.setDeptName(dept.getDeptName()));
        }
        return appointments;
    }

    public List<ScheduleSlot> getScheduleSlots(Long scheduleId) {
        List<ScheduleSlot> slots = scheduleSlotRepository.findByScheduleIdOrderBySlotIndexAsc(scheduleId);
        LocalDateTime now = LocalDateTime.now();
        for (ScheduleSlot slot : slots) {
            if ("locked".equals(slot.getStatus()) && slot.getLockExpireTime() != null) {
                if (slot.getLockExpireTime().isAfter(now)) {
                    slot.setRemainingLockSeconds(Duration.between(now, slot.getLockExpireTime()).getSeconds());
                } else {
                    slot.setStatus("available");
                    slot.setLockUserId(null);
                    slot.setLockExpireTime(null);
                    scheduleSlotRepository.save(slot);
                }
            }
        }
        return slots;
    }

    @Transactional
    public void releaseExpiredLocks() {
        List<ScheduleSlot> expiredSlots = scheduleSlotRepository.findExpiredLocks(LocalDateTime.now());
        for (ScheduleSlot slot : expiredSlots) {
            slot.setStatus("available");
            slot.setLockUserId(null);
            slot.setLockExpireTime(null);
            scheduleSlotRepository.save(slot);
        }
    }

    private String generateAppointmentNo() {
        return "APPT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    private String generatePaymentNo() {
        return "PAY" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }
}
