package com.petclinic.controller;

import com.petclinic.dto.Result;
import com.petclinic.entity.HospitalRating;
import com.petclinic.entity.PetHospital;
import com.petclinic.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 宠物医院控制器
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/hospital")
@RequiredArgsConstructor
@Tag(name = "宠物医院", description = "宠物医院相关接口，显示用户评分")
public class HospitalController {

    private final HospitalService hospitalService;

    @GetMapping("/nearby")
    @Operation(summary = "获取附近医院", description = "根据经纬度获取附近的宠物医院")
    public Result<List<PetHospital>> getNearbyHospitals(
            @Parameter(description = "纬度") @RequestParam BigDecimal latitude,
            @Parameter(description = "经度") @RequestParam BigDecimal longitude,
            @Parameter(description = "搜索半径(公里)") @RequestParam(defaultValue = "5") double radius) {
        return hospitalService.getNearbyHospitals(latitude, longitude, radius);
    }

    @GetMapping("/{hospitalId}")
    @Operation(summary = "获取医院详情", description = "获取宠物医院的详细信息")
    public Result<PetHospital> getHospitalDetail(
            @Parameter(description = "医院ID") @PathVariable Long hospitalId) {
        return hospitalService.getHospitalDetail(hospitalId);
    }

    @PostMapping("/rating")
    @Operation(summary = "提交医院评分", description = "为宠物医院打分和评论，满分5分")
    public Result<HospitalRating> submitRating(@RequestBody HospitalRating rating) {
        return hospitalService.submitRating(rating);
    }

    @GetMapping("/ratings/{hospitalId}")
    @Operation(summary = "获取医院评分列表", description = "获取宠物医院的所有用户评分")
    public Result<List<HospitalRating>> getHospitalRatings(
            @Parameter(description = "医院ID") @PathVariable Long hospitalId) {
        return hospitalService.getHospitalRatings(hospitalId);
    }

    @GetMapping("/emergency")
    @Operation(summary = "获取急诊医院", description = "获取24小时急诊宠物医院")
    public Result<List<PetHospital>> getEmergencyHospitals() {
        return hospitalService.getEmergencyHospitals();
    }
}