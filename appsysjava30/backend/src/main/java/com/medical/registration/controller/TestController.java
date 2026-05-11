package com.medical.registration.controller;

import com.medical.registration.common.Result;
import com.medical.registration.dto.LoginDTO;
import com.medical.registration.dto.LoginVO;
import com.medical.registration.service.AuthService;
import com.medical.registration.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "测试辅助", description = "功能测试辅助接口")
@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private RegistrationService registrationService;
    
    @Operation(summary = "获取测试账号Token")
    @GetMapping("/token")
    public Result<LoginVO> getTestToken() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("zhangshan");
        dto.setPassword("123456");
        return Result.success(authService.login(dto));
    }
    
    @Operation(summary = "模拟退费完成（测试用）")
    @PostMapping("/refund/{registrationNo}")
    public Result<Void> simulateRefund(@PathVariable String registrationNo) {
        registrationService.simulateRefundComplete(registrationNo);
        return Result.success();
    }
    
    @Operation(summary = "模拟就诊完成（测试用）")
    @PostMapping("/visit-complete/{registrationNo}")
    public Result<Void> simulateVisitComplete(@PathVariable String registrationNo) {
        registrationService.simulateVisitComplete(registrationNo);
        return Result.success();
    }
    
    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("系统运行正常");
    }
}
