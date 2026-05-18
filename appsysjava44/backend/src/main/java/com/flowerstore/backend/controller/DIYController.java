package com.flowerstore.backend.controller;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.DIYBouquet;
import com.flowerstore.backend.service.DIYService;
import com.flowerstore.backend.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * DIY花束控制器
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/diy")
@Tag(name = "DIY接口", description = "DIY花束设计与管理")
public class DIYController {

    @Autowired
    private DIYService diyService;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 保存DIY花束
     */
    @PostMapping("/save")
    @Operation(summary = "保存花束", description = "保存DIY花束设计")
    public Result<DIYBouquet> saveDIYBouquet(@RequestBody DIYBouquet bouquet, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return diyService.saveDIYBouquet(userId, bouquet);
    }

    /**
     * 获取用户DIY花束列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取我的花束", description = "获取用户保存的DIY花束列表")
    public Result<List<DIYBouquet>> getDIYBouquetList(
            @RequestParam(required = false) Integer status,
            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return diyService.getUserDIYBouquets(userId, status);
    }

    /**
     * 获取DIY花束详情
     */
    @GetMapping("/{bouquetId}")
    @Operation(summary = "获取花束详情", description = "获取DIY花束的详细信息")
    public Result<DIYBouquet> getDIYBouquetDetail(@PathVariable Long bouquetId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return diyService.getDIYBouquetDetail(userId, bouquetId);
    }

    /**
     * 更新DIY花束
     */
    @PutMapping("/{bouquetId}")
    @Operation(summary = "更新花束", description = "更新DIY花束设计")
    public Result<DIYBouquet> updateDIYBouquet(@PathVariable Long bouquetId, @RequestBody DIYBouquet bouquet, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return diyService.updateDIYBouquet(userId, bouquetId, bouquet);
    }

    /**
     * 删除DIY花束
     */
    @DeleteMapping("/{bouquetId}")
    @Operation(summary = "删除花束", description = "删除DIY花束")
    public Result<String> deleteDIYBouquet(@PathVariable Long bouquetId, HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return diyService.deleteDIYBouquet(userId, bouquetId);
    }

    /**
     * 从token中获取用户ID
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}
