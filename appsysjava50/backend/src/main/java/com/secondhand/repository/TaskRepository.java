package com.secondhand.repository;

import com.secondhand.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatusOrderBySortOrderAsc(String status);

}
