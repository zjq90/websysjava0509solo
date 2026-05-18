package com.secondhand.repository;

import com.secondhand.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

    List<Message> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<Message> findByUserIdAndIsRead(Long userId, Boolean isRead);

    long countByUserIdAndIsRead(Long userId, Boolean isRead);
}
