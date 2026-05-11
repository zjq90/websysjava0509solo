package com.medical.registration.service;

import com.medical.registration.common.BusinessException;
import com.medical.registration.common.ErrorCode;
import com.medical.registration.dto.PaymentDTO;
import com.medical.registration.dto.RegistrationDTO;
import com.medical.registration.dto.RegistrationVO;
import com.medical.registration.entity.*;
import com.medical.registration.repository.*;
import com.medical.registration.util.QrCodeUtil;
import com.medical.registration.aop.LogOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegistrationService {
    
    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Transactional
    @LogOperation(operationType = "挂号", operationDesc = "预约挂号", module = "挂号模块")
    public RegistrationVO createRegistration(Long userId, RegistrationDTO dto) {
        Schedule schedule = scheduleRepository.findById(dto.getScheduleId())
                .orElseThrow(() -> new BusinessException("号源不存在"));
        
        if (schedule.getAvailableCount() <= 0) {
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_AVAILABLE);
        }
        
        if (registrationRepository.existsByUserIdAndScheduleIdAndStatus(userId, dto.getScheduleId())) {
            throw new BusinessException(ErrorCode.REGISTRATION_ALREADY_EXIST);
        }
        
        int updated = scheduleRepository.decreaseAvailableCount(dto.getScheduleId());
        if (updated <= 0) {
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_AVAILABLE);
        }
        
        Doctor doctor = doctorRepository.findById(schedule.getDoctorId())
                .orElseThrow(() -> new BusinessException("医生信息不存在"));
        
        String registrationNo = "REG" + System.currentTimeMillis();
        
        Registration registration = new Registration();
        registration.setRegistrationNo(registrationNo);
        registration.setUserId(userId);
        registration.setDoctorId(schedule.getDoctorId());
        registration.setScheduleId(schedule.getId());
        registration.setDeptCode(schedule.getDeptCode());
        registration.setVisitDate(schedule.getScheduleDate());
        registration.setVisitTime(schedule.getStartTime());
        registration.setTimeSlot(schedule.getTimeSlot());
        registration.setAmount(doctor.getRegistrationFee());
        registration.setPaymentStatus("UNPAID");
        registration.setStatus("BOOKED");
        registration.setSymptoms(dto.getSymptoms());
        registration.setRemark(dto.getRemark());
        registration = registrationRepository.save(registration);
        
        String qrContent = registrationNo + "|" + userId + "|" + schedule.getScheduleDate();
        String qrCode = QrCodeUtil.generateQrCodeBase64(qrContent, 200, 200);
        registration.setQrCode(qrCode);
        registration = registrationRepository.save(registration);
        
        return convertToVO(registration, doctor);
    }
    
    @Transactional
    @LogOperation(operationType = "支付", operationDesc = "支付挂号费", module = "支付模块")
    public RegistrationVO payRegistration(Long userId, PaymentDTO dto) {
        Registration registration = registrationRepository.findByRegistrationNo(dto.getRegistrationNo())
                .orElseThrow(() -> new BusinessException(ErrorCode.REGISTRATION_NOT_EXIST));
        
        if (!registration.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        
        if (!"BOOKED".equals(registration.getStatus())) {
            throw new BusinessException("当前状态不允许支付");
        }
        
        if ("PAID".equals(registration.getPaymentStatus())) {
            throw new BusinessException("已支付，请勿重复支付");
        }
        
        String paymentNo = "PAY" + System.currentTimeMillis();
        Payment payment = new Payment();
        payment.setPaymentNo(paymentNo);
        payment.setRegistrationNo(dto.getRegistrationNo());
        payment.setUserId(userId);
        payment.setAmount(registration.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setStatus("SUCCESS");
        payment.setSuccessTime(LocalDateTime.now());
        paymentRepository.save(payment);
        
        registration.setPaymentMethod(dto.getPaymentMethod());
        registration.setPaymentStatus("PAID");
        registration = registrationRepository.save(registration);
        
        Doctor doctor = doctorRepository.findById(registration.getDoctorId())
                .orElseThrow(() -> new BusinessException("医生信息不存在"));
        
        return convertToVO(registration, doctor);
    }
    
    @Transactional
    @LogOperation(operationType = "取消挂号", operationDesc = "取消预约挂号", module = "挂号模块")
    public RegistrationVO cancelRegistration(Long userId, String registrationNo) {
        Registration registration = registrationRepository.findByRegistrationNo(registrationNo)
                .orElseThrow(() -> new BusinessException(ErrorCode.REGISTRATION_NOT_EXIST));
        
        if (!registration.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        
        if (!"BOOKED".equals(registration.getStatus()) && !"VISITING".equals(registration.getStatus())) {
            throw new BusinessException(ErrorCode.INVALID_STATUS);
        }
        
        LocalDate today = LocalDate.now();
        LocalDate visitDate = registration.getVisitDate();
        LocalTime cancelDeadline = LocalTime.of(17, 0);
        
        if (visitDate.isEqual(today.plusDays(1))) {
            if (LocalTime.now().isAfter(cancelDeadline)) {
                throw new BusinessException(ErrorCode.CANCEL_TIME_EXPIRED);
            }
        } else if (visitDate.isBefore(today.plusDays(1))) {
            throw new BusinessException(ErrorCode.CANCEL_TIME_EXPIRED);
        }
        
        scheduleRepository.increaseAvailableCount(registration.getScheduleId());
        
        registration.setStatus("CANCELLED");
        registration.setCancelTime(LocalDateTime.now());
        
        if ("PAID".equals(registration.getPaymentStatus())) {
            registration.setRefundStatus("REFUNDING");
            Payment payment = paymentRepository.findByRegistrationNo(registrationNo).orElse(null);
            if (payment != null) {
                payment.setStatus("REFUNDING");
                payment.setRefundTime(LocalDateTime.now());
                paymentRepository.save(payment);
            }
        }
        
        registration = registrationRepository.save(registration);
        
        Doctor doctor = doctorRepository.findById(registration.getDoctorId())
                .orElseThrow(() -> new BusinessException("医生信息不存在"));
        
        return convertToVO(registration, doctor);
    }
    
    public Page<RegistrationVO> getRegistrationList(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Registration> registrations = registrationRepository.findByUserIdOrderByCreateTimeDesc(userId, pageable);
        
        return registrations.map(r -> {
            Doctor doctor = doctorRepository.findById(r.getDoctorId()).orElse(null);
            return convertToVO(r, doctor);
        });
    }
    
    public RegistrationVO getRegistrationDetail(Long userId, String registrationNo) {
        Registration registration = registrationRepository.findByRegistrationNo(registrationNo)
                .orElseThrow(() -> new BusinessException(ErrorCode.REGISTRATION_NOT_EXIST));
        
        if (!registration.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        
        Doctor doctor = doctorRepository.findById(registration.getDoctorId())
                .orElseThrow(() -> new BusinessException("医生信息不存在"));
        
        return convertToVO(registration, doctor);
    }
    
    public List<RegistrationVO> getTodayAppointments(Long userId) {
        LocalDate today = LocalDate.now();
        List<Registration> registrations = registrationRepository.findTodayAppointments(userId, today);
        
        return registrations.stream().map(r -> {
            Doctor doctor = doctorRepository.findById(r.getDoctorId()).orElse(null);
            return convertToVO(r, doctor);
        }).collect(Collectors.toList());
    }
    
    private RegistrationVO convertToVO(Registration registration, Doctor doctor) {
        RegistrationVO vo = new RegistrationVO();
        vo.setId(registration.getId());
        vo.setRegistrationNo(registration.getRegistrationNo());
        vo.setDeptCode(registration.getDeptCode());
        vo.setDoctorId(registration.getDoctorId());
        vo.setVisitDate(registration.getVisitDate());
        vo.setVisitTime(registration.getVisitTime());
        vo.setTimeSlot(registration.getTimeSlot());
        vo.setAmount(registration.getAmount());
        vo.setPaymentMethod(registration.getPaymentMethod());
        vo.setPaymentStatus(registration.getPaymentStatus());
        vo.setStatus(registration.getStatus());
        vo.setQrCode(registration.getQrCode());
        vo.setSymptoms(registration.getSymptoms());
        vo.setCreateTime(registration.getCreateTime());
        
        Department dept = departmentRepository.findByDeptCode(registration.getDeptCode()).orElse(null);
        if (dept != null) {
            vo.setDeptName(dept.getDeptName());
        }
        
        if (doctor != null) {
            vo.setDoctorName(doctor.getName());
            vo.setDoctorTitle(doctor.getTitle());
        }
        
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("BOOKED", "预约成功");
        statusMap.put("VISITING", "就诊中");
        statusMap.put("VISITED", "已就诊");
        statusMap.put("CANCELLED", "已取消");
        statusMap.put("REFUNDING", "退费中");
        vo.setStatusDesc(statusMap.getOrDefault(registration.getStatus(), registration.getStatus()));
        
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        String statusTag = null;
        if ("BOOKED".equals(registration.getStatus()) || "VISITING".equals(registration.getStatus())) {
            if (registration.getVisitDate().isEqual(today)) {
                if (registration.getVisitTime().isAfter(now)) {
                    statusTag = "今日待就诊";
                }
            } else if (registration.getVisitDate().isEqual(today.plusDays(1))) {
                statusTag = "明日就诊";
            }
        }
        vo.setStatusTag(statusTag);
        
        LocalDate visitDate = registration.getVisitDate();
        LocalTime deadlineTime = LocalTime.of(17, 0);
        boolean canCancel = false;
        String cancelDeadlineDesc = "";
        
        if ("BOOKED".equals(registration.getStatus()) || "VISITING".equals(registration.getStatus())) {
            if (visitDate.isAfter(today.plusDays(1))) {
                canCancel = true;
                cancelDeadlineDesc = "就诊前一日17:00前可取消";
            } else if (visitDate.isEqual(today.plusDays(1))) {
                if (LocalTime.now().isBefore(deadlineTime)) {
                    canCancel = true;
                    cancelDeadlineDesc = "今日17:00前可取消";
                } else {
                    cancelDeadlineDesc = "取消时间已过";
                }
            } else if (visitDate.isEqual(today)) {
                cancelDeadlineDesc = "就诊当日不可取消";
            }
        }
        vo.setCanCancel(canCancel);
        vo.setCancelDeadlineDesc(cancelDeadlineDesc);
        
        return vo;
    }
    
    @Transactional
    public void simulateRefundComplete(String registrationNo) {
        Registration registration = registrationRepository.findByRegistrationNo(registrationNo).orElse(null);
        if (registration != null && "CANCELLED".equals(registration.getStatus())) {
            registration.setRefundStatus("REFUNDED");
            registrationRepository.save(registration);
            
            Payment payment = paymentRepository.findByRegistrationNo(registrationNo).orElse(null);
            if (payment != null) {
                payment.setStatus("REFUNDED");
                paymentRepository.save(payment);
            }
        }
    }
    
    @Transactional
    public void simulateVisitComplete(String registrationNo) {
        Registration registration = registrationRepository.findByRegistrationNo(registrationNo).orElse(null);
        if (registration != null && ("BOOKED".equals(registration.getStatus()) || "VISITING".equals(registration.getStatus()))) {
            registration.setStatus("VISITED");
            registration.setVisitCompleteTime(LocalDateTime.now());
            registrationRepository.save(registration);
        }
    }
}
