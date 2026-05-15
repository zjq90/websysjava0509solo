package com.chatsystem.service;

import com.chatsystem.entity.Friend;
import com.chatsystem.entity.User;
import com.chatsystem.repository.FriendRepository;
import com.chatsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 好友管理服务类
 */
@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 添加好友请求
     */
    @Transactional
    public Friend addFriendRequest(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new RuntimeException("不能添加自己为好友");
        }

        // 检查用户是否存在
        if (!userRepository.existsById(friendId)) {
            throw new RuntimeException("好友用户不存在");
        }

        // 检查是否已存在好友关系
        if (friendRepository.existsByUserIdAndFriendId(userId, friendId)) {
            throw new RuntimeException("已存在好友关系或请求");
        }

        // 创建单向好友请求
        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        friend.setStatus(0);

        return friendRepository.save(friend);
    }

    /**
     * 接受好友请求
     */
    @Transactional
    public Friend acceptFriendRequest(Long requestId) {
        Friend request = friendRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("好友请求不存在"));

        if (request.getStatus() != 0) {
            throw new RuntimeException("好友请求状态不正确");
        }

        // 更新请求状态为已添加
        request.setStatus(1);
        friendRepository.save(request);

        // 创建反向好友关系
        Friend reverseFriend = new Friend();
        reverseFriend.setUserId(request.getFriendId());
        reverseFriend.setFriendId(request.getUserId());
        reverseFriend.setStatus(1);
        friendRepository.save(reverseFriend);

        return request;
    }

    /**
     * 拒绝好友请求
     */
    @Transactional
    public void rejectFriendRequest(Long requestId) {
        Friend request = friendRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("好友请求不存在"));

        request.setStatus(2);
        friendRepository.save(request);
    }

    /**
     * 删除好友
     */
    @Transactional
    public void deleteFriend(Long userId, Long friendId) {
        // 删除正向好友关系
        Optional<Friend> friend1 = friendRepository.findByUserIdAndFriendId(userId, friendId);
        friend1.ifPresent(friendRepository::delete);

        // 删除反向好友关系
        Optional<Friend> friend2 = friendRepository.findByUserIdAndFriendId(friendId, userId);
        friend2.ifPresent(friendRepository::delete);
    }

    /**
     * 获取好友列表
     */
    public List<User> getFriendList(Long userId) {
        List<Friend> friends = friendRepository.findByUserIdAndStatus(userId, 1);
        List<Long> friendIds = new ArrayList<>();
        for (Friend friend : friends) {
            friendIds.add(friend.getFriendId());
        }
        return userRepository.findAllById(friendIds);
    }

    /**
     * 获取好友请求列表
     */
    public List<Friend> getFriendRequests(Long userId) {
        return friendRepository.findByFriendIdAndStatus(userId, 0);
    }

    /**
     * 更新好友备注
     */
    @Transactional
    public Friend updateRemark(Long userId, Long friendId, String remark) {
        Friend friend = friendRepository.findByUserIdAndFriendId(userId, friendId)
                .orElseThrow(() -> new RuntimeException("好友关系不存在"));

        friend.setRemark(remark);
        return friendRepository.save(friend);
    }

    /**
     * 检查是否为好友
     */
    public boolean isFriend(Long userId1, Long userId2) {
        return friendRepository.existsByUserIdAndFriendId(userId1, userId2) &&
               friendRepository.existsByUserIdAndFriendId(userId2, userId1);
    }
}
