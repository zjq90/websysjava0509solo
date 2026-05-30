package com.gameplatform.repository;

import com.gameplatform.entity.PushTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PushTaskRepository extends JpaRepository<PushTask, Long> {
    List<PushTask> findByStatus(String status);

    @Query("SELECT p FROM PushTask p WHERE p.status = 'PENDING' AND p.timed = true AND p.scheduledTime <= :now")
    List<PushTask> findScheduledTasksToSend(LocalDateTime now);

    List<PushTask> findByStatusIn(List<String> statuses);
}
