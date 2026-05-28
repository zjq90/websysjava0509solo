package com.club.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.entity.ActivitySignIn;
import com.club.management.entity.ClubActivity;

import java.util.List;

/**
 * 活动服务接口
 *
 * @author club-management
 * @since 2024-01-01
 */
public interface ActivityService {

    /**
     * 分页查询活动列表
     */
    PageResult<ClubActivity> getActivityPage(Page<ClubActivity> page, Long clubId, String activityType, Integer status, String timeRange);

    /**
     * 获取活动详情
     */
    ClubActivity getActivityDetail(Long id);

    /**
     * 报名活动
     */
    void signUpActivity(Long activityId, String remark);

    /**
     * 取消报名
     */
    void cancelSignUp(Long activityId);

    /**
     * 检查是否已报名
     */
    boolean isSignedUp(Long activityId);

    /**
     * 活动签到
     */
    void signInActivity(Long activityId, Integer signInType, String location, boolean isOffline);

    /**
     * 同步离线签到数据
     */
    void syncOfflineSignIn(List<ActivitySignIn> signInList);

    /**
     * 检查是否已签到
     */
    boolean isSignedIn(Long activityId);

    /**
     * 获取我报名的活动列表
     */
    List<ClubActivity> getMyActivities();

    /**
     * 获取我的签到记录
     */
    List<ActivitySignIn> getMySignInRecords(Long activityId);
}
