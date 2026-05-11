package com.hospital.service;

import com.hospital.dto.AppointmentDTO;
import com.hospital.dto.AppointmentVO;
import com.hospital.entity.Appointment;
import com.hospital.entity.Department;
import com.hospital.entity.Doctor;
import com.hospital.entity.NotificationSetting;
import com.hospital.entity.User;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.NotificationSettingRepository;
import com.hospital.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * 预约服务类
 * 处理预约挂号业务
 * 
 * @author hospital
 * @version 1.0.0
 */
@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private NotificationSettingRepository notificationSettingRepository;

    @Autowired
    private MessageService messageService;

    /**
     * 创建预约
     * 
     * @param patientId 患者ID
     * @param dto 预约请求DTO
     * @return 预约信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Appointment createAppointment(Long patientId, AppointmentDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("医生不存在"));

        if (doctor.getStatus() != 1) {
            throw new RuntimeException("该医生当前不可预约");
        }

        LocalDateTime startTime = dto.getStartTime();
        LocalDateTime endTime = dto.getEndTime();
        LocalDateTime appointmentDate = null;
        
        if (startTime == null && dto.getAppointmentDate() != null && dto.getAppointmentTime() != null) {
            LocalDate date = LocalDate.parse(dto.getAppointmentDate());
            LocalTime time = LocalTime.parse(dto.getAppointmentTime());
            startTime = LocalDateTime.of(date, time);
            endTime = startTime.plusMinutes(30);
            appointmentDate = startTime;
        } else if (startTime != null && endTime == null) {
            endTime = startTime.plusMinutes(30);
            appointmentDate = startTime;
        } else if (startTime != null) {
            appointmentDate = startTime;
        }
        
        if (startTime == null || endTime == null) {
            throw new RuntimeException("请选择预约时间");
        }
        
        if (dto.getTimeSlotType() == null) {
            int hour = startTime.getHour();
            dto.setTimeSlotType(hour < 12 ? "MORNING" : "AFTERNOON");
        }
        
        if (dto.getDeptId() == null) {
            dto.setDeptId(doctor.getDeptId());
        }

        List<Appointment> conflicts = appointmentRepository.findByDoctorAndTimeRange(
                dto.getDoctorId(), startTime, endTime);
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("该时段已被预约，请选择其他时间");
        }

        Appointment appointment = new Appointment();
        appointment.setAppointmentNo(generateAppointmentNo());
        appointment.setPatientId(patientId);
        appointment.setDoctorId(dto.getDoctorId());
        appointment.setDeptId(dto.getDeptId());
        appointment.setAppointmentDate(appointmentDate != null ? appointmentDate : startTime);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setTimeSlotType(dto.getTimeSlotType());
        appointment.setTimeSlotNo(dto.getTimeSlotNo());
        appointment.setFee(dto.getFee() != null ? dto.getFee() : doctor.getRegistrationFee());
        appointment.setPaymentStatus(0);
        appointment.setStatus("PENDING");
        appointment.setChiefComplaint(dto.getChiefComplaint());
        appointment.setRemark(dto.getRemark());
        appointment.setQueueNo(generateQueueNo(dto.getDoctorId(), startTime));
        appointment.setRoomNo("请查看详情");

        appointment = appointmentRepository.save(appointment);

        NotificationSetting setting = notificationSettingRepository.findByUserId(patientId)
                .orElse(null);
        if (setting == null || setting.getAppointmentSuccess() == 1) {
            User patient = userRepository.findById(patientId).orElse(null);
            String content = String.format("您好，您已成功预约%s医生%s的号源，请准时就诊。",
                    doctor.getDoctorName(),
                    startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            messageService.createMessage(
                    patientId,
                    "预约成功通知",
                    content,
                    "APPOINTMENT_SUCCESS",
                    "APPOINTMENT",
                    appointment.getId()
            );
        }

        return appointment;
    }

    /**
     * 取消预约
     * 
     * @param appointmentId 预约ID
     * @param userId 用户ID
     * @param reason 取消原因
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelAppointment(Long appointmentId, Long userId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("预约不存在"));

        if (!appointment.getPatientId().equals(userId)) {
            throw new RuntimeException("无权取消他人预约");
        }

        if (!"PENDING".equals(appointment.getStatus()) && !"CONFIRMED".equals(appointment.getStatus())) {
            throw new RuntimeException("当前状态不允许取消预约");
        }

        appointment.setStatus("CANCELLED");
        appointment.setCancelReason(reason);
        appointment.setCancelTime(LocalDateTime.now());
        appointmentRepository.save(appointment);
    }

    /**
     * 获取患者的预约列表
     * 
     * @param patientId 患者ID
     * @return 预约列表
     */
    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByCreateTimeDesc(patientId);
    }

    /**
     * 获取医生的预约列表
     * 
     * @param doctorId 医生ID
     * @return 预约列表
     */
    public List<Appointment> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByStartTimeAsc(doctorId);
    }

    /**
     * 根据ID获取预约详情
     * 
     * @param id 预约ID
     * @return 预约详情
     */
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预约不存在"));
    }

    /**
     * 模拟支付
     * 
     * @param appointmentId 预约ID
     * @param paymentMethod 支付方式
     */
    @Transactional(rollbackFor = Exception.class)
    public void payAppointment(Long appointmentId, String paymentMethod) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("预约不存在"));

        if (appointment.getPaymentStatus() != 0) {
            throw new RuntimeException("该预约已支付");
        }

        appointment.setPaymentStatus(1);
        appointment.setPaymentMethod(paymentMethod);
        appointment.setPaymentTime(LocalDateTime.now());
        appointment.setPaymentNo("PAY" + UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        appointmentRepository.save(appointment);
    }

    /**
     * 生成预约编号
     */
    private String generateAppointmentNo() {
        return "APT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4);
    }

    /**
     * 生成排队号
     */
    private Integer generateQueueNo(Long doctorId, LocalDateTime dateTime) {
        LocalDateTime startOfDay = dateTime.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = dateTime.withHour(23).withMinute(59).withSecond(59);
        List<Appointment> appointments = appointmentRepository.findByDoctorAndTimeRange(doctorId, startOfDay, endOfDay);
        return appointments.size() + 1;
    }

    /**
     * 获取患者的预约列表（返回VO）
     * 
     * @param patientId 患者ID
     * @return 预约VO列表
     */
    public List<AppointmentVO> getPatientAppointmentVOs(Long patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientIdOrderByCreateTimeDesc(patientId);
        return appointments.stream().map(this::convertToVO).collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取预约详情VO
     * 
     * @param id 预约ID
     * @return 预约VO
     */
    public AppointmentVO getAppointmentVOById(Long id) {
        Appointment appointment = getAppointmentById(id);
        return convertToVO(appointment);
    }

    /**
     * 将Appointment转换为AppointmentVO
     */
    private AppointmentVO convertToVO(Appointment appointment) {
        AppointmentVO vo = new AppointmentVO();
        vo.setId(appointment.getId());
        vo.setAppointmentNo(appointment.getAppointmentNo());
        vo.setPatientId(appointment.getPatientId());
        vo.setDoctorId(appointment.getDoctorId());
        vo.setDeptId(appointment.getDeptId());
        
        vo.setAppointmentDateTime(appointment.getAppointmentDate());
        if (appointment.getStartTime() != null) {
            vo.setAppointmentDate(appointment.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            vo.setAppointmentTime(appointment.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        
        vo.setTimeSlotType(appointment.getTimeSlotType());
        vo.setTimeSlotNo(appointment.getTimeSlotNo());
        vo.setFee(appointment.getFee());
        vo.setPaymentStatus(appointment.getPaymentStatus());
        vo.setPaymentMethod(appointment.getPaymentMethod());
        vo.setPaymentTime(appointment.getPaymentTime());
        vo.setPaymentNo(appointment.getPaymentNo());
        
        vo.setRoomNo(appointment.getRoomNo());
        vo.setQueueNo(appointment.getQueueNo());
        vo.setChiefComplaint(appointment.getChiefComplaint());
        vo.setRemark(appointment.getRemark());
        vo.setCreateTime(appointment.getCreateTime());
        
        String status = appointment.getStatus();
        if ("PENDING".equals(status) && appointment.getPaymentStatus() == null) {
            vo.setStatus("PENDING");
        } else if ("PENDING".equals(status) && appointment.getPaymentStatus() != null && appointment.getPaymentStatus() == 1) {
            vo.setStatus("PAID");
        } else if ("CONFIRMED".equals(status)) {
            vo.setStatus("PAID");
        } else {
            vo.setStatus(status);
        }
        
        Doctor doctor = doctorRepository.findById(appointment.getDoctorId()).orElse(null);
        if (doctor != null) {
            vo.setDoctorName(doctor.getDoctorName());
            vo.setDoctorTitle(doctor.getTitle());
        }
        
        Department dept = departmentRepository.findById(appointment.getDeptId()).orElse(null);
        if (dept != null) {
            vo.setDepartmentName(dept.getDeptName());
        }
        
        User patient = userRepository.findById(appointment.getPatientId()).orElse(null);
        if (patient != null) {
            vo.setPatientName(patient.getName());
        }
        
        return vo;
    }
}
