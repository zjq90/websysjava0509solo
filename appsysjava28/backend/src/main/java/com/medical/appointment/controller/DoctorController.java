package com.medical.appointment.controller;

import com.medical.appointment.common.ApiResponse;
import com.medical.appointment.service.DoctorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "医生接口")
@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @ApiOperation("获取热门医生")
    @GetMapping("/popular")
    public ApiResponse<List<Map<String, Object>>> getPopularDoctors() {
        List<Map<String, Object>> doctors = doctorService.getPopularDoctors();
        return ApiResponse.success(doctors);
    }

    @ApiOperation("获取所有医生")
    @GetMapping
    public ApiResponse<List<Map<String, Object>>> getAllDoctors() {
        List<Map<String, Object>> doctors = doctorService.getAllDoctors();
        return ApiResponse.success(doctors);
    }

    @ApiOperation("获取医生详情")
    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> getDoctorDetail(@PathVariable Long id) {
        Map<String, Object> detail = doctorService.getDoctorDetail(id);
        return ApiResponse.success(detail);
    }

    @ApiOperation("搜索医生")
    @GetMapping("/search")
    public ApiResponse<List<Map<String, Object>>> searchDoctors(@RequestParam String keyword) {
        List<Map<String, Object>> doctors = doctorService.searchDoctors(keyword);
        return ApiResponse.success(doctors);
    }
}
