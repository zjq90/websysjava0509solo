package com.agriculture.controller;

import com.agriculture.common.Result;
import com.agriculture.entity.PestImage;
import com.agriculture.service.PestImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 病虫害图片控制器
 * 处理病虫害图片的增删改查操作
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Tag(name = "病虫害图片管理", description = "病虫害图像的上传、查询、删除操作")
@RestController
@RequestMapping("/pest-images")
@CrossOrigin(origins = "*")
public class PestImageController {

    @Autowired
    private PestImageService pestImageService;

    /**
     * 根据田间记录ID查询图片
     */
    @Operation(summary = "查询记录图片", description = "查询某个田间记录关联的所有图片")
    @GetMapping("/record/{fieldRecordId}")
    public Result<List<PestImage>> getByRecordId(
            @Parameter(description = "田间记录ID") @PathVariable Long fieldRecordId) {
        return Result.success(pestImageService.findByFieldRecordId(fieldRecordId));
    }

    /**
     * 根据图片类型查询
     */
    @Operation(summary = "按类型查询图片", description = "根据图片类型（病虫害、植株、叶片等）筛选")
    @GetMapping("/type/{type}")
    public Result<List<PestImage>> getByType(
            @Parameter(description = "图片类型：PEST/PLANT/LEAF/ROOT") @PathVariable String type) {
        return Result.success(pestImageService.findByImageType(type));
    }

    /**
     * 根据ID获取图片详情
     */
    @Operation(summary = "获取图片详情", description = "根据ID获取图片详细信息")
    @GetMapping("/{id}")
    public Result<PestImage> getById(@Parameter(description = "图片ID") @PathVariable Long id) {
        Optional<PestImage> imageOpt = pestImageService.findById(id);
        if (imageOpt.isPresent()) {
            return Result.success(imageOpt.get());
        } else {
            return Result.notFound("图片不存在");
        }
    }

    /**
     * 批量保存图片信息
     */
    @Operation(summary = "批量保存图片", description = "一次保存多张图片信息")
    @PostMapping("/batch")
    public Result<List<PestImage>> saveBatch(@RequestBody List<PestImage> images) {
        try {
            List<PestImage> saved = pestImageService.saveAll(images);
            return Result.success("保存成功", saved);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 保存单张图片
     */
    @Operation(summary = "保存图片", description = "保存单张图片信息")
    @PostMapping
    public Result<PestImage> save(@RequestBody PestImage pestImage) {
        try {
            PestImage saved = pestImageService.save(pestImage);
            return Result.success("保存成功", saved);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新图片信息
     */
    @Operation(summary = "更新图片", description = "修改图片信息（如描述、识别结果等）")
    @PutMapping
    public Result<PestImage> update(@RequestBody PestImage pestImage) {
        try {
            PestImage updated = pestImageService.update(pestImage);
            return Result.success("更新成功", updated);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 删除图片（逻辑删除）
     */
    @Operation(summary = "删除图片", description = "根据ID删除图片（逻辑删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "图片ID") @PathVariable Long id) {
        pestImageService.deleteById(id);
        return Result.success();
    }

    /**
     * 统计记录的图片数量
     */
    @Operation(summary = "统计图片数量", description = "统计某个田间记录的图片数量")
    @GetMapping("/count/{fieldRecordId}")
    public Result<Long> countByRecord(@Parameter(description = "田间记录ID") @PathVariable Long fieldRecordId) {
        return Result.success(pestImageService.countByFieldRecordId(fieldRecordId));
    }
}
