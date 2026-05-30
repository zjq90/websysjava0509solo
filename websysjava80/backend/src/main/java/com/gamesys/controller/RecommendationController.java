package com.gamesys.controller;

import com.gamesys.common.Result;
import com.gamesys.entity.Recommendation;
import com.gamesys.service.RecommendationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "推荐管理")
@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @ApiOperation("获取推荐列表")
    @GetMapping
    public Result<List<Recommendation>> getRecommendations(
            @RequestParam String type,
            @RequestParam(required = false) Long categoryId) {
        return Result.success(recommendationService.getRecommendations(type, categoryId));
    }

    @ApiOperation("获取首页推荐")
    @GetMapping("/home")
    public Result<List<Recommendation>> getHomeRecommendations() {
        return Result.success(recommendationService.getHomeRecommendations());
    }

    @ApiOperation("获取分类推荐")
    @GetMapping("/category/{categoryId}")
    public Result<List<Recommendation>> getCategoryRecommendations(@PathVariable Long categoryId) {
        return Result.success(recommendationService.getCategoryRecommendations(categoryId));
    }

    @ApiOperation("获取弹窗推荐")
    @GetMapping("/popup")
    public Result<Recommendation> getPopupRecommendation() {
        return Result.success(recommendationService.getPopupRecommendation());
    }

    @ApiOperation("新增推荐")
    @PostMapping
    public Result<Void> saveRecommendation(@RequestBody Recommendation recommendation) {
        recommendationService.saveRecommendation(recommendation);
        return Result.success();
    }

    @ApiOperation("更新推荐")
    @PutMapping("/{id}")
    public Result<Void> updateRecommendation(@PathVariable Long id, @RequestBody Recommendation recommendation) {
        recommendation.setId(id);
        recommendationService.updateRecommendation(recommendation);
        return Result.success();
    }

    @ApiOperation("删除推荐")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRecommendation(@PathVariable Long id) {
        recommendationService.deleteRecommendation(id);
        return Result.success();
    }

    @ApiOperation("手动验证推荐有效性")
    @PostMapping("/validate")
    public Result<Void> validateRecommendations() {
        recommendationService.validateRecommendations();
        return Result.success();
    }
}
