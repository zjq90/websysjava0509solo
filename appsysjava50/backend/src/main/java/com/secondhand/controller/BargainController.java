package com.secondhand.controller;

import com.secondhand.common.Result;
import com.secondhand.entity.BargainActivity;
import com.secondhand.entity.BargainHelp;
import com.secondhand.entity.BargainRecord;
import com.secondhand.entity.User;
import com.secondhand.service.BargainService;
import com.secondhand.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 砍价控制器
 *
 * @author secondhand
 * @version 1.0.0
 */
@RestController
@RequestMapping("/bargain")
@Tag(name = "砍价管理", description = "砍价活动、砍价助力等接口")
public class BargainController {

    @Autowired
    private BargainService bargainService;

    @Autowired
    private UserService userService;

    @PostMapping("/activity/create")
    @Operation(summary = "创建砍价活动", description = "创建新的砍价活动")
    public Result<BargainActivity> createActivity(@RequestHeader("Authorization") String token, @RequestBody BargainActivity activity) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            BargainActivity created = bargainService.createActivity(activity);
            return Result.success("创建成功", created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/activity/list")
    @Operation(summary = "砍价活动列表", description = "获取所有有效砍价活动")
    public Result<List<BargainActivity>> getActiveActivities() {
        List<BargainActivity> activities = bargainService.getActiveActivities();
        return Result.success(activities);
    }

    @PostMapping("/start")
    @Operation(summary = "发起砍价", description = "发起砍价")
    public Result<BargainRecord> startBargain(@RequestHeader("Authorization") String token, @RequestParam Long activityId) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            BargainRecord record = bargainService.startBargain(user.getId(), activityId);
            return Result.success("发起成功", record);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/help")
    @Operation(summary = "帮砍一刀", description = "帮好友砍价")
    public Result<BargainHelp> helpBargain(@RequestHeader("Authorization") String token, @RequestParam Long recordId) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            BargainHelp help = bargainService.helpBargain(user.getId(), recordId);
            return Result.success("帮砍成功", help);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    @Operation(summary = "我的砍价", description = "获取我的砍价记录")
    public Result<List<BargainRecord>> getMyBargainRecords(@RequestHeader("Authorization") String token) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        List<BargainRecord> records = bargainService.getMyBargainRecords(user.getId());
        return Result.success(records);
    }

    @GetMapping("/detail/{recordId}")
    @Operation(summary = "砍价详情", description = "获取砍价助力记录")
    public Result<List<BargainHelp>> getBargainHelpRecords(@PathVariable Long recordId) {
        List<BargainHelp> helps = bargainService.getBargainHelpRecords(recordId);
        return Result.success(helps);
    }

    @GetMapping("/record/{recordId}")
    @Operation(summary = "砍价记录详情", description = "获取砍价记录详情")
    public Result<BargainRecord> getBargainRecord(@PathVariable Long recordId) {
        BargainRecord record = bargainService.getBargainRecord(recordId);
        if (record == null) {
            return Result.error("砍价记录不存在");
        }
        return Result.success(record);
    }

}
