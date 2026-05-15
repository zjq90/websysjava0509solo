package com.teaching.repository;

import com.teaching.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByReceiverIdOrderByCreateTimeDesc(Long receiverId);

    List<Message> findByReceiverIdAndIsReadFalse(Long receiverId);

    int countByReceiverIdAndIsReadFalse(Long receiverId);
}
