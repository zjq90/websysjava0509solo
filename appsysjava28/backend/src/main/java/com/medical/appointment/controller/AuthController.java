package com.medical.appointment.controller;

import com.medical.appointment.common.ApiResponse;
import com.medical.appointment.entity.SmsCode;
import com.medical.appointment.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "认证接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @ApiOperation("发送验证码")
    @PostMapping("/sms-code")
    public ApiResponse<Map<String, String>> sendSmsCode(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String typeStr = request.getOrDefault("type", "LOGIN");
        
        if (phone == null || phone.isEmpty()) {
            return ApiResponse.badRequest("手机号不能为空");
        }

        SmsCode.CodeType type;
        try {
            type = SmsCode.CodeType.valueOf(typeStr.toUpperCase());
        } catch (Exception e) {
            type = SmsCode.CodeType.LOGIN;
        }

        String code = userService.generateSmsCode(phone, type);
        
        Map<String, String> result = new HashMap<>();
        result.put("phone", phone);
        result.put("code", code);
        
        return ApiResponse.success("验证码发送成功（测试环境，验证码为：" + code + "）", result);
    }

    @ApiOperation("手机号验证码登录")
    @PostMapping("/login/sms")
    public ApiResponse<Map<String, Object>> loginWithSms(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String code = request.get("code");
        
        if (phone == null || phone.isEmpty()) {
            return ApiResponse.badRequest("手机号不能为空");
        }
        if (code == null || code.isEmpty()) {
            return ApiResponse.badRequest("验证码不能为空");
        }

        try {
            Map<String, Object> result = userService.loginWithSms(phone, code);
            return ApiResponse.success("登录成功", result);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @ApiOperation("微信授权登录")
    @PostMapping("/login/wechat")
    public ApiResponse<Map<String, Object>> loginWithWechat(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String nickname = request.get("nickname");
        String avatar = request.get("avatar");
        
        if (code == null || code.isEmpty()) {
            return ApiResponse.badRequest("授权码不能为空");
        }

        try {
            Map<String, Object> result = userService.loginWithWechat(code, nickname, avatar);
            return ApiResponse.success("微信登录成功", result);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
