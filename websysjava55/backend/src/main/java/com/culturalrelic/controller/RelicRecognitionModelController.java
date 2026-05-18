package com.culturalrelic.controller;

import com.culturalrelic.common.Result;
import com.culturalrelic.entity.RelicRecognitionModel;
import com.culturalrelic.service.RelicRecognitionModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/relic-recognition-model")
@Tag(name = "文物识别模型管理", description = "AI模型部署管理，自动识别文物类别")
public class RelicRecognitionModelController {

    @Autowired
    private RelicRecognitionModelService relicRecognitionModelService;

    @PostMapping
    @Operation(summary = "新增识别模型", description = "部署新的文物识别AI模型")
    public Result<RelicRecognitionModel> save(@RequestBody RelicRecognitionModel model) {
        return Result.success(relicRecognitionModelService.save(model));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询模型详情", description = "根据ID查询模型详情")
    public Result<RelicRecognitionModel> findById(@PathVariable Long id) {
        Optional<RelicRecognitionModel> model = relicRecognitionModelService.findById(id);
        if (model.isPresent()) {
            return Result.success(model.get());
        }
        return Result.error("模型不存在");
    }

    @GetMapping
    @Operation(summary = "查询所有模型", description = "获取所有识别模型列表")
    public Result<List<RelicRecognitionModel>> findAll() {
        return Result.success(relicRecognitionModelService.findAll());
    }

    @PutMapping
    @Operation(summary = "更新模型", description = "更新模型信息")
    public Result<RelicRecognitionModel> update(@RequestBody RelicRecognitionModel model) {
        return Result.success(relicRecognitionModelService.update(model));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除模型", description = "根据ID删除模型")
    public Result<Void> delete(@PathVariable Long id) {
        return relicRecognitionModelService.delete(id) ? Result.success("删除成功") : Result.error("删除失败");
    }

    @GetMapping("/default")
    @Operation(summary = "获取默认模型", description = "获取系统默认的文物识别模型")
    public Result<RelicRecognitionModel> findDefault() {
        RelicRecognitionModel model = relicRecognitionModelService.findByIsDefault();
        return model != null ? Result.success(model) : Result.error("默认模型不存在");
    }
}
