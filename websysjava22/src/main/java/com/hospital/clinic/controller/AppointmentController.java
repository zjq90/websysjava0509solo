package com.hospital.clinic.controller;

import com.hospital.clinic.common.Result;
import com.hospital.clinic.dto.AppointmentRequest;
import com.hospital.clinic.entity.Appointment;
import com.hospital.clinic.entity.Department;
import com.hospital.clinic.entity.Doctor;
import com.hospital.clinic.entity.Patient;
import com.hospital.clinic.service.AppointmentService;
import com.hospital.clinic.service.DepartmentService;
import com.hospital.clinic.service.DoctorService;
import com.hospital.clinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 预约挂号Controller
 * 预约挂号相关API接口
 */
@RestController
@RequestMapping("/api/appointment")
@Tag(name = "预约挂号管理", description = "预约挂号管理相关接口")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DepartmentService departmentService;

    /**
     * 查询所有预约
     */
    @GetMapping
    @Operation(summary = "查询所有预约", description = "获取所有预约列表")
    public Result<List<Appointment>> findAll() {
        return Result.success(appointmentService.findAll());
    }

    /**
     * 根据ID查询预约
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询预约", description = "根据预约ID获取预约信息")
    public Result<Appointment> findById(@Parameter(description = "预约ID") @PathVariable Long id) {
        return Result.success(appointmentService.findById(id));
    }

    /**
     * 根据预约单号查询
     */
    @GetMapping("/no/{appointmentNo}")
    @Operation(summary = "根据单号查询预约", description = "根据预约单号获取预约信息")
    public Result<Appointment> findByAppointmentNo(@Parameter(description = "预约单号") @PathVariable String appointmentNo) {
        return Result.success(appointmentService.findByAppointmentNo(appointmentNo));
    }

    /**
     * 根据日期查询预约
     */
    @GetMapping("/date/{date}")
    @Operation(summary = "根据日期查询预约", description = "根据预约日期获取预约列表")
    public Result<List<Appointment>> findByAppointmentDate(
            @Parameter(description = "预约日期")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.success(appointmentService.findByAppointmentDate(date));
    }

    /**
     * 根据状态查询
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态查询预约", description = "根据状态获取预约列表")
    public Result<List<Appointment>> findByStatus(@Parameter(description = "状态") @PathVariable String status) {
        return Result.success(appointmentService.findByStatus(status));
    }

    /**
     * 创建预约
     */
    @PostMapping("/create")
    @Operation(summary = "创建预约", description = "根据患者姓名和医生ID创建预约")
    public Result<Appointment> createAppointment(@RequestBody AppointmentRequest request) {
        try {
            // 1. 查找或创建患者
            Patient patient = null;
            if (request.getPatientId() != null) {
                patient = patientService.findById(request.getPatientId());
            }
            if (patient == null && request.getPatientName() != null) {
                List<Patient> patients = patientService.findByName(request.getPatientName());
                if (patients != null && !patients.isEmpty()) {
                    patient = patients.get(0);
                } else {
                    // 创建新患者
                    patient = new Patient();
                    patient.setName(request.getPatientName());
                    patient = patientService.save(patient);
                }
            }
            if (patient == null) {
                return Result.error("患者信息不能为空");
            }

            // 2. 查找医生
            Doctor doctor = null;
            if (request.getDoctorId() != null) {
                doctor = doctorService.findById(request.getDoctorId());
            }
            if (doctor == null) {
                return Result.error("医生信息不能为空");
            }

            // 3. 获取科室
            Department department = doctor.getDepartment();
            if (department == null && request.getDepartmentId() != null) {
                department = departmentService.findById(request.getDepartmentId());
            }

            // 4. 创建预约
            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setDepartment(department);
            appointment.setAppointmentDate(request.getAppointmentDate() != null ? request.getAppointmentDate() : LocalDate.now());
            appointment.setAppointmentTime(request.getAppointmentTime());
            appointment.setConsultationFee(request.getConsultationFee() != null ? request.getConsultationFee() : new BigDecimal("50.00"));
            
            return Result.success(appointmentService.save(appointment));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("创建预约失败: " + e.getMessage());
        }
    }

    /**
     * 新增预约
     */
    @PostMapping
    @Operation(summary = "新增预约", description = "新增预约信息")
    public Result<Appointment> save(@RequestBody Appointment appointment) {
        return Result.success(appointmentService.save(appointment));
    }

    /**
     * 更新预约
     */
    @PutMapping
    @Operation(summary = "更新预约", description = "更新预约信息")
    public Result<Appointment> update(@RequestBody Appointment appointment) {
        return Result.success(appointmentService.update(appointment));
    }

    /**
     * 删除预约
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除预约", description = "根据ID删除预约信息")
    public Result<Void> deleteById(@Parameter(description = "预约ID") @PathVariable Long id) {
        appointmentService.deleteById(id);
        return Result.success();
    }

    /**
     * 叫号
     */
    @PostMapping("/call/{id}")
    @Operation(summary = "叫号", description = "呼叫指定患者")
    public Result<Appointment> callNumber(@Parameter(description = "预约ID") @PathVariable Long id) {
        return Result.success(appointmentService.callNumber(id));
    }

    /**
     * 完成就诊
     */
    @PostMapping("/complete/{id}")
    @Operation(summary = "完成就诊", description = "标记患者已完成就诊")
    public Result<Appointment> complete(@Parameter(description = "预约ID") @PathVariable Long id) {
        return Result.success(appointmentService.complete(id));
    }
}
