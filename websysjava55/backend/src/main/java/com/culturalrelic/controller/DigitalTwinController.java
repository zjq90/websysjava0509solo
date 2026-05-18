package com.culturalrelic.controller;

import com.culturalrelic.common.Result;
import com.culturalrelic.entity.DigitalTwin;
import com.culturalrelic.service.DigitalTwinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/digital-twin")
@Tag(name = "数字分身管理", description = "文物数字分身3D模型管理，支持虚拟修复演示")
public class DigitalTwinController {

    @Autowired
    private DigitalTwinService digitalTwinService;

    @PostMapping
    @Operation(summary = "新增数字分身", description = "创建文物的3D数字分身")
    public Result<DigitalTwin> save(@RequestBody DigitalTwin twin) {
        return Result.success(digitalTwinService.save(twin));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询数字分身详情", description = "根据ID查询数字分身详情")
    public Result<DigitalTwin> findById(@PathVariable Long id) {
        Optional<DigitalTwin> twin = digitalTwinService.findById(id);
        if (twin.isPresent()) {
            return Result.success(twin.get());
        }
        return Result.error("数字分身不存在");
    }

    @GetMapping
    @Operation(summary = "查询所有数字分身", description = "获取所有数字分身列表")
    public Result<List<DigitalTwin>> findAll() {
        return Result.success(digitalTwinService.findAll());
    }

    @PutMapping
    @Operation(summary = "更新数字分身", description = "更新数字分身信息")
    public Result<DigitalTwin> update(@RequestBody DigitalTwin twin) {
        return Result.success(digitalTwinService.update(twin));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除数字分身", description = "根据ID删除数字分身")
    public Result<Void> delete(@PathVariable Long id) {
        return digitalTwinService.delete(id) ? Result.success("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/relic/{relicId}")
    @Operation(summary = "根据文物ID查询", description = "根据文物ID查询对应的数字分身")
    public Result<List<DigitalTwin>> findByRelicId(@PathVariable Long relicId) {
        return Result.success(digitalTwinService.findByRelicId(relicId));
    }
}
