package com.medical.appointment.controller;

import com.medical.appointment.common.ApiResponse;
import com.medical.appointment.entity.User;
import com.medical.appointment.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("获取当前用户信息")
    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.unauthorized("未登录");
        }
        
        User user = userService.findById(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("phone", user.getPhone());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        result.put("role", user.getRole().name());
        result.put("elderlyMode", user.getElderlyMode());
        result.put("status", user.getStatus().name());
        
        return ApiResponse.success(result);
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/me")
    public ApiResponse<Map<String, Object>> updateProfile(@RequestBody Map<String, String> requestBody,
                                                          HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String nickname = requestBody.get("nickname");
        String avatar = requestBody.get("avatar");
        
        User user = userService.updateProfile(userId, nickname, avatar);
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        
        return ApiResponse.success("更新成功", result);
    }

    @ApiOperation("切换长辈模式")
    @PostMapping("/elderly-mode")
    public ApiResponse<String> toggleElderlyMode(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.toggleElderlyMode(userId);
        
        User user = userService.findById(userId);
        return ApiResponse.success(user.getElderlyMode() ? "已开启长辈模式" : "已关闭长辈模式", 
                                   user.getElderlyMode() ? "ON" : "OFF");
    }
}
