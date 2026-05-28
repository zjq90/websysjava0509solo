package com.club.management.message.repository;

import com.club.management.message.entity.ChatGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 群聊Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long>, JpaSpecificationExecutor<ChatGroup> {

    List<ChatGroup> findByClubId(Long clubId);

    List<ChatGroup> findByOwnerId(Long ownerId);

    Page<ChatGroup> findByIdIn(List<Long> ids, Pageable pageable);
}
