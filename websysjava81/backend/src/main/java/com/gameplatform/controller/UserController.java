package com.gameplatform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplatform.common.Result;
import com.gameplatform.dto.BanUserDTO;
import com.gameplatform.dto.UserQueryDTO;
import com.gameplatform.entity.BanRecord;
import com.gameplatform.service.UserService;
import com.gameplatform.vo.UserDetailVO;
import com.gameplatform.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/list")
    public Result<Page<UserVO>> getUserList(@RequestBody UserQueryDTO query) {
        return Result.success(userService.getUserList(query));
    }

    @GetMapping("/detail/{userId}")
    public Result<UserDetailVO> getUserDetail(@PathVariable String userId) {
        UserDetailVO detail = userService.getUserDetail(userId);
        if (detail == null) {
            return Result.error("用户不存在");
        }
        return Result.success(detail);
    }

    @PostMapping("/ban")
    public Result<Void> banUser(@RequestBody BanUserDTO dto) {
        userService.banUser(dto);
        return Result.success();
    }

    @PostMapping("/unban/{banRecordId}")
    public Result<Void> unbanUser(@PathVariable Long banRecordId) {
        userService.unbanUser(banRecordId);
        return Result.success();
    }

    @PostMapping("/batch-ban")
    public Result<Void> batchBanUsers(@RequestBody List<BanUserDTO> list) {
        userService.batchBanUsers(list);
        return Result.success();
    }

    @GetMapping("/ban-records/{userId}")
    public Result<List<BanRecord>> getBanRecords(@PathVariable String userId) {
        return Result.success(userService.getBanRecords(userId));
    }
}
