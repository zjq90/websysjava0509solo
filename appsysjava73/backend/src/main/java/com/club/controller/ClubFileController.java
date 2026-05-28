package com.club.controller;

import com.club.common.Result;
import com.club.entity.ClubFile;
import com.club.service.ClubFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 社团网盘控制器
 *
 * @author club-management
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/clubs/{clubId}/files")
@Tag(name = "社团网盘", description = "社团文件上传、下载、管理相关接口")
public class ClubFileController {

    @Autowired
    private ClubFileService clubFileService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "社团成员上传文件到网盘")
    public Result<ClubFile> uploadFile(
            @PathVariable Long clubId,
            @RequestParam(required = false) String folder,
            @RequestParam(required = false) String description,
            @RequestParam("file") MultipartFile file,
            @RequestAttribute("userId") Long userId) throws IOException {
        ClubFile saved = clubFileService.uploadFile(clubId, folder, description, file, userId);
        return Result.success(saved);
    }

    /**
     * 获取文件列表
     */
    @GetMapping
    @Operation(summary = "获取文件列表", description = "获取社团网盘文件列表，支持按文件夹、文件类型筛选")
    public Result<List<ClubFile>> getFileList(
            @PathVariable Long clubId,
            @RequestParam(required = false) String folder,
            @RequestParam(required = false) String fileType) {
        List<ClubFile> files = clubFileService.getFileList(clubId, folder, fileType);
        return Result.success(files);
    }

    /**
     * 下载文件
     */
    @GetMapping("/{fileId}/download")
    @Operation(summary = "下载文件", description = "下载文件，返回文件信息")
    public Result<ClubFile> downloadFile(@PathVariable Long clubId, @PathVariable Long fileId) {
        ClubFile file = clubFileService.downloadFile(fileId);
        return Result.success(file);
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{fileId}")
    @Operation(summary = "删除文件", description = "删除网盘文件")
    public Result<Void> deleteFile(
            @PathVariable Long clubId,
            @PathVariable Long fileId,
            @RequestAttribute("userId") Long userId) {
        clubFileService.deleteFile(fileId, userId);
        return Result.success();
    }
}
