package com.club.management.service;

import com.club.management.common.BusinessException;
import com.club.management.common.PageQuery;
import com.club.management.dto.ActivityDTO;
import com.club.management.entity.Activity;
import com.club.management.entity.enums.ActivityStatus;
import com.club.management.entity.enums.RegistrationStatus;
import com.club.management.repository.ActivityRepository;
import com.club.management.repository.RegistrationRepository;
import com.club.management.util.QrCodeUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 活动服务
 * 
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final RegistrationRepository registrationRepository;

    /**
     * 创建活动
     */
    @Transactional
    @CacheEvict(value = "activities", allEntries = true)
    public Activity createActivity(ActivityDTO dto, Long userId) {
        log.info("创建活动: name={}, clubId={}, userId={}", dto.getName(), dto.getClubId(), userId);

        validateActivityTime(dto);

        Activity activity = new Activity();
        activity.setClubId(dto.getClubId());
        activity.setName(dto.getName());
        activity.setStartTime(dto.getStartTime());
        activity.setEndTime(dto.getEndTime());
        activity.setLocation(dto.getLocation());
        activity.setQuota(dto.getQuota());
        activity.setRegistrationStartTime(dto.getRegistrationStartTime());
        activity.setRegistrationEndTime(dto.getRegistrationEndTime());
        activity.setRequirements(dto.getRequirements());
        activity.setDescription(dto.getDescription());
        activity.setPosterUrl(dto.getPosterUrl());
        activity.setRegistrationScope(dto.getRegistrationScope());
        activity.setNeedApproval(dto.getNeedApproval());
        activity.setSignInStartTime(dto.getSignInStartTime());
        activity.setSignInEndTime(dto.getSignInEndTime());
        activity.setOrganizerName(dto.getOrganizerName());
        activity.setOrganizerPhone(dto.getOrganizerPhone());
        activity.setCreateBy(userId);
        activity.setStatus(ActivityStatus.DRAFT);

        activity = activityRepository.save(activity);
        log.info("活动创建成功: activityId={}", activity.getId());
        return activity;
    }

    /**
     * 更新活动
     */
    @Transactional
    @CacheEvict(value = "activities", allEntries = true)
    public Activity updateActivity(Long id, ActivityDTO dto, Long userId) {
        log.info("更新活动: activityId={}, userId={}", id, userId);

        Activity activity = getActivityById(id);
        validateActivityTime(dto);

        activity.setName(dto.getName());
        activity.setStartTime(dto.getStartTime());
        activity.setEndTime(dto.getEndTime());
        activity.setLocation(dto.getLocation());
        activity.setQuota(dto.getQuota());
        activity.setRegistrationStartTime(dto.getRegistrationStartTime());
        activity.setRegistrationEndTime(dto.getRegistrationEndTime());
        activity.setRequirements(dto.getRequirements());
        activity.setDescription(dto.getDescription());
        activity.setPosterUrl(dto.getPosterUrl());
        activity.setRegistrationScope(dto.getRegistrationScope());
        activity.setNeedApproval(dto.getNeedApproval());
        activity.setSignInStartTime(dto.getSignInStartTime());
        activity.setSignInEndTime(dto.getSignInEndTime());
        activity.setOrganizerName(dto.getOrganizerName());
        activity.setOrganizerPhone(dto.getOrganizerPhone());
        activity.setUpdateBy(userId);

        activity = activityRepository.save(activity);
        log.info("活动更新成功: activityId={}", activity.getId());
        return activity;
    }

    /**
     * 发布活动
     */
    @Transactional
    @CacheEvict(value = "activities", allEntries = true)
    public Activity publishActivity(Long id, Long userId) {
        log.info("发布活动: activityId={}, userId={}", id, userId);

        Activity activity = getActivityById(id);

        if (activity.getStatus() != ActivityStatus.DRAFT) {
            throw new BusinessException("只有草稿状态的活动才能发布");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getRegistrationStartTime())) {
            activity.setStatus(ActivityStatus.DRAFT);
        } else if (now.isAfter(activity.getRegistrationEndTime())) {
            activity.setStatus(ActivityStatus.REGISTRATION_CLOSED);
        } else {
            activity.setStatus(ActivityStatus.REGISTRATION_OPEN);
        }
        activity.setUpdateBy(userId);

        activity = activityRepository.save(activity);
        log.info("活动发布成功: activityId={}, status={}", activity.getId(), activity.getStatus());
        return activity;
    }

    /**
     * 取消活动
     */
    @Transactional
    @CacheEvict(value = "activities", allEntries = true)
    public void cancelActivity(Long id, Long userId) {
        log.info("取消活动: activityId={}, userId={}", id, userId);

        Activity activity = getActivityById(id);
        activity.setStatus(ActivityStatus.CANCELLED);
        activity.setUpdateBy(userId);
        activityRepository.save(activity);
        log.info("活动取消成功: activityId={}", id);
    }

    /**
     * 删除活动
     */
    @Transactional
    @CacheEvict(value = "activities", allEntries = true)
    public void deleteActivity(Long id, Long userId) {
        log.info("删除活动: activityId={}, userId={}", id, userId);

        Activity activity = getActivityById(id);
        activity.setDeleted(true);
        activity.setUpdateBy(userId);
        activityRepository.save(activity);
        log.info("活动删除成功: activityId={}", id);
    }

    /**
     * 根据ID获取活动
     */
    @Cacheable(value = "activities", key = "#id")
    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动不存在"));
    }

    /**
     * 分页查询活动列表
     */
    public Page<Activity> getActivityList(PageQuery pageQuery, Boolean archived) {
        Pageable pageable = PageRequest.of(
                pageQuery.getPageNum() - 1,
                pageQuery.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createTime")
        );

        String keyword = pageQuery.getKeyword();
        Boolean archivedParam = archived != null ? archived : false;

        return activityRepository.searchActivities(keyword, archivedParam, pageable);
    }

    /**
     * 根据社团ID分页查询活动
     */
    public Page<Activity> getActivityListByClub(Long clubId, PageQuery pageQuery) {
        Pageable pageable = PageRequest.of(
                pageQuery.getPageNum() - 1,
                pageQuery.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createTime")
        );
        return activityRepository.findByClubIdAndDeletedFalse(clubId, pageable);
    }

    /**
     * 生成签到二维码
     */
    @Transactional
    public String generateSignInQrCode(Long activityId, Long userId, Integer validMinutes) {
        log.info("生成签到二维码: activityId={}, userId={}, validMinutes={}", activityId, userId, validMinutes);

        Activity activity = getActivityById(activityId);

        if (activity.getStatus() != ActivityStatus.ONGOING && activity.getStatus() != ActivityStatus.REGISTRATION_CLOSED) {
            throw new BusinessException("活动未开始，无法生成签到二维码");
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(validMinutes != null ? validMinutes : 30);

        activity.setQrCodeToken(token);
        activity.setQrCodeExpireTime(expireTime);
        activity.setUpdateBy(userId);
        activityRepository.save(activity);

        String qrContent = String.format("club:activityId=%d&token=%s", activityId, token);
        return QrCodeUtil.generateQrCodeBase64(qrContent, 400, 400);
    }

    /**
     * 刷新签到二维码
     */
    @Transactional
    public String refreshSignInQrCode(Long activityId, Long userId, Integer validMinutes) {
        log.info("刷新签到二维码: activityId={}, userId={}", activityId, userId);
        return generateSignInQrCode(activityId, userId, validMinutes);
    }

    /**
     * 获取活动签到统计
     */
    public Object getSignInStatistics(Long activityId) {
        Activity activity = getActivityById(activityId);
        return new Object();
    }

    /**
     * 验证活动时间
     */
    private void validateActivityTime(ActivityDTO dto) {
        LocalDateTime now = LocalDateTime.now();

        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new BusinessException("活动开始时间不能晚于结束时间");
        }

        if (dto.getRegistrationStartTime().isAfter(dto.getRegistrationEndTime())) {
            throw new BusinessException("报名开始时间不能晚于结束时间");
        }

        if (dto.getRegistrationEndTime().isAfter(dto.getStartTime())) {
            throw new BusinessException("报名结束时间不能晚于活动开始时间");
        }

        if (dto.getSignInStartTime() != null && dto.getSignInEndTime() != null) {
            if (dto.getSignInStartTime().isAfter(dto.getSignInEndTime())) {
                throw new BusinessException("签到开始时间不能晚于结束时间");
            }
        }
    }

    /**
     * 归档活动
     */
    @Transactional
    @CacheEvict(value = "activities", allEntries = true)
    public Activity archiveActivity(Long id, Long userId) {
        log.info("归档活动: activityId={}, userId={}", id, userId);

        Activity activity = getActivityById(id);

        if (activity.getStatus() != ActivityStatus.COMPLETED) {
            throw new BusinessException("只有已结束的活动才能归档");
        }

        activity.setArchived(true);
        activity.setUpdateBy(userId);
        activity = activityRepository.save(activity);
        log.info("活动归档成功: activityId={}", id);
        return activity;
    }

    /**
     * 获取签到二维码Token
     */
    public String getQrCodeToken(Long activityId) {
        Activity activity = getActivityById(activityId);

        if (activity.getQrCodeToken() == null) {
            throw new BusinessException("签到二维码未生成");
        }

        if (activity.getQrCodeExpireTime() == null || 
            activity.getQrCodeExpireTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("签到二维码已过期，请刷新");
        }

        String qrContent = String.format("club:activityId=%d&token=%s", 
                activityId, activity.getQrCodeToken());
        return QrCodeUtil.generateQrCodeBase64(qrContent, 400, 400);
    }
}
