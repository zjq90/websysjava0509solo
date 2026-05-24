package com.vehicle.service;

import com.vehicle.entity.Task;
import com.vehicle.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getPendingTasks() {
        return taskRepository.findByStatusOrderByCreatedAtDesc("PENDING");
    }

    public List<Task> getTasksByOfficer(String officerName) {
        return taskRepository.findByAssignedOfficerAndStatusOrderByCreatedAtDesc(officerName, "PENDING");
    }

    public List<Task> getActiveTasks() {
        return taskRepository.findByStatusInOrderByCreatedAtDesc(Arrays.asList("PENDING", "IN_PROGRESS"));
    }

    public Task updateTaskStatus(Long id, String status) {
        Optional<Task> opt = taskRepository.findById(id);
        if (opt.isPresent()) {
            Task task = opt.get();
            task.setStatus(status);
            if ("COMPLETED".equals(status)) {
                task.setCompletedAt(LocalDateTime.now());
            }
            return taskRepository.save(task);
        }
        return null;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
