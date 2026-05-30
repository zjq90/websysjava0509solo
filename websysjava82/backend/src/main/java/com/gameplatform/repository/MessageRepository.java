package com.gameplatform.repository;

import com.gameplatform.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUserId(Long userId);
    List<Message> findByUserIdAndRead(Long userId, Boolean read);
    List<Message> findByTaskId(Long taskId);
    long countByTaskIdAndRead(Long taskId, Boolean read);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.taskId = :taskId")
    long countByTaskId(@Param("taskId") Long taskId);
}
