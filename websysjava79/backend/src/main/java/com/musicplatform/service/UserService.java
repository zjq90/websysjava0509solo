package com.musicplatform.service;

import com.musicplatform.entity.Follow;
import com.musicplatform.entity.Notification;
import com.musicplatform.entity.User;
import com.musicplatform.repository.FollowRepository;
import com.musicplatform.repository.NotificationRepository;
import com.musicplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getDetails() instanceof Long) {
            return (Long) auth.getDetails();
        }
        return null;
    }

    public User getCurrentUser() {
        Long userId = getCurrentUserId();
        return userId != null ? userRepository.findById(userId).orElse(null) : null;
    }

    public Map<String, Object> getUserProfile(Long userId, Long viewerId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        Map<String, Object> profile = new HashMap<>();
        profile.put("id", user.getId());
        profile.put("username", user.getUsername());
        profile.put("nickname", user.getNickname());
        profile.put("avatar", user.getAvatar());
        profile.put("bio", user.getBio());
        profile.put("role", user.getRole().name());
        profile.put("isVip", user.isVip());
        profile.put("createdAt", user.getCreatedAt());

        profile.put("followerCount", userRepository.countFollowers(userId));
        profile.put("followingCount", userRepository.countFollowing(userId));

        if (viewerId != null) {
            profile.put("isFollowing", followRepository.existsByFollower_IdAndFollowing_Id(viewerId, userId));
        } else {
            profile.put("isFollowing", false);
        }

        return profile;
    }

    @Transactional
    public User updateProfile(Long userId, Map<String, String> updates) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!userId.equals(getCurrentUserId())) {
            throw new RuntimeException("无权限修改");
        }

        if (updates.containsKey("nickname")) {
            user.setNickname(updates.get("nickname"));
        }
        if (updates.containsKey("bio")) {
            user.setBio(updates.get("bio"));
        }
        if (updates.containsKey("avatar")) {
            user.setAvatar(updates.get("avatar"));
        }

        return userRepository.save(user);
    }

    @Transactional
    public void followUser(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new RuntimeException("不能关注自己");
        }

        if (followRepository.existsByFollower_IdAndFollowing_Id(followerId, followingId)) {
            throw new RuntimeException("已经关注了该用户");
        }

        User follower = userRepository.findById(followerId).orElseThrow(() -> new RuntimeException("用户不存在"));
        User following = userRepository.findById(followingId).orElseThrow(() -> new RuntimeException("用户不存在"));

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);

        createNotification(followingId, followerId, follower.getNickname(), 
            follower.getAvatar(), Notification.NotificationType.FOLLOW, null, null);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        followRepository.deleteByFollower_IdAndFollowing_Id(followerId, followingId);
    }

    public Page<User> getFollowers(Long userId, Pageable pageable) {
        return followRepository.findFollowersByFollowingId(userId, pageable);
    }

    public Page<User> getFollowing(Long userId, Pageable pageable) {
        return followRepository.findFollowingByFollowerId(userId, pageable);
    }

    private void createNotification(Long userId, Long fromUserId, String fromUserName, 
            String fromUserAvatar, Notification.NotificationType type, Long targetId, String targetTitle) {
        Notification notification = new Notification();
        notification.setUser(userRepository.getById(userId));
        notification.setFromUserId(fromUserId);
        notification.setFromUserName(fromUserName);
        notification.setFromUserAvatar(fromUserAvatar);
        notification.setType(type);
        notification.setTargetId(targetId);
        notification.setTargetTitle(targetTitle);
        notificationRepository.save(notification);
    }

    public Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.searchUsers(keyword).stream()
            .collect(java.util.stream.Collectors.collectingAndThen(
                java.util.stream.Collectors.toList(), 
                list -> new org.springframework.data.domain.PageImpl<>(list, pageable, list.size())
            ));
    }

    public long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByUser_IdAndIsReadFalse(userId);
    }

    public Page<Notification> getNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUser_IdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("通知不存在"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
