package com.petclinic.controller;

import com.petclinic.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/file")
@Tag(name = "文件管理", description = "文件上传下载")
public class FileUploadController {

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @PostMapping("/upload")
    @Operation(summary = "文件上传", description = "上传图片等文件")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String newFilename = UUID.randomUUID().toString() + extension;
            
            String relativePath = dateStr + "/" + newFilename;
            Path filePath = Paths.get(uploadPath, relativePath);
            
            Files.createDirectories(filePath.getParent());
            file.transferTo(filePath.toFile());
            
            return Result.success("/uploads/" + relativePath);
        } catch (IOException e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }
}
