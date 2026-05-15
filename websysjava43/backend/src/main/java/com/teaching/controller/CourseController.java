package com.teaching.controller;

import com.teaching.common.Result;
import com.teaching.entity.Course;
import com.teaching.entity.User;
import com.teaching.service.CourseService;
import com.teaching.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@Tag(name = "课程管理", description = "课程的增删改查和选课管理")
@CrossOrigin
public class CourseController {

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

    @GetMapping("/list")
    @Operation(summary = "获取所有课程")
    public Result<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return Result.success(courses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取课程详情")
    public Result<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getById(id);
        return Result.success(course);
    }

    @PostMapping("/create")
    @Operation(summary = "创建课程")
    public Result<Course> createCourse(@RequestHeader("Authorization") String token, @RequestBody Course course) {
        Long userId = getUserIdFromToken(token);
        Course savedCourse = courseService.createCourse(course, userId);
        return Result.success("课程创建成功", savedCourse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新课程")
    public Result<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return Result.success("课程更新成功", updatedCourse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除课程")
    public Result<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return Result.success("课程删除成功");
    }

    @GetMapping("/my-courses")
    @Operation(summary = "获取我的课程")
    public Result<List<Course>> getMyCourses(@RequestHeader("Authorization") String token) {
        Long userId = getUserIdFromToken(token);
        List<Course> courses = courseService.getUserCourses(userId);
        return Result.success(courses);
    }

    @PostMapping("/{id}/enroll")
    @Operation(summary = "选修课程")
    public Result<Void> enrollCourse(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Long userId = getUserIdFromToken(token);
        courseService.enrollCourse(id, userId);
        return Result.success("选课成功");
    }

    @PostMapping("/{id}/drop")
    @Operation(summary = "退选课程")
    public Result<Void> dropCourse(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Long userId = getUserIdFromToken(token);
        courseService.dropCourse(id, userId);
        return Result.success("退课成功");
    }

    @GetMapping("/{id}/students")
    @Operation(summary = "获取课程学生列表")
    public Result<List<User>> getCourseStudents(@PathVariable Long id) {
        List<User> students = courseService.getCourseStudents(id);
        students.forEach(s -> s.setPassword(null));
        return Result.success(students);
    }

    @GetMapping("/{id}/is-enrolled")
    @Operation(summary = "检查是否已选课")
    public Result<Boolean> isEnrolled(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Long userId = getUserIdFromToken(token);
        boolean enrolled = courseService.isEnrolled(id, userId);
        return Result.success(enrolled);
    }
}
