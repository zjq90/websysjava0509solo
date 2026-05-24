package com.vehicle.repository;

import com.vehicle.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatusOrderByCreatedAtDesc(String status);

    List<Task> findByAssignedOfficerAndStatusOrderByCreatedAtDesc(String assignedOfficer, String status);

    List<Task> findByStatusInOrderByCreatedAtDesc(List<String> statuses);
}
