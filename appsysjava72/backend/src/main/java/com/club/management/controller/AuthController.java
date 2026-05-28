package com.club.management.controller;

import com.club.management.common.Result;
import com.club.management.dto.LoginDTO;
import com.club.management.dto.RegisterDTO;
import com.club.management.service.AuthService;
import com.club.management.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 认证控制器
 *
 * @author club-management
 * @since 2024-01-01
 */
@Api(tags = "认证管理")
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = authService.login(loginDTO);
        return Result.success("登录成功", loginVO);
    }

    /**
     * 用户注册
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<LoginVO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        LoginVO loginVO = authService.register(registerDTO);
        return Result.success("注册成功", loginVO);
    }

    /**
     * 发送验证码
     */
    @ApiOperation("发送验证码")
    @PostMapping("/send-code")
    public Result<Void> sendVerifyCode(
            @ApiParam(value = "手机号", required = true)
            @RequestParam @NotBlank(message = "手机号不能为空") String phone) {
        authService.sendVerifyCode(phone);
        return Result.success("验证码发送成功", null);
    }

    /**
     * 退出登录
     */
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        authService.logout();
        return Result.success("退出成功", null);
    }
}
