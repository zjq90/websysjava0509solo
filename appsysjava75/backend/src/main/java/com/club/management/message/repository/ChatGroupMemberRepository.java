package com.club.management.message.repository;

import com.club.management.message.entity.ChatGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 群成员Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ChatGroupMemberRepository extends JpaRepository<ChatGroupMember, Long>, JpaSpecificationExecutor<ChatGroupMember> {

    List<ChatGroupMember> findByGroupId(Long groupId);

    List<ChatGroupMember> findByUserId(Long userId);

    ChatGroupMember findByGroupIdAndUserId(Long groupId, Long userId);

    @Modifying
    @Query("UPDATE ChatGroupMember m SET m.unreadCount = m.unreadCount + 1 WHERE m.groupId = :groupId AND m.userId != :senderId")
    int increaseUnreadCount(@Param("groupId") Long groupId, @Param("senderId") Long senderId);

    @Modifying
    @Query("UPDATE ChatGroupMember m SET m.unreadCount = 0 WHERE m.groupId = :groupId AND m.userId = :userId")
    int resetUnreadCount(@Param("groupId") Long groupId, @Param("userId") Long userId);
}
