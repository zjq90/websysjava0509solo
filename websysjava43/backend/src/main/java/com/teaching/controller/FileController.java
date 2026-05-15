package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.entity.CourseMaterial;
import com.teaching.entity.CourseVideo;
import com.teaching.service.FileService;
import com.teaching.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/file")
@Tag(name = "文件管理", description = "课程资料和视频的上传下载")
@CrossOrigin
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private JwtUtils jwtUtils;

    private Long getUserIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtils.getUserIdFromToken(token);
    }

    @PostMapping("/material/upload")
    @Operation(summary = "上传课程资料")
    public Result<CourseMaterial> uploadMaterial(@RequestHeader("Authorization") String token,
                                                  @RequestParam Long courseId,
                                                  @RequestParam String title,
                                                  @RequestParam(required = false) String description,
                                                  @RequestParam MultipartFile file) throws IOException {
        Long userId = getUserIdFromToken(token);
        CourseMaterial material = fileService.uploadMaterial(courseId, title, description, file, userId);
        return Result.success("上传成功", material);
    }

    @GetMapping("/material/list/{courseId}")
    @Operation(summary = "获取课程资料列表")
    public Result<List<CourseMaterial>> getCourseMaterials(@PathVariable Long courseId) {
        List<CourseMaterial> materials = fileService.getCourseMaterials(courseId);
        return Result.success(materials);
    }

    @GetMapping("/material/download/{id}")
    @Operation(summary = "下载课程资料")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable Long id) throws MalformedURLException, UnsupportedEncodingException {
        Resource resource = fileService.downloadMaterial(id);
        CourseMaterial material = fileService.getCourseMaterials(1L).stream()
                .filter(m -> m.getId().equals(id)).findFirst().orElse(null);
        String fileName = material != null ? material.getFileName() : "download";
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }

    @DeleteMapping("/material/{id}")
    @Operation(summary = "删除课程资料")
    public Result<Void> deleteMaterial(@PathVariable Long id) {
        fileService.deleteMaterial(id);
        return Result.success("删除成功");
    }

    @PostMapping("/video/upload")
    @Operation(summary = "上传课程视频")
    public Result<CourseVideo> uploadVideo(@RequestHeader("Authorization") String token,
                                           @RequestParam Long courseId,
                                           @RequestParam String title,
                                           @RequestParam(required = false) String description,
                                           @RequestParam MultipartFile file) throws IOException {
        Long userId = getUserIdFromToken(token);
        CourseVideo video = fileService.uploadVideo(courseId, title, description, file, userId);
        return Result.success("上传成功", video);
    }

    @GetMapping("/video/list/{courseId}")
    @Operation(summary = "获取课程视频列表")
    public Result<List<CourseVideo>> getCourseVideos(@PathVariable Long courseId) {
        List<CourseVideo> videos = fileService.getCourseVideos(courseId);
        return Result.success(videos);
    }

    @GetMapping("/video/play/{id}")
    @Operation(summary = "播放课程视频")
    public ResponseEntity<Resource> playVideo(@PathVariable Long id) throws MalformedURLException {
        Resource resource = fileService.getVideoResource(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }

    @DeleteMapping("/video/{id}")
    @Operation(summary = "删除课程视频")
    public Result<Void> deleteVideo(@PathVariable Long id) {
        fileService.deleteVideo(id);
        return Result.success("删除成功");
    }
}
