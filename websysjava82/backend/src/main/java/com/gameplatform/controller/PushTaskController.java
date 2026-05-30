package com.gameplatform.controller;

import com.gameplatform.common.Result;
import com.gameplatform.entity.PushTask;
import com.gameplatform.service.PushTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/push-tasks")
public class PushTaskController {
    @Autowired
    private PushTaskService pushTaskService;

    @GetMapping
    public Result<List<PushTask>> findAll() {
        return Result.success(pushTaskService.findAll());
    }

    @GetMapping("/status/{status}")
    public Result<List<PushTask>> findByStatus(@PathVariable String status) {
        return Result.success(pushTaskService.findByStatus(status));
    }

    @GetMapping("/{id}")
    public Result<PushTask> findById(@PathVariable Long id) {
        return pushTaskService.findById(id)
                .map(Result::success)
                .orElse(Result.error("推送任务不存在"));
    }

    @PostMapping
    public Result<PushTask> save(@RequestBody PushTask task) {
        return Result.success(pushTaskService.save(task));
    }

    @PutMapping("/{id}")
    public Result<PushTask> update(@PathVariable Long id, @RequestBody PushTask task) {
        return Result.success(pushTaskService.update(id, task));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        pushTaskService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/{id}/send")
    public Result<PushTask> sendTask(@PathVariable Long id) {
        PushTask task = pushTaskService.sendTask(id);
        if (task != null) {
            return Result.success(task);
        }
        return Result.error("推送任务不存在");
    }
}
