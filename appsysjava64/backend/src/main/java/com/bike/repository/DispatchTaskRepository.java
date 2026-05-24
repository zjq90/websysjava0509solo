package com.bike.repository;

import com.bike.entity.DispatchTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 调度任务数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface DispatchTaskRepository extends JpaRepository<DispatchTask, Long>, JpaSpecificationExecutor<DispatchTask> {

    Optional<DispatchTask> findByTaskNo(String taskNo);

    List<DispatchTask> findByStaffId(Long staffId);

    List<DispatchTask> findByStatus(String status);

    List<DispatchTask> findByTaskType(String taskType);

    List<DispatchTask> findByStatusIn(List<String> statuses);

    List<DispatchTask> findByStaffIdAndStatus(Long staffId, String status);

    long countByStatus(String status);
}
