package com.club.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.club.management.common.PageResult;
import com.club.management.common.ResultCode;
import com.club.management.entity.ActivitySignIn;
import com.club.management.entity.ActivitySignUp;
import com.club.management.entity.ClubActivity;
import com.club.management.exception.BusinessException;
import com.club.management.mapper.ActivitySignInMapper;
import com.club.management.mapper.ActivitySignUpMapper;
import com.club.management.mapper.ClubActivityMapper;
import com.club.management.service.ActivityService;
import com.club.management.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动服务实现类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ClubActivityMapper clubActivityMapper;

    @Autowired
    private ActivitySignUpMapper activitySignUpMapper;

    @Autowired
    private ActivitySignInMapper activitySignInMapper;

    @Override
    public PageResult<ClubActivity> getActivityPage(Page<ClubActivity> page, Long clubId, String activityType, Integer status, String timeRange) {
        LambdaQueryWrapper<ClubActivity> wrapper = new LambdaQueryWrapper<>();

        if (clubId != null) {
            wrapper.eq(ClubActivity::getClubId, clubId);
        }

        if (StringUtils.hasText(activityType)) {
            wrapper.eq(ClubActivity::getActivityType, activityType);
        }

        if (status != null) {
            wrapper.eq(ClubActivity::getStatus, status);
        }

        if (StringUtils.hasText(timeRange)) {
            LocalDateTime now = LocalDateTime.now();
            switch (timeRange) {
                case "today":
                    wrapper.ge(ClubActivity::getStartTime, now.toLocalDate().atStartOfDay())
                            .lt(ClubActivity::getStartTime, now.toLocalDate().plusDays(1).atStartOfDay());
                    break;
                case "week":
                    wrapper.ge(ClubActivity::getStartTime, now.minusDays(7));
                    break;
                case "month":
                    wrapper.ge(ClubActivity::getStartTime, now.minusDays(30));
                    break;
                default:
                    break;
            }
        }

        wrapper.ne(ClubActivity::getStatus, 3);
        wrapper.orderByAsc(ClubActivity::getStartTime);

        Page<ClubActivity> activityPage = clubActivityMapper.selectPage(page, wrapper);

        log.debug("分页查询活动列表, 社团ID: {}, 类型: {}, 状态: {}, 总数: {}", clubId, activityType, status, activityPage.getTotal());

        return new PageResult<>(activityPage.getTotal(), activityPage.getCurrent(), activityPage.getSize(), activityPage.getRecords());
    }

    @Override
    public ClubActivity getActivityDetail(Long id) {
        ClubActivity activity = clubActivityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException(ResultCode.ACTIVITY_NOT_EXIST);
        }

        log.debug("获取活动详情, 活动ID: {}", id);

        return activity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signUpActivity(Long activityId, String remark) {
        Long userId = UserContext.getUserId();

        ClubActivity activity = clubActivityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException(ResultCode.ACTIVITY_NOT_EXIST);
        }

        if (activity.getStatus() == 3) {
            throw new BusinessException(ResultCode.ACTIVITY_ENDED);
        }

        LocalDateTime now = LocalDateTime.now();
        if (activity.getSignUpEndTime() != null && now.isAfter(activity.getSignUpEndTime())) {
            throw new BusinessException("报名已结束");
        }

        if (isSignedUp(activityId)) {
            throw new BusinessException(ResultCode.ALREADY_SIGNED_UP);
        }

        if (activity.getMaxParticipants() > 0 && activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            throw new BusinessException(ResultCode.ACTIVITY_FULL);
        }

        ActivitySignUp signUp = new ActivitySignUp();
        signUp.setActivityId(activityId);
        signUp.setUserId(userId);
        signUp.setSignUpTime(now);
        signUp.setStatus(1);
        signUp.setRemark(remark);
        activitySignUpMapper.insert(signUp);

        activity.setCurrentParticipants(activity.getCurrentParticipants() + 1);
        clubActivityMapper.updateById(activity);

        log.info("活动报名成功, 用户ID: {}, 活动ID: {}", userId, activityId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelSignUp(Long activityId) {
        Long userId = UserContext.getUserId();

        if (!isSignedUp(activityId)) {
            throw new BusinessException(ResultCode.NOT_SIGNED_UP);
        }

        LambdaQueryWrapper<ActivitySignUp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivitySignUp::getActivityId, activityId)
                .eq(ActivitySignUp::getUserId, userId)
                .eq(ActivitySignUp::getStatus, 1);
        ActivitySignUp signUp = activitySignUpMapper.selectOne(wrapper);

        if (signUp != null) {
            signUp.setStatus(0);
            activitySignUpMapper.updateById(signUp);

            ClubActivity activity = clubActivityMapper.selectById(activityId);
            if (activity != null && activity.getCurrentParticipants() > 0) {
                activity.setCurrentParticipants(activity.getCurrentParticipants() - 1);
                clubActivityMapper.updateById(activity);
            }
        }

        log.info("取消活动报名成功, 用户ID: {}, 活动ID: {}", userId, activityId);
    }

    @Override
    public boolean isSignedUp(Long activityId) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<ActivitySignUp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivitySignUp::getActivityId, activityId)
                .eq(ActivitySignUp::getUserId, userId)
                .eq(ActivitySignUp::getStatus, 1);

        return activitySignUpMapper.selectCount(wrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void signInActivity(Long activityId, Integer signInType, String location, boolean isOffline) {
        Long userId = UserContext.getUserId();

        ClubActivity activity = clubActivityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException(ResultCode.ACTIVITY_NOT_EXIST);
        }

        if (activity.getNeedSignIn() == 0) {
            throw new BusinessException("该活动不需要签到");
        }

        if (isSignedIn(activityId)) {
            throw new BusinessException(ResultCode.ALREADY_SIGNED_IN);
        }

        if (!isOffline) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(activity.getStartTime().minusMinutes(30)) || now.isAfter(activity.getEndTime())) {
                throw new BusinessException(ResultCode.SIGN_IN_TIME_ERROR);
            }
        }

        ActivitySignIn signIn = new ActivitySignIn();
        signIn.setActivityId(activityId);
        signIn.setUserId(userId);
        signIn.setSignInTime(LocalDateTime.now());
        signIn.setSignInType(signInType != null ? signInType : 1);
        signIn.setIsOffline(isOffline ? 1 : 0);
        signIn.setSyncStatus(isOffline ? 0 : 1);
        signIn.setLocation(location);
        activitySignInMapper.insert(signIn);

        log.info("活动签到成功, 用户ID: {}, 活动ID: {}, 离线签到: {}", userId, activityId, isOffline);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncOfflineSignIn(List<ActivitySignIn> signInList) {
        Long userId = UserContext.getUserId();

        for (ActivitySignIn signIn : signInList) {
            if (isSignedIn(signIn.getActivityId())) {
                continue;
            }

            signIn.setUserId(userId);
            signIn.setSyncStatus(1);
            signIn.setIsOffline(1);
            activitySignInMapper.insert(signIn);
        }

        log.info("同步离线签到数据成功, 用户ID: {}, 数量: {}", userId, signInList.size());
    }

    @Override
    public boolean isSignedIn(Long activityId) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<ActivitySignIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivitySignIn::getActivityId, activityId)
                .eq(ActivitySignIn::getUserId, userId);

        return activitySignInMapper.selectCount(wrapper) > 0;
    }

    @Override
    public List<ClubActivity> getMyActivities() {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<ActivitySignUp> signUpWrapper = new LambdaQueryWrapper<>();
        signUpWrapper.eq(ActivitySignUp::getUserId, userId)
                .eq(ActivitySignUp::getStatus, 1);
        List<ActivitySignUp> signUps = activitySignUpMapper.selectList(signUpWrapper);

        List<Long> activityIds = signUps.stream()
                .map(ActivitySignUp::getActivityId)
                .collect(Collectors.toList());

        if (activityIds.isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<ClubActivity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ClubActivity::getId, activityIds)
                .ne(ClubActivity::getStatus, 3)
                .orderByAsc(ClubActivity::getStartTime);

        List<ClubActivity> activities = clubActivityMapper.selectList(wrapper);

        log.debug("获取我报名的活动列表, 用户ID: {}, 数量: {}", userId, activities.size());

        return activities;
    }

    @Override
    public List<ActivitySignIn> getMySignInRecords(Long activityId) {
        Long userId = UserContext.getUserId();

        LambdaQueryWrapper<ActivitySignIn> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivitySignIn::getUserId, userId);
        if (activityId != null) {
            wrapper.eq(ActivitySignIn::getActivityId, activityId);
        }
        wrapper.orderByDesc(ActivitySignIn::getSignInTime);

        List<ActivitySignIn> records = activitySignInMapper.selectList(wrapper);

        log.debug("获取我的签到记录, 用户ID: {}, 活动ID: {}, 数量: {}", userId, activityId, records.size());

        return records;
    }
}
