package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.entity.Course;
import com.teaching.entity.CourseAnnouncement;
import com.teaching.service.AnnouncementService;
import com.teaching.service.CourseService;
import com.teaching.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcement")
@Tag(name = "公告管理", description = "课程公告的增删改查")
@CrossOrigin
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private JwtUtils jwtUtils;

    private Long getUserIdFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtils.getUserIdFromToken(token);
    }

    @PostMapping("/create")
    @Operation(summary = "发布公告")
    public Result<CourseAnnouncement> createAnnouncement(@RequestHeader("Authorization") String token,
                                                         @RequestBody Map<String, Object> params) {
        Long userId = getUserIdFromToken(token);
        Long courseId = Long.valueOf(params.get("courseId").toString());
        String title = params.get("title").toString();
        String content = params.get("content") != null ? params.get("content").toString() : "";
        
        Course course = courseService.getById(courseId);
        CourseAnnouncement announcement = announcementService.createAnnouncement(
                courseId, course.getName(), title, content, userId);
        return Result.success("发布成功", announcement);
    }

    @GetMapping("/list/{courseId}")
    @Operation(summary = "获取课程公告列表")
    public Result<List<CourseAnnouncement>> getCourseAnnouncements(@PathVariable Long courseId) {
        List<CourseAnnouncement> announcements = announcementService.getCourseAnnouncements(courseId);
        return Result.success(announcements);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取公告详情")
    public Result<CourseAnnouncement> getAnnouncement(@PathVariable Long id) {
        CourseAnnouncement announcement = announcementService.getById(id);
        return Result.success(announcement);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新公告")
    public Result<CourseAnnouncement> updateAnnouncement(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String title = params.get("title");
        String content = params.get("content");
        CourseAnnouncement announcement = announcementService.updateAnnouncement(id, title, content);
        return Result.success("更新成功", announcement);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除公告")
    public Result<Void> deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return Result.success("删除成功");
    }

    @PostMapping("/{id}/toggle-top")
    @Operation(summary = "切换置顶状态")
    public Result<CourseAnnouncement> toggleTop(@PathVariable Long id) {
        CourseAnnouncement announcement = announcementService.toggleTop(id);
        return Result.success(announcement.getIsTop() ? "已置顶" : "已取消置顶", announcement);
    }
}
