package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.entity.Album;
import com.photostudio.entity.Photo;
import com.photostudio.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 云相册控制器
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/albums")
@Tag(name = "云相册管理", description = "相册及照片的增删改查")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    @Operation(summary = "查询所有相册")
    public Result<List<Album>> findAll() {
        return albumService.findAll();
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询相册")
    public Result<Page<Album>> findPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size) {
        return albumService.findPage(page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询相册")
    public Result<Album> findById(@Parameter(description = "相册ID") @PathVariable Long id) {
        return albumService.findById(id);
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "根据订单ID查询相册")
    public Result<Album> findByOrderId(@Parameter(description = "订单ID") @PathVariable Long orderId) {
        return albumService.findByOrderId(orderId);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "根据客户ID查询相册")
    public Result<List<Album>> findByCustomerId(@Parameter(description = "客户ID") @PathVariable Long customerId) {
        return albumService.findByCustomerId(customerId);
    }

    @GetMapping("/{id}/statistics")
    @Operation(summary = "获取相册统计信息")
    public Result<Map<String, Object>> getStatistics(@Parameter(description = "相册ID") @PathVariable Long id) {
        return albumService.getStatistics(id);
    }

    @PostMapping
    @Operation(summary = "创建相册")
    public Result<Album> create(@RequestBody Album album) {
        return albumService.create(album);
    }

    @PutMapping
    @Operation(summary = "更新相册")
    public Result<Album> update(@RequestBody Album album) {
        return albumService.update(album);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除相册")
    public Result<Void> delete(@Parameter(description = "相册ID") @PathVariable Long id) {
        return albumService.delete(id);
    }

    // ================ 照片相关接口 ================

    @GetMapping("/{albumId}/photos")
    @Operation(summary = "获取相册照片列表")
    public Result<List<Photo>> getPhotos(@Parameter(description = "相册ID") @PathVariable Long albumId) {
        return albumService.getPhotos(albumId);
    }

    @PostMapping("/{albumId}/photos")
    @Operation(summary = "添加照片到相册")
    public Result<Photo> addPhoto(
            @Parameter(description = "相册ID") @PathVariable Long albumId,
            @RequestBody Photo photo) {
        return albumService.addPhoto(albumId, photo);
    }

    @PostMapping("/{albumId}/photos/batch")
    @Operation(summary = "批量添加照片")
    public Result<List<Photo>> addPhotos(
            @Parameter(description = "相册ID") @PathVariable Long albumId,
            @RequestBody List<Photo> photos) {
        return albumService.addPhotos(albumId, photos);
    }

    @PutMapping("/photos/{photoId}/mark")
    @Operation(summary = "更新照片标记")
    public Result<Photo> updatePhotoMark(
            @Parameter(description = "照片ID") @PathVariable Long photoId,
            @Parameter(description = "标记类型") @RequestParam(required = false) String markType,
            @Parameter(description = "是否选中") @RequestParam(required = false) Integer isSelected) {
        return albumService.updatePhotoMark(photoId, markType, isSelected);
    }

    @PutMapping("/photos/{photoId}/retouch")
    @Operation(summary = "更新修图状态")
    public Result<Photo> updateRetouchStatus(
            @Parameter(description = "照片ID") @PathVariable Long photoId,
            @Parameter(description = "修图状态") @RequestParam Integer retouchStatus,
            @Parameter(description = "修图师ID") @RequestParam(required = false) Long retoucherId,
            @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        return albumService.updateRetouchStatus(photoId, retouchStatus, retoucherId, remark);
    }
}
