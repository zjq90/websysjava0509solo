package com.vehicle.controller;

import com.vehicle.common.Result;
import com.vehicle.entity.Task;
import com.vehicle.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/task")
@Tag(name = "任务管理", description = "查验任务管理接口")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/pending")
    @Operation(summary = "获取待办任务", description = "获取所有待处理的查验任务列表")
    public Result<List<Task>> getPendingTasks() {
        List<Task> tasks = taskService.getPendingTasks();
        return Result.success(tasks);
    }

    @GetMapping("/officer/{officerName}")
    @Operation(summary = "获取执法人任务", description = "根据执法人姓名获取指派的待办任务")
    public Result<List<Task>> getTasksByOfficer(
            @Parameter(description = "执法人姓名", required = true)
            @PathVariable String officerName) {
        List<Task> tasks = taskService.getTasksByOfficer(officerName);
        return Result.success(tasks);
    }

    @GetMapping("/active")
    @Operation(summary = "获取活跃任务", description = "获取所有待处理和处理中的任务")
    public Result<List<Task>> getActiveTasks() {
        List<Task> tasks = taskService.getActiveTasks();
        return Result.success(tasks);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新任务状态", description = "更新任务的处理状态")
    public Result<Task> updateTaskStatus(
            @Parameter(description = "任务ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "任务状态: PENDING-待处理, IN_PROGRESS-处理中, COMPLETED-已完成, CANCELLED-已取消", required = true)
            @RequestParam String status) {
        Task task = taskService.updateTaskStatus(id, status);
        if (task != null) {
            return Result.success("任务状态更新成功", task);
        }
        return Result.error("任务不存在");
    }

    @PostMapping
    @Operation(summary = "创建任务", description = "创建新的查验任务")
    public Result<Task> createTask(@RequestBody Task task) {
        Task saved = taskService.createTask(task);
        return Result.success("任务创建成功", saved);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询任务详情", description = "根据ID查询任务详细信息")
    public Result<Task> getTask(
            @Parameter(description = "任务ID", required = true)
            @PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(Result::success)
                .orElse(Result.error("任务不存在"));
    }

    @GetMapping
    @Operation(summary = "获取所有任务", description = "获取全部任务列表")
    public Result<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return Result.success(tasks);
    }
}
