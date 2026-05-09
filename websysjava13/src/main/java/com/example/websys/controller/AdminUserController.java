package com.example.websys.controller;

import com.example.websys.dto.Result;
import com.example.websys.dto.user.AdminUserCreateDTO;
import com.example.websys.dto.user.AdminUserUpdateDTO;
import com.example.websys.entity.AdminUser;
import com.example.websys.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 管理员用户REST API控制器
 * 提供用户管理和登录接口
 * 新增功能：完整的前后端表单校验
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Validated
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        Optional<AdminUser> userOpt = adminUserService.login(username, password);
        if (userOpt.isPresent()) {
            AdminUser user = userOpt.get();
            Map<String, Object> result = new HashMap<>();
            result.put("userId", user.getId());
            result.put("username", user.getUsername());
            result.put("realName", user.getRealName());
            result.put("role", user.getRole());
            return Result.success("登录成功", result);
        }

        return Result.error(401, "用户名或密码错误");
    }

    @GetMapping
    public Result<List<AdminUser>> getAll() {
        return Result.success(adminUserService.getAll());
    }

    @GetMapping("/{id}")
    public Result<AdminUser> getById(@PathVariable Long id) {
        Optional<AdminUser> user = adminUserService.findById(id);
        return user.map(Result::success).orElse(Result.error("用户不存在"));
    }

    @PostMapping
    public Result<AdminUser> save(@Valid @RequestBody AdminUserCreateDTO dto) {
        Optional<AdminUser> existing = adminUserService.findByUsername(dto.getUsername().trim());
        if (existing.isPresent()) {
            return Result.error("用户名已存在，请使用其他用户名");
        }

        AdminUser user = new AdminUser();
        user.setUsername(dto.getUsername().trim());
        user.setPassword(dto.getPassword());
        user.setRealName(dto.getRealName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone() != null ? dto.getPhone().trim() : null);
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());

        AdminUser saved = adminUserService.save(user);
        return Result.success("保存成功", saved);
    }

    @PutMapping
    public Result<AdminUser> update(@Valid @RequestBody AdminUserUpdateDTO dto) {
        AdminUser user = new AdminUser();
        user.setId(dto.getId());
        user.setPassword(dto.getPassword());
        user.setRealName(dto.getRealName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone() != null ? dto.getPhone().trim() : null);
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());

        AdminUser updated = adminUserService.update(user);
        if (updated != null) {
            return Result.success("更新成功", updated);
        }
        return Result.error("用户不存在");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminUserService.deleteById(id);
        return Result.success();
    }
}
