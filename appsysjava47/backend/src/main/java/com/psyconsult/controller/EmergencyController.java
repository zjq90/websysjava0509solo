package com.psyconsult.controller;

import com.psyconsult.dto.ApiResponse;
import com.psyconsult.entity.EmergencyContact;
import com.psyconsult.service.EmergencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency")
@Tag(name = "紧急求助接口", description = "紧急求助热线、医疗机构查询接口")
public class EmergencyController {

    private final EmergencyService emergencyService;

    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    @GetMapping("/contacts")
    @Operation(summary = "获取所有紧急联系人", description = "获取所有可用的紧急求助联系方式")
    public ApiResponse<List<EmergencyContact>> getAllEmergencyContacts() {
        return ApiResponse.success(emergencyService.getAllEmergencyContacts());
    }

    @GetMapping("/hotlines")
    @Operation(summary = "获取危机干预热线", description = "获取24小时危机干预热线列表")
    public ApiResponse<List<EmergencyContact>> getHotlines() {
        return ApiResponse.success(emergencyService.getHotlines());
    }

    @GetMapping("/contacts/{id}")
    @Operation(summary = "获取紧急联系人详情", description = "根据ID获取紧急联系人详细信息")
    public ApiResponse<EmergencyContact> getEmergencyContactById(@PathVariable Long id) {
        return ApiResponse.success(emergencyService.getEmergencyContactById(id));
    }
}
