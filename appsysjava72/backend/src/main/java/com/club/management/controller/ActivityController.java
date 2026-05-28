package com.club.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.common.Result;
import com.club.management.entity.ActivitySignIn;
import com.club.management.entity.ClubActivity;
import com.club.management.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 活动控制器
 *
 * @author club-management
 * @since 2024-01-01
 */
@Api(tags = "活动管理")
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 分页查询活动列表
     */
    @ApiOperation("分页查询活动列表")
    @GetMapping("/page")
    public Result<PageResult<ClubActivity>> getActivityPage(
            @ApiParam(value = "页码", defaultValue = "1")
            @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页大小", defaultValue = "10")
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("社团ID")
            @RequestParam(required = false) Long clubId,
            @ApiParam("活动类型")
            @RequestParam(required = false) String activityType,
            @ApiParam("活动状态 0未开始 1进行中 2已结束")
            @RequestParam(required = false) Integer status,
            @ApiParam("时间范围 today/week/month")
            @RequestParam(required = false) String timeRange) {
        Page<ClubActivity> page = new Page<>(pageNum, pageSize);
        PageResult<ClubActivity> pageResult = activityService.getActivityPage(page, clubId, activityType, status, timeRange);
        return Result.success(pageResult);
    }

    /**
     * 获取活动详情
     */
    @ApiOperation("获取活动详情")
    @GetMapping("/{id}")
    public Result<ClubActivity> getActivityDetail(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long id) {
        ClubActivity activity = activityService.getActivityDetail(id);
        return Result.success(activity);
    }

    /**
     * 报名活动
     */
    @ApiOperation("报名活动")
    @PostMapping("/signup/{activityId}")
    public Result<Void> signUpActivity(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long activityId,
            @ApiParam("备注")
            @RequestParam(required = false) String remark) {
        activityService.signUpActivity(activityId, remark);
        return Result.success("报名成功", null);
    }

    /**
     * 取消报名
     */
    @ApiOperation("取消报名")
    @PostMapping("/cancel-signup/{activityId}")
    public Result<Void> cancelSignUp(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long activityId) {
        activityService.cancelSignUp(activityId);
        return Result.success("取消报名成功", null);
    }

    /**
     * 检查是否已报名
     */
    @ApiOperation("检查是否已报名")
    @GetMapping("/check-signup/{activityId}")
    public Result<Boolean> isSignedUp(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long activityId) {
        boolean signedUp = activityService.isSignedUp(activityId);
        return Result.success(signedUp);
    }

    /**
     * 活动签到
     */
    @ApiOperation("活动签到")
    @PostMapping("/signin/{activityId}")
    public Result<Void> signInActivity(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long activityId,
            @ApiParam("签到类型 1扫码 2手动")
            @RequestParam(required = false, defaultValue = "1") Integer signInType,
            @ApiParam("签到地点")
            @RequestParam(required = false) String location,
            @ApiParam("是否离线签到")
            @RequestParam(required = false, defaultValue = "false") Boolean isOffline) {
        activityService.signInActivity(activityId, signInType, location, isOffline);
        return Result.success("签到成功", null);
    }

    /**
     * 同步离线签到数据
     */
    @ApiOperation("同步离线签到数据")
    @PostMapping("/sync-offline-signin")
    public Result<Void> syncOfflineSignIn(@RequestBody List<ActivitySignIn> signInList) {
        activityService.syncOfflineSignIn(signInList);
        return Result.success("同步成功", null);
    }

    /**
     * 检查是否已签到
     */
    @ApiOperation("检查是否已签到")
    @GetMapping("/check-signin/{activityId}")
    public Result<Boolean> isSignedIn(
            @ApiParam(value = "活动ID", required = true)
            @PathVariable Long activityId) {
        boolean signedIn = activityService.isSignedIn(activityId);
        return Result.success(signedIn);
    }

    /**
     * 获取我报名的活动列表
     */
    @ApiOperation("获取我报名的活动列表")
    @GetMapping("/my-activities")
    public Result<List<ClubActivity>> getMyActivities() {
        List<ClubActivity> activities = activityService.getMyActivities();
        return Result.success(activities);
    }

    /**
     * 获取我的签到记录
     */
    @ApiOperation("获取我的签到记录")
    @GetMapping("/my-signin-records")
    public Result<List<ActivitySignIn>> getMySignInRecords(
            @ApiParam("活动ID")
            @RequestParam(required = false) Long activityId) {
        List<ActivitySignIn> records = activityService.getMySignInRecords(activityId);
        return Result.success(records);
    }
}
