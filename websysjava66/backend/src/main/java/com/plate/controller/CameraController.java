package com.plate.controller;

import com.plate.common.Result;
import com.plate.entity.Camera;
import com.plate.service.CameraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cameras")
@Tag(name = "摄像头管理", description = "摄像头增删改查接口")
public class CameraController {
    @Autowired
    private CameraService cameraService;

    @GetMapping
    @Operation(summary = "获取所有摄像头")
    public Result<List<Camera>> getAll() {
        return Result.success(cameraService.getAllCameras());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取摄像头")
    public Result<Camera> getById(@PathVariable Long id) {
        Optional<Camera> camera = cameraService.getCameraById(id);
        return camera.map(Result::success).orElse(Result.error("摄像头不存在"));
    }

    @PostMapping
    @Operation(summary = "创建摄像头")
    public Result<Camera> create(@RequestBody Camera camera) {
        return Result.success(cameraService.saveCamera(camera));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新摄像头")
    public Result<Camera> update(@PathVariable Long id, @RequestBody Camera camera) {
        camera.setId(id);
        return Result.success(cameraService.saveCamera(camera));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除摄像头")
    public Result<Void> delete(@PathVariable Long id) {
        cameraService.deleteCamera(id);
        return Result.success();
    }
}
