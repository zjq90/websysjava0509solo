package com.medical.appointment.controller;

import com.medical.appointment.common.ApiResponse;
import com.medical.appointment.service.DepartmentService;
import com.medical.appointment.service.DoctorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "科室接口")
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DoctorService doctorService;

    @ApiOperation("获取科室树")
    @GetMapping("/tree")
    public ApiResponse<List<Map<String, Object>>> getDepartmentTree() {
        List<Map<String, Object>> tree = departmentService.getDepartmentTree();
        return ApiResponse.success(tree);
    }

    @ApiOperation("获取科室详情")
    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> getDepartmentDetail(@PathVariable Long id) {
        Map<String, Object> detail = departmentService.getDepartmentDetail(id);
        return ApiResponse.success(detail);
    }

    @ApiOperation("获取科室下的医生")
    @GetMapping("/{id}/doctors")
    public ApiResponse<List<Map<String, Object>>> getDoctorsByDepartment(@PathVariable Long id) {
        List<Map<String, Object>> doctors = doctorService.getDoctorsByDepartment(id);
        return ApiResponse.success(doctors);
    }

    @ApiOperation("搜索科室")
    @GetMapping("/search")
    public ApiResponse<List<Map<String, Object>>> searchDepartments(@RequestParam String keyword) {
        List<Map<String, Object>> departments = departmentService.searchDepartments(keyword);
        return ApiResponse.success(departments);
    }
}
