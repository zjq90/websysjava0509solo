package com.broadband.controller;

import com.broadband.common.Result;
import com.broadband.service.AuthService;
import com.broadband.util.RSAUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 处理用户登录、注册等认证相关接口
 * 
 * @author broadband
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户登录、注册等认证相关接口")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "获取RSA公钥", description = "用于敏感数据加密传输")
    @GetMapping("/publicKey")
    public Result<String> getPublicKey() {
        return Result.success(RSAUtil.getPublicKey());
    }

    @Operation(summary = "发送手机验证码", description = "向指定手机号发送6位数字验证码，有效期5分钟")
    @PostMapping("/sendCode")
    public Result<String> sendSmsCode(@RequestParam String phone) {
        try {
            String code = authService.sendSmsCode(phone);
            return Result.success("验证码已发送", code);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "手机号验证码登录", description = "使用手机号和验证码进行登录")
    @PostMapping("/login/phone")
    public Result<Map<String, Object>> loginByPhone(@RequestParam String phone, @RequestParam String code) {
        try {
            Map<String, Object> result = authService.loginByPhone(phone, code);
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "微信登录", description = "使用微信OpenID进行登录")
    @PostMapping("/login/wechat")
    public Result<Map<String, Object>> loginByWechat(@RequestParam String wxOpenId,
                                                      @RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String avatar) {
        try {
            Map<String, Object> result = authService.loginByWechat(wxOpenId, name, avatar);
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "支付宝登录", description = "使用支付宝UserID进行登录")
    @PostMapping("/login/alipay")
    public Result<Map<String, Object>> loginByAlipay(@RequestParam String alipayUserId,
                                                      @RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String avatar) {
        try {
            Map<String, Object> result = authService.loginByAlipay(alipayUserId, name, avatar);
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "人脸识别登录", description = "使用人脸识别进行登录")
    @PostMapping("/login/face")
    public Result<Map<String, Object>> faceLogin(@RequestParam Long userId) {
        try {
            Map<String, Object> result = authService.faceLogin(userId);
            return Result.success("登录成功", result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
