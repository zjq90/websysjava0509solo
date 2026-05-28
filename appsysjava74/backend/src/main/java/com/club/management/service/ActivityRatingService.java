package com.club.management.service;

import com.club.management.common.BusinessException;
import com.club.management.common.PageQuery;
import com.club.management.dto.ActivityRatingDTO;
import com.club.management.entity.Activity;
import com.club.management.entity.ActivityRating;
import com.club.management.entity.User;
import com.club.management.entity.enums.ActivityStatus;
import com.club.management.entity.enums.SignInStatus;
import com.club.management.repository.ActivityRatingRepository;
import com.club.management.repository.ActivityRepository;
import com.club.management.repository.SignInRepository;
import com.club.management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 活动评分服务
 * 
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityRatingService {

    private final ActivityRatingRepository activityRatingRepository;
    private final ActivityRepository activityRepository;
    private final SignInRepository signInRepository;
    private final UserRepository userRepository;

    /**
     * 提交活动评分
     */
    @Transactional
    public ActivityRating submitRating(ActivityRatingDTO dto, Long userId) {
        log.info("提交活动评分: activityId={}, userId={}, rating={}", 
                dto.getActivityId(), userId, dto.getRating());

        Activity activity = activityRepository.findById(dto.getActivityId())
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动不存在"));

        if (activity.getStatus() != ActivityStatus.COMPLETED) {
            throw new BusinessException("只有已结束的活动才能评分");
        }

        boolean hasSignedIn = signInRepository.existsByActivityIdAndUserIdAndStatusInAndDeletedFalse(
                dto.getActivityId(), userId,
                List.of(SignInStatus.SIGNED, SignInStatus.LATE, SignInStatus.MAKE_UP));
        if (!hasSignedIn) {
            throw new BusinessException("只有参与活动的用户才能评分");
        }

        if (activityRatingRepository.existsByActivityIdAndUserIdAndDeletedFalse(
                dto.getActivityId(), userId)) {
            throw new BusinessException("您已对该活动进行评分，请勿重复评分");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("用户不存在"));

        ActivityRating rating = new ActivityRating();
        rating.setActivityId(dto.getActivityId());
        rating.setUserId(userId);
        rating.setUsername(dto.getAnonymous() ? "匿名用户" : user.getUsername());
        rating.setRating(dto.getRating());
        rating.setContentRating(dto.getContentRating());
        rating.setOrganizationRating(dto.getOrganizationRating());
        rating.setVenueRating(dto.getVenueRating());
        rating.setComment(dto.getComment());
        rating.setSuggestion(dto.getSuggestion());
        rating.setAnonymous(dto.getAnonymous());
        rating.setCreateBy(userId);

        rating = activityRatingRepository.save(rating);
        updateActivityAverageRating(dto.getActivityId());

        log.info("活动评分提交成功: ratingId={}, activityId={}, userId={}", 
                rating.getId(), dto.getActivityId(), userId);

        return rating;
    }

    /**
     * 分页查询活动评分
     */
    public Page<ActivityRating> getRatingList(Long activityId, PageQuery pageQuery) {
        Pageable pageable = PageRequest.of(
                pageQuery.getPageNum() - 1,
                pageQuery.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createTime")
        );
        return activityRatingRepository.findByActivityIdAndVisibleAndDeletedFalse(
                activityId, true, pageable);
    }

    /**
     * 获取用户对活动的评分
     */
    public ActivityRating getUserRating(Long activityId, Long userId) {
        return activityRatingRepository.findByActivityIdAndUserIdAndDeletedFalse(
                activityId, userId).orElse(null);
    }

    /**
     * 删除评分
     */
    @Transactional
    public void deleteRating(Long id, Long userId) {
        log.info("删除评分: ratingId={}, userId={}", id, userId);

        ActivityRating rating = activityRatingRepository.findById(id)
                .filter(r -> !r.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("评分不存在"));

        if (!rating.getUserId().equals(userId)) {
            throw new BusinessException("只能删除自己的评分");
        }

        rating.setDeleted(true);
        rating.setUpdateBy(userId);
        activityRatingRepository.save(rating);
        updateActivityAverageRating(rating.getActivityId());

        log.info("评分删除成功: ratingId={}", id);
    }

    /**
     * 更新活动平均评分
     */
    private void updateActivityAverageRating(Long activityId) {
        Double avgRating = activityRatingRepository.calculateAverageRating(activityId);
        long ratingCount = activityRatingRepository.countByActivityIdAndVisible(activityId);

        Activity activity = activityRepository.findById(activityId)
                .filter(a -> !a.getDeleted())
                .orElseThrow(() -> new EntityNotFoundException("活动不存在"));

        activity.setAverageRating(avgRating != null ? avgRating : 0.0);
        activity.setRatingCount((int) ratingCount);
        activityRepository.save(activity);

        log.debug("活动评分更新: activityId={}, avgRating={}, ratingCount={}", 
                activityId, avgRating, ratingCount);
    }

    /**
     * 获取活动所有评分（用于统计）
     */
    public List<ActivityRating> getAllRatings(Long activityId) {
        return activityRatingRepository.findByActivityIdAndDeletedFalse(activityId);
    }
}
