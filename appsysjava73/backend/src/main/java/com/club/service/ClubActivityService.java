package com.club.service;

import com.club.common.PageResult;
import com.club.entity.*;
import com.club.entity.enums.MemberRoleEnum;
import com.club.exception.BusinessException;
import com.club.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 社团活动服务
 *
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
public class ClubActivityService {

    @Autowired
    private ClubActivityRepository clubActivityRepository;

    @Autowired
    private ActivityParticipantRepository activityParticipantRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubFollowService clubFollowService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 创建活动
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubActivity createActivity(ClubActivity activity, Long userId) {
        log.info("创建活动 - clubId: {}, title: {}, userId: {}", activity.getClubId(), activity.getTitle(), userId);

        checkClubPermission(activity.getClubId(), userId);

        Club club = clubRepository.findByIdAndDeletedFalse(activity.getClubId())
                .orElseThrow(() -> new BusinessException("社团不存在"));

        activity.setClubName(club.getName());
        activity.setClubLogo(club.getLogo());
        activity.setParticipantCount(0);
        activity.setViewCount(0);
        activity.setStatus(0);

        ClubActivity saved = clubActivityRepository.save(activity);

        if (activity.getNotifyFollowers() != null && activity.getNotifyFollowers() == 1) {
            clubFollowService.sendActivityNotification(activity.getClubId(), saved.getId(), activity.getTitle());
        }

        log.info("活动创建成功 - activityId: {}", saved.getId());
        return saved;
    }

    /**
     * 更新活动信息
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubActivity updateActivity(Long activityId, ClubActivity activity, Long userId) {
        log.info("更新活动信息 - activityId: {}, userId: {}", activityId, userId);

        ClubActivity existActivity = clubActivityRepository.findByIdAndDeletedFalse(activityId)
                .orElseThrow(() -> new BusinessException("活动不存在"));

        checkClubPermission(existActivity.getClubId(), userId);

        if (activity.getTitle() != null) existActivity.setTitle(activity.getTitle());
        if (activity.getCover() != null) existActivity.setCover(activity.getCover());
        if (activity.getContent() != null) existActivity.setContent(activity.getContent());
        if (activity.getType() != null) existActivity.setType(activity.getType());
        if (activity.getLocation() != null) existActivity.setLocation(activity.getLocation());
        if (activity.getStartTime() != null) existActivity.setStartTime(activity.getStartTime());
        if (activity.getEndTime() != null) existActivity.setEndTime(activity.getEndTime());
        if (activity.getSignupDeadline() != null) existActivity.setSignupDeadline(activity.getSignupDeadline());
        if (activity.getMaxParticipants() != null) existActivity.setMaxParticipants(activity.getMaxParticipants());
        if (activity.getContactName() != null) existActivity.setContactName(activity.getContactName());
        if (activity.getContactPhone() != null) existActivity.setContactPhone(activity.getContactPhone());
        if (activity.getStatus() != null) existActivity.setStatus(activity.getStatus());

        ClubActivity saved = clubActivityRepository.save(existActivity);
        log.info("活动信息更新成功 - activityId: {}", activityId);
        return saved;
    }

    /**
     * 删除活动
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteActivity(Long activityId, Long userId) {
        log.info("删除活动 - activityId: {}, userId: {}", activityId, userId);

        ClubActivity activity = clubActivityRepository.findByIdAndDeletedFalse(activityId)
                .orElseThrow(() -> new BusinessException("活动不存在"));

        checkClubPermission(activity.getClubId(), userId);

        activity.setDeleted(true);
        clubActivityRepository.save(activity);

        log.info("活动删除成功 - activityId: {}", activityId);
    }

    /**
     * 获取活动列表
     */
    public PageResult<ClubActivity> getActivityList(Integer pageNum, Integer pageSize, Long clubId, Integer status) {
        log.info("获取活动列表 - pageNum: {}, pageSize: {}, clubId: {}, status: {}", pageNum, pageSize, clubId, status);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ClubActivity> page;

        if (clubId != null && status != null) {
            page = clubActivityRepository.findByClubIdAndStatusAndDeletedFalseOrderByCreateTimeDesc(clubId, status, pageable);
        } else if (clubId != null) {
            page = clubActivityRepository.findByClubIdAndDeletedFalseOrderByCreateTimeDesc(clubId, pageable);
        } else {
            page = clubActivityRepository.findByStatusAndDeletedFalseOrderByCreateTimeDesc(1, pageable);
        }

        return PageResult.of(page);
    }

    /**
     * 获取活动详情
     */
    @Transactional(rollbackFor = Exception.class)
    public ClubActivity getActivityDetail(Long activityId) {
        log.info("获取活动详情 - activityId: {}", activityId);

        ClubActivity activity = clubActivityRepository.findByIdAndDeletedFalse(activityId)
                .orElseThrow(() -> new BusinessException("活动不存在"));

        clubActivityRepository.incrementViewCount(activityId);

        return activity;
    }

    /**
     * 报名活动
     */
    @Transactional(rollbackFor = Exception.class)
    public ActivityParticipant signupActivity(Long activityId, Long userId) {
        log.info("报名活动 - activityId: {}, userId: {}", activityId, userId);

        ClubActivity activity = clubActivityRepository.findByIdAndDeletedFalse(activityId)
                .orElseThrow(() -> new BusinessException("活动不存在"));

        if (activityParticipantRepository.existsByActivityIdAndUserIdAndDeletedFalse(activityId, userId)) {
            throw new BusinessException("已报名该活动");
        }

        if (activity.getMaxParticipants() != null && activity.getParticipantCount() >= activity.getMaxParticipants()) {
            throw new BusinessException("活动名额已满");
        }

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        ActivityParticipant participant = new ActivityParticipant();
        participant.setActivityId(activityId);
        participant.setClubId(activity.getClubId());
        participant.setUserId(userId);
        participant.setUsername(user.getUsername());
        participant.setRealName(user.getRealName());
        participant.setAvatar(user.getAvatar());
        participant.setPhone(user.getPhone());
        participant.setSignupTime(LocalDateTime.now().format(FORMATTER));
        participant.setCheckedIn(0);

        ActivityParticipant saved = activityParticipantRepository.save(participant);

        clubActivityRepository.incrementParticipantCount(activityId);

        ClubMember member = clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(activity.getClubId(), userId).orElse(null);
        if (member != null) {
            member.setActivityCount(member.getActivityCount() + 1);
            clubMemberRepository.save(member);
        }

        log.info("活动报名成功 - participantId: {}", saved.getId());
        return saved;
    }

    /**
     * 取消报名
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelSignup(Long activityId, Long userId) {
        log.info("取消活动报名 - activityId: {}, userId: {}", activityId, userId);

        ActivityParticipant participant = activityParticipantRepository.findByActivityIdAndUserIdAndDeletedFalse(activityId, userId)
                .orElseThrow(() -> new BusinessException("未报名该活动"));

        participant.setDeleted(true);
        activityParticipantRepository.save(participant);

        log.info("活动报名取消成功 - activityId: {}, userId: {}", activityId, userId);
    }

    /**
     * 活动签到
     */
    @Transactional(rollbackFor = Exception.class)
    public void checkinActivity(Long activityId, Long userId) {
        log.info("活动签到 - activityId: {}, userId: {}", activityId, userId);

        ActivityParticipant participant = activityParticipantRepository.findByActivityIdAndUserIdAndDeletedFalse(activityId, userId)
                .orElseThrow(() -> new BusinessException("未报名该活动"));

        if (participant.getCheckedIn() == 1) {
            throw new BusinessException("已签到");
        }

        participant.setCheckedIn(1);
        participant.setCheckinTime(LocalDateTime.now().format(FORMATTER));
        activityParticipantRepository.save(participant);

        log.info("活动签到成功 - activityId: {}, userId: {}", activityId, userId);
    }

    /**
     * 获取活动参与者列表
     */
    public PageResult<ActivityParticipant> getActivityParticipants(Long activityId, Integer pageNum, Integer pageSize) {
        log.info("获取活动参与者列表 - activityId: {}, pageNum: {}, pageSize: {}", activityId, pageNum, pageSize);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ActivityParticipant> page = activityParticipantRepository.findByActivityIdAndDeletedFalseOrderBySignupTimeDesc(activityId, pageable);

        return PageResult.of(page);
    }

    /**
     * 获取我报名的活动列表
     */
    public PageResult<ActivityParticipant> getMyActivities(Long userId, Integer pageNum, Integer pageSize) {
        log.info("获取我报名的活动列表 - userId: {}, pageNum: {}, pageSize: {}", userId, pageNum, pageSize);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ActivityParticipant> page = activityParticipantRepository.findByUserIdAndDeletedFalseOrderBySignupTimeDesc(userId, pageable);

        return PageResult.of(page);
    }

    /**
     * 检查用户是否有社团管理权限
     */
    private void checkClubPermission(Long clubId, Long userId) {
        ClubMember operator = clubMemberRepository.findByClubIdAndUserIdAndDeletedFalse(clubId, userId)
                .orElseThrow(() -> new BusinessException("你不是该社团成员"));

        if (operator.getRole() != MemberRoleEnum.PRESIDENT && operator.getRole() != MemberRoleEnum.VICE_PRESIDENT) {
            throw new BusinessException("权限不足，只有社长或副社长可以执行此操作");
        }
    }
}
