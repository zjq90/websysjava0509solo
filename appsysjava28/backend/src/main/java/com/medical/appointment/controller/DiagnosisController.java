package com.medical.appointment.controller;

import com.medical.appointment.common.ApiResponse;
import com.medical.appointment.service.SymptomDiagnosisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "智能导诊接口")
@RestController
@RequestMapping("/diagnosis")
public class DiagnosisController {

    @Autowired
    private SymptomDiagnosisService symptomDiagnosisService;

    @ApiOperation("症状分析")
    @PostMapping("/analyze")
    public ApiResponse<Map<String, Object>> analyzeSymptoms(@RequestBody Map<String, String> requestBody) {
        String symptoms = requestBody.get("symptoms");
        if (symptoms == null || symptoms.isEmpty()) {
            return ApiResponse.badRequest("请输入症状描述");
        }
        
        Map<String, Object> result = symptomDiagnosisService.analyzeSymptoms(symptoms);
        return ApiResponse.success(result);
    }

    @ApiOperation("获取热门症状")
    @GetMapping("/hot-symptoms")
    public ApiResponse<List<String>> getHotSymptoms() {
        List<String> symptoms = symptomDiagnosisService.getHotSymptoms();
        return ApiResponse.success(symptoms);
    }
}
