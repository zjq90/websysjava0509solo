package com.club.management.message.service;

import com.club.management.common.exception.BusinessException;
import com.club.management.common.result.PageResult;
import com.club.management.message.entity.*;
import com.club.management.message.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息通知Service
 *
 * @author club-management
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final SystemMessageRepository systemMessageRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final ChatGroupMemberRepository chatGroupMemberRepository;
    private final GroupMessageRepository groupMessageRepository;
    private final PrivateMessageRepository privateMessageRepository;
    private final MessageReadStatusRepository messageReadStatusRepository;

    public PageResult<SystemMessage> getSystemMessageList(Long userId, Integer pageNum, Integer pageSize) {
        log.info("获取系统消息列表，用户ID：{}，页码：{}，每页大小：{}", userId, pageNum, pageSize);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<SystemMessage> page = systemMessageRepository.findByReceiverIdOrReceiverIdIsNullOrderByCreateTimeDesc(userId, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemMessage sendSystemMessage(SystemMessage message) {
        log.info("发送系统消息，类型：{}，标题：{}", message.getMessageType(), message.getTitle());
        message.setIsRead(0);
        message.setPushTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        SystemMessage saved = systemMessageRepository.save(message);
        log.info("系统消息发送成功，消息ID：{}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public void markSystemMessageAsRead(Long messageId, Long userId) {
        log.info("标记系统消息为已读，消息ID：{}，用户ID：{}", messageId, userId);
        SystemMessage message = systemMessageRepository.findById(messageId)
                .orElseThrow(() -> new BusinessException("消息不存在"));
        message.setIsRead(1);
        systemMessageRepository.save(message);
    }

    @Transactional(rollbackFor = Exception.class)
    public void markAllSystemMessageAsRead(Long userId) {
        log.info("标记所有系统消息为已读，用户ID：{}", userId);
        int count = systemMessageRepository.markAllAsRead(userId);
        log.info("已标记{}条消息为已读", count);
    }

    public long getUnreadMessageCount(Long userId) {
        return systemMessageRepository.countByReceiverIdAndIsRead(userId, 0);
    }

    public PageResult<ChatGroup> getChatGroupList(Long userId, Integer pageNum, Integer pageSize) {
        log.info("获取用户群聊列表，用户ID：{}", userId);
        List<ChatGroupMember> members = chatGroupMemberRepository.findByUserId(userId);
        List<Long> groupIds = new ArrayList<>();
        for (ChatGroupMember member : members) {
            groupIds.add(member.getGroupId());
        }
        if (groupIds.isEmpty()) {
            return new PageResult<>(new ArrayList<>(), 0L, pageNum, pageSize);
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<ChatGroup> page = chatGroupRepository.findByIdIn(groupIds, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public ChatGroup createChatGroup(ChatGroup group) {
        log.info("创建群聊，群名称：{}，社团ID：{}", group.getGroupName(), group.getClubId());
        group.setMemberCount(1);
        group.setStatus(0);
        ChatGroup saved = chatGroupRepository.save(group);

        ChatGroupMember owner = new ChatGroupMember();
        owner.setGroupId(saved.getId());
        owner.setUserId(group.getOwnerId());
        owner.setRole(2);
        owner.setUnreadCount(0);
        owner.setIsMute(0);
        owner.setIsTop(0);
        owner.setStatus(0);
        chatGroupMemberRepository.save(owner);

        log.info("群聊创建成功，群ID：{}", saved.getId());
        return saved;
    }

    @Transactional(rollbackFor = Exception.class)
    public GroupMessage sendGroupMessage(GroupMessage message) {
        log.info("发送群消息，群ID：{}，发送者ID：{}", message.getGroupId(), message.getSenderId());
        message.setReadCount(0);
        message.setIsRecall(0);
        GroupMessage saved = groupMessageRepository.save(message);

        chatGroupMemberRepository.increaseUnreadCount(message.getGroupId(), message.getSenderId());

        ChatGroup group = chatGroupRepository.findById(message.getGroupId()).orElse(null);
        if (group != null) {
            group.setLastMessage(message.getContent());
            group.setLastMessageTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            chatGroupRepository.save(group);
        }

        log.info("群消息发送成功，消息ID：{}", saved.getId());
        return saved;
    }

    public PageResult<GroupMessage> getGroupMessageList(Long groupId, Integer pageNum, Integer pageSize) {
        log.info("获取群消息列表，群ID：{}", groupId);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<GroupMessage> page = groupMessageRepository.findByGroupIdOrderByCreateTimeDesc(groupId, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public void markGroupMessageAsRead(Long groupId, Long userId) {
        log.info("标记群消息为已读，群ID：{}，用户ID：{}", groupId, userId);
        chatGroupMemberRepository.resetUnreadCount(groupId, userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public PrivateMessage sendPrivateMessage(PrivateMessage message) {
        log.info("发送私聊消息，发送者ID：{}，接收者ID：{}", message.getSenderId(), message.getReceiverId());
        message.setIsRead(0);
        message.setIsRecall(0);
        PrivateMessage saved = privateMessageRepository.save(message);
        log.info("私聊消息发送成功，消息ID：{}", saved.getId());
        return saved;
    }

    public PageResult<PrivateMessage> getPrivateMessageList(Long userId1, Long userId2, Integer pageNum, Integer pageSize) {
        log.info("获取私聊消息列表，用户1：{}，用户2：{}", userId1, userId2);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<PrivateMessage> page = privateMessageRepository.findConversationMessages(userId1, userId2, pageable);
        return new PageResult<>(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Transactional(rollbackFor = Exception.class)
    public void markPrivateMessageAsRead(Long senderId, Long receiverId) {
        log.info("标记私聊消息为已读，发送者ID：{}，接收者ID：{}", senderId, receiverId);
        int count = privateMessageRepository.markConversationAsRead(senderId, receiverId);
        log.info("已标记{}条私聊消息为已读", count);
    }

    public long getPrivateUnreadCount(Long userId) {
        return privateMessageRepository.countByReceiverIdAndIsRead(userId, 0);
    }

    public List<MessageReadStatus> getMessageReadStatus(Long messageId, Integer messageType) {
        log.info("获取消息已读状态，消息ID：{}，消息类型：{}", messageId, messageType);
        return messageReadStatusRepository.findByMessageIdAndMessageType(messageId, messageType);
    }

    @Transactional(rollbackFor = Exception.class)
    public void remindUnreadMember(Long messageId, Integer messageType, Long userId) {
        log.info("二次提醒未读成员，消息ID：{}，消息类型：{}，用户ID：{}", messageId, messageType, userId);
        messageReadStatusRepository.updateRemindStatus(messageId, messageType, userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createMessageReadStatus(Long messageId, Integer messageType, Long groupId, List<Long> userIds) {
        log.info("批量创建消息已读状态记录，消息ID：{}，消息类型：{}，用户数量：{}", messageId, messageType, userIds.size());
        for (Long userId : userIds) {
            MessageReadStatus status = new MessageReadStatus();
            status.setMessageId(messageId);
            status.setMessageType(messageType);
            status.setGroupId(groupId);
            status.setUserId(userId);
            status.setIsRead(0);
            status.setIsReminded(0);
            status.setRemindCount(0);
            messageReadStatusRepository.save(status);
        }
    }

    public List<ChatGroupMember> getGroupMemberList(Long groupId) {
        log.info("获取群成员列表，群ID：{}", groupId);
        return chatGroupMemberRepository.findByGroupId(groupId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addGroupMember(Long groupId, Long userId, String username, String realName, String avatar) {
        log.info("添加群成员，群ID：{}，用户ID：{}", groupId, userId);
        ChatGroupMember existing = chatGroupMemberRepository.findByGroupIdAndUserId(groupId, userId);
        if (existing != null) {
            throw new BusinessException("用户已在群聊中");
        }
        ChatGroupMember member = new ChatGroupMember();
        member.setGroupId(groupId);
        member.setUserId(userId);
        member.setUsername(username);
        member.setRealName(realName);
        member.setAvatar(avatar);
        member.setRole(0);
        member.setUnreadCount(0);
        member.setIsMute(0);
        member.setIsTop(0);
        member.setStatus(0);
        chatGroupMemberRepository.save(member);

        ChatGroup group = chatGroupRepository.findById(groupId).orElse(null);
        if (group != null) {
            group.setMemberCount(group.getMemberCount() + 1);
            chatGroupRepository.save(group);
        }
    }
}
