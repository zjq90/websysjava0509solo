package com.medical.appointment.controller;

import com.medical.appointment.common.ApiResponse;
import com.medical.appointment.entity.Patient;
import com.medical.appointment.service.PatientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Api(tags = "就诊人接口")
@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @ApiOperation("获取就诊人列表")
    @GetMapping
    public ApiResponse<List<Map<String, Object>>> getPatientList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<Map<String, Object>> patients = patientService.getPatientsByUserId(userId);
        return ApiResponse.success(patients);
    }

    @ApiOperation("获取就诊人详情")
    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> getPatientDetail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Map<String, Object> patient = patientService.getPatientById(userId, id);
        return ApiResponse.success(patient);
    }

    @ApiOperation("添加就诊人")
    @PostMapping
    public ApiResponse<Map<String, Object>> addPatient(@RequestBody Map<String, Object> requestBody,
                                                        HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        Patient patient = new Patient();
        patient.setRealName((String) requestBody.get("realName"));
        patient.setGender(Patient.Gender.valueOf(((String) requestBody.getOrDefault("gender", "MALE")).toUpperCase()));
        
        if (requestBody.get("birthDate") != null) {
            patient.setBirthDate(LocalDate.parse((String) requestBody.get("birthDate")));
        }
        patient.setPhone((String) requestBody.get("phone"));
        patient.setIdCardNumber((String) requestBody.get("idCardNumber"));
        patient.setRelation((String) requestBody.getOrDefault("relation", "本人"));

        try {
            Patient saved = patientService.addPatient(userId, patient);
            Map<String, Object> result = patientService.getPatientById(userId, saved.getId());
            return ApiResponse.success("添加成功", result);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @ApiOperation("更新就诊人")
    @PutMapping("/{id}")
    public ApiResponse<Map<String, Object>> updatePatient(@PathVariable Long id,
                                                           @RequestBody Map<String, Object> requestBody,
                                                           HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        Patient patient = new Patient();
        if (requestBody.containsKey("realName")) {
            patient.setRealName((String) requestBody.get("realName"));
        }
        if (requestBody.containsKey("gender")) {
            patient.setGender(Patient.Gender.valueOf(((String) requestBody.get("gender")).toUpperCase()));
        }
        if (requestBody.containsKey("birthDate")) {
            patient.setBirthDate(LocalDate.parse((String) requestBody.get("birthDate")));
        }
        if (requestBody.containsKey("phone")) {
            patient.setPhone((String) requestBody.get("phone"));
        }
        if (requestBody.containsKey("relation")) {
            patient.setRelation((String) requestBody.get("relation"));
        }

        try {
            patientService.updatePatient(userId, id, patient);
            Map<String, Object> result = patientService.getPatientById(userId, id);
            return ApiResponse.success("更新成功", result);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @ApiOperation("删除就诊人")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePatient(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        try {
            patientService.deletePatient(userId, id);
            return ApiResponse.success("删除成功", null);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @ApiOperation("实名认证")
    @PostMapping("/{id}/verify")
    public ApiResponse<String> verifyIdentity(@PathVariable Long id,
                                              @RequestBody Map<String, String> requestBody,
                                              HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String idCardNumber = requestBody.get("idCardNumber");
        String name = requestBody.get("name");
        
        if (idCardNumber == null || idCardNumber.isEmpty()) {
            return ApiResponse.badRequest("身份证号不能为空");
        }
        if (name == null || name.isEmpty()) {
            return ApiResponse.badRequest("姓名不能为空");
        }

        try {
            patientService.verifyIdentity(userId, id, idCardNumber, name);
            return ApiResponse.success("实名认证成功", null);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @ApiOperation("设为默认就诊人")
    @PostMapping("/{id}/primary")
    public ApiResponse<String> setPrimaryPatient(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        patientService.setPrimaryPatient(userId, id);
        return ApiResponse.success("设置成功", null);
    }
}
