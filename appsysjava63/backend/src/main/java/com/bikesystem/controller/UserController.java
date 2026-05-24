package com.bikesystem.controller;

import com.bikesystem.common.Result;
import com.bikesystem.dto.LoginRequest;
import com.bikesystem.dto.LoginResponse;
import com.bikesystem.dto.RealNameVerifyRequest;
import com.bikesystem.dto.RegisterRequest;
import com.bikesystem.entity.User;
import com.bikesystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 用户控制器
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户模块", description = "用户注册、登录、实名认证、押金等接口")
@Validated
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/send-sms-code")
    @Operation(summary = "发送短信验证码", description = "向指定手机号发送短信验证码")
    public Result<Map<String, Object>> sendSmsCode(
            @Parameter(description = "手机号", required = true) @RequestParam String phone) {
        return Result.success(userService.sendSmsCode(phone));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册账号")
    public Result<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(userService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "手机号密码登录")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @PostMapping("/third-party-login")
    @Operation(summary = "第三方登录", description = "微信/支付宝第三方账号登录")
    public Result<LoginResponse> thirdPartyLogin(
            @Parameter(description = "平台: WECHAT/ALIPAY", required = true) @RequestParam String platform,
            @Parameter(description = "第三方OpenID", required = true) @RequestParam String openid,
            @Parameter(description = "昵称") @RequestParam(required = false) String nickname,
            @Parameter(description = "头像URL") @RequestParam(required = false) String avatar) {
        return Result.success(userService.thirdPartyLogin(platform, openid, nickname, avatar));
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户信息")
    public Result<User> getUserInfo() {
        return Result.success(userService.getCurrentUser());
    }

    @PutMapping("/info")
    @Operation(summary = "更新用户信息", description = "更新用户昵称、头像等信息")
    public Result<User> updateUserInfo(@RequestBody User user) {
        user.setId(null);
        user.setPhone(null);
        user.setPassword(null);
        user.setIdCard(null);
        user.setCreditScore(null);
        user.setBalance(null);
        userService.updateById(user);
        return Result.success(userService.getCurrentUser());
    }

    @PostMapping("/real-name-verify")
    @Operation(summary = "实名认证", description = "身份证+人脸双验证")
    public Result<User> realNameVerify(@Valid @RequestBody RealNameVerifyRequest request) {
        return Result.success(userService.realNameVerify(request));
    }

    @PostMapping("/deposit/pay")
    @Operation(summary = "缴纳押金", description = "缴纳骑行押金")
    public Result<Map<String, Object>> payDeposit(
            @Parameter(description = "支付方式: WECHAT/ALIPAY/BALANCE") @RequestParam(defaultValue = "WECHAT") String paymentMethod) {
        return Result.success(userService.payDeposit(paymentMethod));
    }

    @PostMapping("/deposit/credit-exempt")
    @Operation(summary = "信用免押金", description = "芝麻信用分≥650可免押金")
    public Result<Map<String, Object>> creditExempt(
            @Parameter(description = "授权来源: ZHIMA") @RequestParam(defaultValue = "ZHIMA") String authSource,
            @Parameter(description = "信用分", required = true) @RequestParam Integer creditScore) {
        return Result.success(userService.creditExempt(authSource, creditScore));
    }

    @PostMapping("/deposit/refund")
    @Operation(summary = "退还押金", description = "申请退还押金")
    public Result<Map<String, Object>> refundDeposit() {
        return Result.success(userService.refundDeposit());
    }

    @PostMapping("/recharge")
    @Operation(summary = "账户充值", description = "充值账户余额")
    public Result<Map<String, Object>> recharge(
            @Parameter(description = "充值金额", required = true) @RequestParam BigDecimal amount,
            @Parameter(description = "支付方式") @RequestParam(defaultValue = "WECHAT") String paymentMethod) {
        return Result.success(userService.recharge(amount, paymentMethod));
    }
}
