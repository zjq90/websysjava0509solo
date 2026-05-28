package com.club.management.service;

import com.club.management.common.BusinessException;
import com.club.management.common.PageQuery;
import com.club.management.dto.ActivitySummaryDTO;
import com.club.management.entity.Activity;
import com.club.management.entity.ActivitySummary;
import com.club.management.entity.enums.ActivityStatus;
import com.club.management.repository.ActivityRepository;
import com.club.management.repository.ActivitySummaryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 活动总结服务
 * 
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivitySummaryService {

    private final ActivitySummaryRepository activitySummaryRepository;
    private final ActivityRepository activityRepository;

    /**
     * 创建活动总结
     */
    @Transactional
    public ActivitySummary createSummary(ActivitySummaryDTO dto, Long userId) {
        log.info("创建活动总结: activityId={}, title={}, userId={}", 
                dto.getActivityId(), dto.getTitle(), userId);

        Activity activity = activityRepository.findById(dto.getActivityId())
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动不存在"));

        if (activity.getStatus() != ActivityStatus.COMPLETED) {
            throw new BusinessException("只有已结束的活动才能创建总结");
        }

        if (activitySummaryRepository.existsByActivityIdAndDeletedFalse(dto.getActivityId())) {
            throw new BusinessException("该活动已有总结，请勿重复创建");
        }

        ActivitySummary summary = new ActivitySummary();
        summary.setActivityId(dto.getActivityId());
        summary.setClubId(activity.getClubId());
        summary.setTitle(dto.getTitle());
        summary.setContent(dto.getContent());
        summary.setHighlights(dto.getHighlights());
        summary.setShortcomings(dto.getShortcomings());
        summary.setImprovements(dto.getImprovements());
        summary.setAchievements(dto.getAchievements());
        summary.setPhotoUrls(dto.getPhotoUrls());
        summary.setAttachmentUrls(dto.getAttachmentUrls());
        summary.setSyncToClubPage(dto.getSyncToClubPage());
        summary.setIsPublic(dto.getIsPublic());
        summary.setCreateBy(userId);

        if (dto.getPublished()) {
            summary.setPublished(true);
            summary.setPublishTime(LocalDateTime.now());
            summary.setPublishBy(userId);
        }

        summary = activitySummaryRepository.save(summary);
        log.info("活动总结创建成功: summaryId={}, activityId={}", summary.getId(), dto.getActivityId());

        return summary;
    }

    /**
     * 更新活动总结
     */
    @Transactional
    public ActivitySummary updateSummary(Long id, ActivitySummaryDTO dto, Long userId) {
        log.info("更新活动总结: summaryId={}, userId={}", id, userId);

        ActivitySummary summary = activitySummaryRepository.findById(id)
                .filter(s -> !s.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动总结不存在"));

        summary.setTitle(dto.getTitle());
        summary.setContent(dto.getContent());
        summary.setHighlights(dto.getHighlights());
        summary.setShortcomings(dto.getShortcomings());
        summary.setImprovements(dto.getImprovements());
        summary.setAchievements(dto.getAchievements());
        summary.setPhotoUrls(dto.getPhotoUrls());
        summary.setAttachmentUrls(dto.getAttachmentUrls());
        summary.setSyncToClubPage(dto.getSyncToClubPage());
        summary.setIsPublic(dto.getIsPublic());
        summary.setUpdateBy(userId);

        if (dto.getPublished() && !summary.getPublished()) {
            summary.setPublished(true);
            summary.setPublishTime(LocalDateTime.now());
            summary.setPublishBy(userId);
        }

        summary = activitySummaryRepository.save(summary);
        log.info("活动总结更新成功: summaryId={}", id);

        return summary;
    }

    /**
     * 发布活动总结
     */
    @Transactional
    public ActivitySummary publishSummary(Long id, Long userId) {
        log.info("发布活动总结: summaryId={}, userId={}", id, userId);

        ActivitySummary summary = activitySummaryRepository.findById(id)
                .filter(s -> !s.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动总结不存在"));

        if (summary.getPublished()) {
            throw new BusinessException("该总结已发布");
        }

        summary.setPublished(true);
        summary.setPublishTime(LocalDateTime.now());
        summary.setPublishBy(userId);
        summary.setUpdateBy(userId);

        summary = activitySummaryRepository.save(summary);
        log.info("活动总结发布成功: summaryId={}", id);

        return summary;
    }

    /**
     * 删除活动总结
     */
    @Transactional
    public void deleteSummary(Long id, Long userId) {
        log.info("删除活动总结: summaryId={}, userId={}", id, userId);

        ActivitySummary summary = activitySummaryRepository.findById(id)
                .filter(s -> !s.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动总结不存在"));

        summary.setDeleted(true);
        summary.setUpdateBy(userId);
        activitySummaryRepository.save(summary);

        log.info("活动总结删除成功: summaryId={}", id);
    }

    /**
     * 获取活动总结详情
     */
    public ActivitySummary getSummaryById(Long id) {
        return activitySummaryRepository.findById(id)
                .filter(s -> !s.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动总结不存在"));
    }

    /**
     * 根据活动ID获取总结
     */
    public ActivitySummary getSummaryByActivityId(Long activityId) {
        return activitySummaryRepository.findByActivityIdAndDeletedFalse(activityId)
                .orElseThrow(() -> new EntityNotFoundException("该活动暂无总结"));
    }

    /**
     * 分页查询社团活动总结
     */
    public Page<ActivitySummary> getSummaryListByClub(Long clubId, PageQuery pageQuery, boolean includeUnpublished) {
        Pageable pageable = PageRequest.of(
                pageQuery.getPageNum() - 1,
                pageQuery.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createTime")
        );

        if (includeUnpublished) {
            return activitySummaryRepository.findByClubIdAndDeletedFalse(clubId, pageable);
        } else {
            return activitySummaryRepository.findByClubIdAndIsPublicAndPublishedAndDeletedFalse(
                    clubId, true, true, pageable);
        }
    }
}
