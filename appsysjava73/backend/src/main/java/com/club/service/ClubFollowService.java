package com.club.service;

import com.club.common.PageResult;
import com.club.entity.Club;
import com.club.entity.ClubFollow;
import com.club.entity.ClubNotification;
import com.club.exception.BusinessException;
import com.club.repository.ClubFollowRepository;
import com.club.repository.ClubNotificationRepository;
import com.club.repository.ClubRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ClubFollowService {

    @Autowired
    private ClubFollowRepository clubFollowRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubNotificationRepository clubNotificationRepository;

    @Transactional(rollbackFor = Exception.class)
    public void followClub(Long clubId, Long userId) {
        log.info("关注社团 - clubId: {}, userId: {}", clubId, userId);

        if (clubFollowRepository.existsByUserIdAndClubIdAndDeletedFalse(userId, clubId)) {
            throw new BusinessException("已关注该社团");
        }

        Club club = clubRepository.findByIdAndDeletedFalse(clubId)
                .orElseThrow(() -> new BusinessException("社团不存在"));

        ClubFollow follow = new ClubFollow();
        follow.setUserId(userId);
        follow.setClubId(clubId);
        follow.setClubName(club.getName());
        follow.setClubLogo(club.getLogo());
        clubFollowRepository.save(follow);

        clubRepository.incrementFollowCount(clubId);

        log.info("关注社团成功 - clubId: {}, userId: {}", clubId, userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void unfollowClub(Long clubId, Long userId) {
        log.info("取消关注社团 - clubId: {}, userId: {}", clubId, userId);

        ClubFollow follow = clubFollowRepository.findByUserIdAndClubIdAndDeletedFalse(userId, clubId)
                .orElseThrow(() -> new BusinessException("未关注该社团"));

        follow.setDeleted(true);
        clubFollowRepository.save(follow);

        clubRepository.decrementFollowCount(clubId);

        log.info("取消关注社团成功 - clubId: {}, userId: {}", clubId, userId);
    }

    public PageResult<Club> getMyFollowedClubs(Integer pageNum, Integer pageSize, Long userId) {
        log.info("获取我关注的社团列表 - userId: {}, pageNum: {}, pageSize: {}", userId, pageNum, pageSize);

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ClubFollow> followPage = clubFollowRepository.findByUserIdAndDeletedFalseOrderByCreateTimeDesc(userId, pageable);

        List<Club> clubs = new ArrayList<>();
        for (ClubFollow follow : followPage.getContent()) {
            clubRepository.findByIdAndDeletedFalse(follow.getClubId()).ifPresent(clubs::add);
        }

        return PageResult.of(clubs, followPage.getTotalElements(), pageNum, pageSize);
    }

    public boolean checkFollowStatus(Long clubId, Long userId) {
        return clubFollowRepository.existsByUserIdAndClubIdAndDeletedFalse(userId, clubId);
    }

    public long getFollowCount(Long clubId) {
        return clubFollowRepository.countByClubIdAndDeletedFalse(clubId);
    }

    public boolean isFollowed(Long userId, Long clubId) {
        return clubFollowRepository.existsByUserIdAndClubIdAndDeletedFalse(userId, clubId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendActivityNotification(Long clubId, Long activityId, String activityTitle) {
        log.info("发送活动通知 - clubId: {}, activityId: {}, activityTitle: {}", clubId, activityId, activityTitle);

        Club club = clubRepository.findByIdAndDeletedFalse(clubId)
                .orElseThrow(() -> new BusinessException("社团不存在"));

        List<ClubFollow> followers = clubFollowRepository.findByClubIdAndDeletedFalse(clubId);

        for (ClubFollow follower : followers) {
            ClubNotification notification = new ClubNotification();
            notification.setUserId(follower.getUserId());
            notification.setClubId(clubId);
            notification.setClubName(club.getName());
            notification.setClubLogo(club.getLogo());
            notification.setTitle("新活动发布");
            notification.setContent(club.getName() + "发布了新活动：" + activityTitle);
            notification.setType("activity");
            notification.setRelatedId(activityId);
            notification.setRead(0);
            clubNotificationRepository.save(notification);
        }

        log.info("活动通知发送完成 - 接收人数: {}", followers.size());
    }
}
