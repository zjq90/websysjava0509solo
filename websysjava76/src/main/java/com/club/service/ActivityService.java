package com.club.service;

import com.club.dto.PageQuery;
import com.club.entity.Activity;
import com.club.entity.ContentReview;
import com.club.enums.ActivityType;
import com.club.enums.ApprovalStatus;
import com.club.enums.ReviewStatus;
import com.club.enums.ReviewType;
import com.club.repository.ActivityRepository;
import com.club.repository.ContentReviewRepository;
import com.club.util.SensitiveWordFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 活动管理服务类
 * 包含活动CRUD、活动审批、内容审查等业务逻辑
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ContentReviewRepository contentReviewRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ActivityService(ActivityRepository activityRepository,
                          ContentReviewRepository contentReviewRepository) {
        this.activityRepository = activityRepository;
        this.contentReviewRepository = contentReviewRepository;
    }

    /**
     * 分页查询活动列表
     */
    public Page<Activity> getActivityList(PageQuery query) {
        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize());
        
        Specification<Activity> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(query.getKeyword())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + query.getKeyword() + "%"));
            }
            if (StringUtils.hasText(query.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get("approvalStatus"), ApprovalStatus.valueOf(query.getStatus())));
            }
            if (StringUtils.hasText(query.getType())) {
                predicates.add(criteriaBuilder.equal(root.get("type"), ActivityType.valueOf(query.getType())));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return activityRepository.findAll(spec, pageable);
    }

    /**
     * 根据ID查询活动
     */
    public Activity getActivityById(Long id) {
        return activityRepository.findById(id).orElse(null);
    }

    /**
     * 创建活动
     */
    public Activity createActivity(Activity activity) {
        // 自动判断是否为大型活动（预计人数>=500）
        if (activity.getExpectedParticipants() != null && activity.getExpectedParticipants() >= 500) {
            activity.setIsLargeScale(true);
        }
        
        // 根据活动类型和规模自动设置审批级别
        setApprovalLevel(activity);
        
        // 设置需要审批
        activity.setNeedApproval(true);
        activity.setApprovalStatus(ApprovalStatus.PENDING);
        activity.setCurrentApprovalStage(1);
        
        Activity saved = activityRepository.save(activity);
        // 内容敏感词检查并创建审查记录
        checkAndCreateReview(saved);
        return saved;
    }
    
    /**
     * 根据活动类型和规模设置审批级别
     * 跨校活动：需要3级审批（社联->团委->校级领导）
     * 大型校级活动（>=500人）：需要2级审批（社联->团委）
     * 普通校级活动：需要1级审批（社联）
     * 社团内部活动：需要1级审批（社联）
     */
    private void setApprovalLevel(Activity activity) {
        if (ActivityType.CROSS_SCHOOL.equals(activity.getType())) {
            activity.setApprovalLevel(3);
        } else if (ActivityType.SCHOOL_LEVEL.equals(activity.getType()) && 
                   activity.getIsLargeScale() != null && activity.getIsLargeScale()) {
            activity.setApprovalLevel(2);
        } else {
            activity.setApprovalLevel(1);
        }
    }

    /**
     * 更新活动
     */
    public Activity updateActivity(Long id, Activity activity) {
        Activity existing = getActivityById(id);
        if (existing == null) {
            throw new RuntimeException("活动不存在");
        }
        activity.setId(id);
        Activity saved = activityRepository.save(activity);
        // 内容敏感词检查并创建审查记录
        checkAndCreateReview(saved);
        return saved;
    }

    /**
     * 删除活动
     */
    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }

    /**
     * 审核活动（支持多级审批）
     */
    public Activity approveActivity(Long id, boolean approved, String opinion, Long approverId, String approverName) {
        Activity activity = getActivityById(id);
        if (activity == null) {
            throw new RuntimeException("活动不存在");
        }
        if (!activity.getNeedApproval()) {
            throw new RuntimeException("该活动不需要审批");
        }
        if (activity.getApprovalStatus() != null && activity.getApprovalStatus() != ApprovalStatus.PENDING) {
            throw new RuntimeException("该活动已处理");
        }
        
        int currentStage = activity.getCurrentApprovalStage() != null ? activity.getCurrentApprovalStage() : 1;
        int approvalLevel = activity.getApprovalLevel() != null ? activity.getApprovalLevel() : 1;
        
        // 保存当前阶段的审核信息
        saveApprovalInfo(activity, currentStage, approved, opinion, approverId, approverName);
        
        if (!approved) {
            // 任何一级驳回都直接结束审批
            activity.setApprovalStatus(ApprovalStatus.REJECTED);
            activity.setApproveOpinion(opinion);
            activity.setApproverId(approverId);
            activity.setApproverName(approverName);
            activity.setApproveTime(LocalDateTime.now());
        } else {
            // 审核通过，检查是否还有下一级
            if (currentStage < approvalLevel) {
                // 进入下一级审批
                activity.setCurrentApprovalStage(currentStage + 1);
                activity.setApprovalStatus(ApprovalStatus.PENDING);
            } else {
                // 所有级别都通过，审批完成
                activity.setApprovalStatus(ApprovalStatus.APPROVED);
                activity.setApproveOpinion(opinion);
                activity.setApproverId(approverId);
                activity.setApproverName(approverName);
                activity.setApproveTime(LocalDateTime.now());
            }
        }
        
        return activityRepository.save(activity);
    }
    
    /**
     * 保存对应审批阶段的审核信息
     */
    private void saveApprovalInfo(Activity activity, int stage, boolean approved, String opinion, Long approverId, String approverName) {
        LocalDateTime now = LocalDateTime.now();
        switch (stage) {
            case 1:
                activity.setAssociationApproverId(approverId);
                activity.setAssociationApproverName(approverName);
                activity.setAssociationApproveTime(now);
                activity.setAssociationApproveOpinion(opinion + (approved ? "（通过）" : "（驳回）"));
                break;
            case 2:
                activity.setLeagueApproverId(approverId);
                activity.setLeagueApproverName(approverName);
                activity.setLeagueApproveTime(now);
                activity.setLeagueApproveOpinion(opinion + (approved ? "（通过）" : "（驳回）"));
                break;
            case 3:
                activity.setSchoolApproverId(approverId);
                activity.setSchoolApproverName(approverName);
                activity.setSchoolApproveTime(now);
                activity.setSchoolApproveOpinion(opinion + (approved ? "（通过）" : "（驳回）"));
                break;
        }
    }
    
    /**
     * 获取待审批活动列表（按审批级别筛选）
     */
    public Page<Activity> getPendingApprovalList(PageQuery query, Integer approvalStage) {
        Pageable pageable = PageRequest.of(query.getPageNum() - 1, query.getPageSize());
        
        Specification<Activity> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 只查询待审核的活动
            predicates.add(criteriaBuilder.equal(root.get("approvalStatus"), ApprovalStatus.PENDING));
            predicates.add(criteriaBuilder.equal(root.get("needApproval"), true));
            
            // 如果指定了审批阶段，则筛选对应阶段的活动
            if (approvalStage != null) {
                predicates.add(criteriaBuilder.equal(root.get("currentApprovalStage"), approvalStage));
            }
            
            if (StringUtils.hasText(query.getKeyword())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + query.getKeyword() + "%"));
            }
            if (StringUtils.hasText(query.getType())) {
                predicates.add(criteriaBuilder.equal(root.get("type"), ActivityType.valueOf(query.getType())));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return activityRepository.findAll(spec, pageable);
    }
    
    /**
     * 获取审批统计
     */
    public Map<String, Object> getApprovalStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 各级别待审批数量
        stats.put("level1Pending", activityRepository.countByNeedApprovalTrueAndApprovalStatusAndCurrentApprovalStage(
            ApprovalStatus.PENDING, 1));
        stats.put("level2Pending", activityRepository.countByNeedApprovalTrueAndApprovalStatusAndCurrentApprovalStage(
            ApprovalStatus.PENDING, 2));
        stats.put("level3Pending", activityRepository.countByNeedApprovalTrueAndApprovalStatusAndCurrentApprovalStage(
            ApprovalStatus.PENDING, 3));
        
        // 跨校活动和大型校级活动待审批数量
        stats.put("crossSchoolPending", activityRepository.countByNeedApprovalTrueAndApprovalStatusAndType(
            ApprovalStatus.PENDING, ActivityType.CROSS_SCHOOL));
        stats.put("largeScalePending", activityRepository.countByNeedApprovalTrueAndApprovalStatusAndIsLargeScale(
            ApprovalStatus.PENDING, true));
        
        // 已通过和已驳回数量
        stats.put("approved", activityRepository.countByNeedApprovalTrueAndApprovalStatus(ApprovalStatus.APPROVED));
        stats.put("rejected", activityRepository.countByNeedApprovalTrueAndApprovalStatus(ApprovalStatus.REJECTED));
        stats.put("totalPending", activityRepository.countByNeedApprovalTrueAndApprovalStatus(ApprovalStatus.PENDING));
        
        return stats;
    }

    /**
     * 内容敏感词检查并创建审查记录
     */
    public void checkAndCreateReview(Activity activity) {
        // 检查活动名称
        checkFieldAndCreateReview(activity, "name", activity.getName(), ReviewType.NAME);
        
        // 检查活动主题
        checkFieldAndCreateReview(activity, "theme", activity.getTheme(), ReviewType.THEME);
        
        // 检查活动描述
        checkFieldAndCreateReview(activity, "description", activity.getDescription(), ReviewType.DESCRIPTION);
        
        // 检查海报内容（假设posterUrl字段存储的是海报文本描述）
        if (activity.getPosterUrl() != null && activity.getPosterUrl().length() > 0) {
            checkFieldAndCreateReview(activity, "poster", activity.getPosterUrl(), ReviewType.POSTER);
        }
    }

    /**
     * 检查单个字段并创建审查记录
     */
    private void checkFieldAndCreateReview(Activity activity, String fieldName, String content, ReviewType reviewType) {
        if (content == null || content.isEmpty()) {
            return;
        }
        
        List<String> sensitiveWords = SensitiveWordFilter.findSensitiveWords(content);
        
        if (!sensitiveWords.isEmpty()) {
            ContentReview review = new ContentReview();
            review.setActivityId(activity.getId());
            review.setActivityName(activity.getName());
            review.setClubName(activity.getClubName());
            review.setReviewType(reviewType);
            review.setFieldName(fieldName);
            review.setOriginalContent(content);
            review.setStatus(ReviewStatus.PENDING);
            
            try {
                review.setSensitiveWords(objectMapper.writeValueAsString(sensitiveWords));
            } catch (JsonProcessingException e) {
                review.setSensitiveWords(sensitiveWords.toString());
            }
            
            contentReviewRepository.save(review);
        }
    }

    /**
     * 获取待审查列表
     */
    public List<ContentReview> getPendingReviewList() {
        return contentReviewRepository.findPendingReviews();
    }

    /**
     * 审查通过
     */
    public ContentReview approveReview(Long reviewId, Long reviewerId, String reviewerName, String opinion) {
        ContentReview review = contentReviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            throw new RuntimeException("审查记录不存在");
        }
        if (review.getStatus() != ReviewStatus.PENDING) {
            throw new RuntimeException("该审查已处理");
        }
        
        review.setStatus(ReviewStatus.APPROVED);
        review.setReviewerId(reviewerId);
        review.setReviewerName(reviewerName);
        review.setReviewOpinion(opinion);
        review.setReviewTime(LocalDateTime.now());
        
        return contentReviewRepository.save(review);
    }

    /**
     * 屏蔽内容
     */
    public ContentReview blockReview(Long reviewId, Long reviewerId, String reviewerName, String opinion) {
        ContentReview review = contentReviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            throw new RuntimeException("审查记录不存在");
        }
        if (review.getStatus() != ReviewStatus.PENDING) {
            throw new RuntimeException("该审查已处理");
        }
        
        review.setStatus(ReviewStatus.BLOCKED);
        review.setReviewerId(reviewerId);
        review.setReviewerName(reviewerName);
        review.setReviewOpinion(opinion);
        review.setReviewTime(LocalDateTime.now());
        
        // 屏蔽活动内容
        Activity activity = activityRepository.findById(review.getActivityId()).orElse(null);
        if (activity != null) {
            String maskedContent = SensitiveWordFilter.maskSensitiveWords(review.getOriginalContent());
            // 根据字段名更新对应字段
            updateActivityField(activity, review.getFieldName(), maskedContent);
            activityRepository.save(activity);
        }
        
        return contentReviewRepository.save(review);
    }

    /**
     * 更新活动字段
     */
    private void updateActivityField(Activity activity, String fieldName, String maskedContent) {
        switch (fieldName) {
            case "name":
                activity.setName(maskedContent);
                break;
            case "theme":
                activity.setTheme(maskedContent);
                break;
            case "description":
                activity.setDescription(maskedContent);
                break;
            case "poster":
                activity.setPosterUrl(maskedContent);
                break;
        }
    }

    /**
     * 获取审查统计
     */
    public Map<String, Object> getReviewStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("pending", contentReviewRepository.countByStatus(ReviewStatus.PENDING));
        stats.put("approved", contentReviewRepository.countByStatus(ReviewStatus.APPROVED));
        stats.put("blocked", contentReviewRepository.countByStatus(ReviewStatus.BLOCKED));
        stats.put("total", contentReviewRepository.count());
        return stats;
    }

    /**
     * 获取活动统计数据
     */
    public Map<String, Object> getActivityStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalActivities", activityRepository.count());
        stats.put("byType", activityRepository.countByType());
        stats.put("byApprovalStatus", activityRepository.countByApprovalStatus());
        stats.put("pendingApproval", activityRepository.countByNeedApprovalTrueAndApprovalStatus(ApprovalStatus.PENDING));
        stats.put("nonCompliant", activityRepository.findByContentCompliantFalse().size());
        return stats;
    }

    /**
     * 查询内容不合规的活动
     */
    public List<Activity> getNonCompliantActivities() {
        return activityRepository.findByContentCompliantFalse();
    }

    /**
     * 获取活动内容审查列表
     */
    public List<Map<String, Object>> getReviewList() {
        List<Activity> nonCompliantActivities = getNonCompliantActivities();
        List<Map<String, Object>> reviewList = new ArrayList<>();
        
        for (Activity activity : nonCompliantActivities) {
            Map<String, Object> review = new HashMap<>();
            review.put("id", activity.getId());
            review.put("activityName", activity.getName());
            review.put("clubName", activity.getClubName());
            review.put("field", "description");
            review.put("content", activity.getDescription());
            review.put("sensitiveWords", extractSensitiveWords(activity.getViolationNote()));
            review.put("status", "PENDING");
            review.put("createTime", activity.getCreateTime());
            reviewList.add(review);
        }
        
        return reviewList;
    }

    /**
     * 从违规说明中提取敏感词
     */
    private List<String> extractSensitiveWords(String violationNote) {
        List<String> words = new ArrayList<>();
        if (violationNote != null && !violationNote.isEmpty()) {
            String[] parts = violationNote.split("；");
            for (String part : parts) {
                if (part.contains("包含敏感词：")) {
                    words.add(part.replace("包含敏感词：", "").trim());
                }
            }
        }
        return words;
    }
}
