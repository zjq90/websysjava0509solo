package com.secondhand.controller;

import com.secondhand.common.Result;
import com.secondhand.entity.GroupBuyActivity;
import com.secondhand.entity.GroupBuyRecord;
import com.secondhand.entity.User;
import com.secondhand.service.GroupBuyService;
import com.secondhand.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 拼团控制器
 *
 * @author secondhand
 * @version 1.0.0
 */
@RestController
@RequestMapping("/groupbuy")
@Tag(name = "拼团管理", description = "拼团活动、拼团参与等接口")
public class GroupBuyController {

    @Autowired
    private GroupBuyService groupBuyService;

    @Autowired
    private UserService userService;

    @PostMapping("/activity/create")
    @Operation(summary = "创建拼团活动", description = "创建新的拼团活动")
    public Result<GroupBuyActivity> createActivity(@RequestHeader("Authorization") String token, @RequestBody GroupBuyActivity activity) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            GroupBuyActivity created = groupBuyService.createActivity(activity);
            return Result.success("创建成功", created);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/activity/list")
    @Operation(summary = "拼团活动列表", description = "获取所有有效拼团活动")
    public Result<List<GroupBuyActivity>> getActiveActivities() {
        List<GroupBuyActivity> activities = groupBuyService.getActiveActivities();
        return Result.success(activities);
    }

    @PostMapping("/join")
    @Operation(summary = "参与拼团", description = "参与或发起拼团")
    public Result<GroupBuyRecord> joinActivity(@RequestHeader("Authorization") String token, @RequestParam Long activityId, @RequestParam(required = false) Long groupRecordId) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        try {
            GroupBuyRecord record = groupBuyService.joinActivity(user.getId(), activityId, groupRecordId);
            return Result.success("参与成功", record);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/my")
    @Operation(summary = "我的拼团", description = "获取我的拼团记录")
    public Result<List<GroupBuyRecord>> getMyGroupRecords(@RequestHeader("Authorization") String token) {
        User user = userService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "未登录或登录已过期");
        }
        List<GroupBuyRecord> records = groupBuyService.getMyGroupRecords(user.getId());
        return Result.success(records);
    }

    @GetMapping("/members/{groupNo}")
    @Operation(summary = "拼团成员", description = "获取拼团成员列表")
    public Result<List<GroupBuyRecord>> getGroupMembers(@PathVariable String groupNo) {
        List<GroupBuyRecord> members = groupBuyService.getGroupMembers(groupNo);
        return Result.success(members);
    }

}
