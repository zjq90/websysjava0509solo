package com.psyconsult.controller;

import com.psyconsult.dto.ApiResponse;
import com.psyconsult.entity.CommunityPost;
import com.psyconsult.entity.Resource;
import com.psyconsult.service.AuthService;
import com.psyconsult.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@Tag(name = "资源库接口", description = "心理健康资源、社区帖子等接口")
public class ResourceController {

    private final ResourceService resourceService;
    private final AuthService authService;

    public ResourceController(ResourceService resourceService, AuthService authService) {
        this.resourceService = resourceService;
        this.authService = authService;
    }

    @GetMapping
    @Operation(summary = "获取所有资源", description = "获取所有心理健康资源列表")
    public ApiResponse<List<Resource>> getAllResources() {
        return ApiResponse.success(resourceService.getAllResources());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "按类型获取资源", description = "按类型获取资源，如音频、文章、练习工具")
    public ApiResponse<List<Resource>> getResourcesByType(@PathVariable String type) {
        return ApiResponse.success(resourceService.getResourcesByType(type));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "按分类获取资源", description = "按分类获取资源，如冥想、情绪管理、人际关系")
    public ApiResponse<List<Resource>> getResourcesByCategory(@PathVariable String category) {
        return ApiResponse.success(resourceService.getResourcesByCategory(category));
    }

    @GetMapping("/popular")
    @Operation(summary = "获取热门资源", description = "按浏览量获取热门资源")
    public ApiResponse<List<Resource>> getPopularResources() {
        return ApiResponse.success(resourceService.getPopularResources());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取资源详情", description = "根据ID获取资源详细内容")
    public ApiResponse<Resource> getResourceById(@PathVariable Long id) {
        return ApiResponse.success(resourceService.getResourceById(id));
    }

    @PostMapping("/community/posts")
    @Operation(summary = "发布社区帖子", description = "发布匿名社区分享帖子（需审核）")
    public ApiResponse<CommunityPost> createCommunityPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommunityPost post) {
        Long userId = authService.getCurrentUser(userDetails.getUsername()).getId();
        return ApiResponse.success("帖子发布成功，等待审核", resourceService.createCommunityPost(userId, post));
    }

    @GetMapping("/community/posts")
    @Operation(summary = "获取社区帖子", description = "获取已审核通过的社区帖子")
    public ApiResponse<List<CommunityPost>> getApprovedPosts() {
        return ApiResponse.success(resourceService.getApprovedPosts());
    }

    @GetMapping("/community/my-posts")
    @Operation(summary = "获取我的帖子", description = "获取当前用户发布的所有社区帖子")
    public ApiResponse<List<CommunityPost>> getUserPosts(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = authService.getCurrentUser(userDetails.getUsername()).getId();
        return ApiResponse.success(resourceService.getUserPosts(userId));
    }

    @GetMapping("/community/posts/{id}")
    @Operation(summary = "获取帖子详情", description = "根据ID获取社区帖子详细内容")
    public ApiResponse<CommunityPost> getCommunityPostById(@PathVariable Long id) {
        return ApiResponse.success(resourceService.getCommunityPostById(id));
    }
}
