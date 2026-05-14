package com.appsys.controller;

import com.appsys.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/file")
@Tag(name = "文件上传", description = "文件上传相关接口")
public class FileController {

    @Value("${app.upload.path:./uploads}")
    private String uploadPath;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String randomStr = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String newFilename = dateStr + "_" + randomStr + extension;

        File dateDir = new File(uploadPath, dateStr);
        if (!dateDir.exists()) {
            dateDir.mkdirs();
        }

        File destFile = new File(dateDir, newFilename);
        try {
            file.transferTo(destFile);
            
            Map<String, String> result = new HashMap<>();
            result.put("filename", newFilename);
            result.put("originalName", originalFilename);
            result.put("url", "/uploads/" + dateStr + "/" + newFilename);
            
            return Result.success(result);
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-upload")
    @Operation(summary = "批量上传文件")
    public Result<List<Map<String, String>>> batchUpload(@RequestParam("files") MultipartFile[] files) {
        List<Map<String, String>> results = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                
                String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String randomStr = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
                String newFilename = dateStr + "_" + randomStr + extension;

                File dateDir = new File(uploadPath, dateStr);
                if (!dateDir.exists()) {
                    dateDir.mkdirs();
                }

                File destFile = new File(dateDir, newFilename);
                try {
                    file.transferTo(destFile);
                    
                    Map<String, String> result = new HashMap<>();
                    result.put("filename", newFilename);
                    result.put("originalName", originalFilename);
                    result.put("url", "/uploads/" + dateStr + "/" + newFilename);
                    results.add(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return Result.success(results);
    }
}
