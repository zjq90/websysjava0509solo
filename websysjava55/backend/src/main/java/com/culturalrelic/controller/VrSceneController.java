package com.culturalrelic.controller;

import com.culturalrelic.common.Result;
import com.culturalrelic.entity.VrScene;
import com.culturalrelic.service.VrSceneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vr-scene")
@Tag(name = "VR场景管理", description = "VR虚拟博物馆场景管理，用户可'走进'虚拟博物馆观看文物")
public class VrSceneController {

    @Autowired
    private VrSceneService vrSceneService;

    @PostMapping
    @Operation(summary = "新增VR场景", description = "创建新的VR虚拟博物馆场景")
    public Result<VrScene> save(@RequestBody VrScene scene) {
        return Result.success(vrSceneService.save(scene));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询VR场景详情", description = "根据ID查询VR场景详情")
    public Result<VrScene> findById(@PathVariable Long id) {
        Optional<VrScene> scene = vrSceneService.findById(id);
        if (scene.isPresent()) {
            return Result.success(scene.get());
        }
        return Result.error("VR场景不存在");
    }

    @GetMapping
    @Operation(summary = "查询所有VR场景", description = "获取所有VR场景列表")
    public Result<List<VrScene>> findAll() {
        return Result.success(vrSceneService.findAll());
    }

    @PutMapping
    @Operation(summary = "更新VR场景", description = "更新VR场景信息")
    public Result<VrScene> update(@RequestBody VrScene scene) {
        return Result.success(vrSceneService.update(scene));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除VR场景", description = "根据ID删除VR场景")
    public Result<Void> delete(@PathVariable Long id) {
        return vrSceneService.delete(id) ? Result.success("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/no/{sceneNo}")
    @Operation(summary = "根据场景编号查询", description = "根据场景编号查询VR场景")
    public Result<VrScene> findBySceneNo(@PathVariable String sceneNo) {
        VrScene scene = vrSceneService.findBySceneNo(sceneNo);
        return scene != null ? Result.success(scene) : Result.error("VR场景不存在");
    }
}
