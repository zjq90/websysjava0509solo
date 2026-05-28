package com.club.management.task;

import com.club.management.entity.Activity;
import com.club.management.entity.enums.ActivityStatus;
import com.club.management.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动状态定时更新任务
 * 
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityStatusTask {

    private final ActivityRepository activityRepository;

    /**
     * 每分钟检查活动状态并更新
     * 检查报名是否开始、报名是否结束、活动是否开始、活动是否结束
     */
    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void updateActivityStatus() {
        LocalDateTime now = LocalDateTime.now();
        log.debug("开始更新活动状态，当前时间: {}", now);

        // 1. 检查需要开启报名的活动
        List<Activity> activitiesToOpen = activityRepository.findActivitiesToOpenRegistration(
                List.of(ActivityStatus.DRAFT, ActivityStatus.REGISTRATION_CLOSED),
                now);
        for (Activity activity : activitiesToOpen) {
            if (now.isAfter(activity.getRegistrationStartTime()) && 
                now.isBefore(activity.getRegistrationEndTime())) {
                activity.setStatus(ActivityStatus.REGISTRATION_OPEN);
                log.info("活动报名已开启: activityId={}, name={}", activity.getId(), activity.getName());
            }
        }

        // 2. 检查需要关闭报名的活动
        List<Activity> activitiesToCloseReg = activityRepository.findActivitiesToCloseRegistration(
                ActivityStatus.REGISTRATION_OPEN, now);
        for (Activity activity : activitiesToCloseReg) {
            activity.setStatus(ActivityStatus.REGISTRATION_CLOSED);
            log.info("活动报名已关闭: activityId={}, name={}", activity.getId(), activity.getName());
        }

        // 3. 检查需要开始的活动
        List<Activity> activitiesToStart = activityRepository.findActivitiesToStart(
                ActivityStatus.REGISTRATION_CLOSED, now);
        for (Activity activity : activitiesToStart) {
            activity.setStatus(ActivityStatus.ONGOING);
            log.info("活动已开始: activityId={}, name={}", activity.getId(), activity.getName());
        }

        // 4. 检查需要结束的活动
        List<Activity> activitiesToComplete = activityRepository.findActivitiesToComplete(
                ActivityStatus.ONGOING, now);
        for (Activity activity : activitiesToComplete) {
            activity.setStatus(ActivityStatus.COMPLETED);
            log.info("活动已结束: activityId={}, name={}", activity.getId(), activity.getName());
        }

        activityRepository.saveAll(activitiesToOpen);
        activityRepository.saveAll(activitiesToCloseReg);
        activityRepository.saveAll(activitiesToStart);
        activityRepository.saveAll(activitiesToComplete);

        log.debug("活动状态更新完成");
    }
}
