package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.entity.Course;
import com.teaching.entity.CourseComment;
import com.teaching.service.CommentService;
import com.teaching.service.CourseService;
import com.teaching.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
@Tag(name = "评论管理", description = "课程评论的增删改查")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

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
    @Operation(summary = "发表评论")
    public Result<CourseComment> createComment(@RequestHeader("Authorization") String token,
                                               @RequestBody Map<String, Object> params) {
        Long userId = getUserIdFromToken(token);
        Long courseId = Long.valueOf(params.get("courseId").toString());
        String content = params.get("content").toString();
        
        Course course = courseService.getById(courseId);
        CourseComment comment = commentService.createComment(courseId, course.getName(), content, userId);
        return Result.success("评论成功", comment);
    }

    @GetMapping("/list/{courseId}")
    @Operation(summary = "获取课程评论列表")
    public Result<List<CourseComment>> getCourseComments(@PathVariable Long courseId) {
        List<CourseComment> comments = commentService.getCourseComments(courseId);
        return Result.success(comments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取评论详情")
    public Result<CourseComment> getComment(@PathVariable Long id) {
        CourseComment comment = commentService.getById(id);
        return Result.success(comment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评论")
    public Result<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success("删除成功");
    }
}
