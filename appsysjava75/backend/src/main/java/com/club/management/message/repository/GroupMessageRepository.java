package com.club.management.message.repository;

import com.club.management.message.entity.GroupMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 群聊消息Repository
 *
 * @author club-management
 * @version 1.0.0
 */
@Repository
public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long>, JpaSpecificationExecutor<GroupMessage> {

    Page<GroupMessage> findByGroupIdOrderByCreateTimeDesc(Long groupId, Pageable pageable);

    List<GroupMessage> findTop20ByGroupIdOrderByCreateTimeDesc(Long groupId);
}
