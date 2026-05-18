package com.flower.controller;

import com.flower.common.Result;
import com.flower.entity.Announcement;
import com.flower.entity.Article;
import com.flower.entity.MarketingCampaign;
import com.flower.service.ContentService;
import com.flower.service.MarketingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 内容管理控制器
 */
@RestController
@RequestMapping("/api/content")
@Api(tags = "内容管理接口")
@CrossOrigin
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private MarketingService marketingService;

    // ========== 公告管理 ==========

    @GetMapping("/announcements")
    @ApiOperation("分页查询公告列表")
    public Result<Page<Announcement>> listAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(contentService.findAllAnnouncements(pageable));
    }

    @GetMapping("/announcements/{id}")
    @ApiOperation("根据ID查询公告")
    public Result<Announcement> getAnnouncementById(@PathVariable Long id) {
        Announcement announcement = contentService.findAnnouncementById(id);
        return announcement != null ? Result.success(announcement) : Result.error("公告不存在");
    }

    @PostMapping("/announcements")
    @ApiOperation("新增公告")
    public Result<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        return Result.success(contentService.saveAnnouncement(announcement));
    }

    @PutMapping("/announcements/{id}")
    @ApiOperation("更新公告")
    public Result<Announcement> updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement) {
        announcement.setId(id);
        return Result.success(contentService.saveAnnouncement(announcement));
    }

    @DeleteMapping("/announcements/{id}")
    @ApiOperation("删除公告")
    public Result<Void> deleteAnnouncement(@PathVariable Long id) {
        contentService.deleteAnnouncement(id);
        return Result.success();
    }

    @PutMapping("/announcements/{id}/publish")
    @ApiOperation("发布公告")
    public Result<Announcement> publishAnnouncement(@PathVariable Long id) {
        Announcement announcement = contentService.publishAnnouncement(id);
        return announcement != null ? Result.success(announcement) : Result.error("公告不存在");
    }

    @PutMapping("/announcements/{id}/top")
    @ApiOperation("置顶/取消置顶")
    public Result<Announcement> toggleTop(@PathVariable Long id) {
        Announcement announcement = contentService.toggleTop(id);
        return announcement != null ? Result.success(announcement) : Result.error("公告不存在");
    }

    @GetMapping("/announcements/published")
    @ApiOperation("获取已发布的公告列表")
    public Result<List<Announcement>> getPublishedAnnouncements() {
        return Result.success(contentService.getPublishedAnnouncements());
    }

    // ========== 文章管理 ==========

    @GetMapping("/articles")
    @ApiOperation("分页查询文章列表")
    public Result<Page<Article>> listArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(contentService.findAllArticles(pageable));
    }

    @GetMapping("/articles/{id}")
    @ApiOperation("根据ID查询文章")
    public Result<Article> getArticleById(@PathVariable Long id) {
        Article article = contentService.findArticleById(id);
        return article != null ? Result.success(article) : Result.error("文章不存在");
    }

    @PostMapping("/articles")
    @ApiOperation("新增文章")
    public Result<Article> createArticle(@RequestBody Article article) {
        return Result.success(contentService.saveArticle(article));
    }

    @PutMapping("/articles/{id}")
    @ApiOperation("更新文章")
    public Result<Article> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        article.setId(id);
        return Result.success(contentService.saveArticle(article));
    }

    @DeleteMapping("/articles/{id}")
    @ApiOperation("删除文章")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        contentService.deleteArticle(id);
        return Result.success();
    }

    @PutMapping("/articles/{id}/publish")
    @ApiOperation("发布文章")
    public Result<Article> publishArticle(@PathVariable Long id) {
        Article article = contentService.publishArticle(id);
        return article != null ? Result.success(article) : Result.error("文章不存在");
    }

    @GetMapping("/articles/published")
    @ApiOperation("获取已发布的文章列表")
    public Result<List<Article>> getPublishedArticles() {
        return Result.success(contentService.getPublishedArticles());
    }

    @GetMapping("/articles/category/{category}")
    @ApiOperation("根据分类查询文章")
    public Result<List<Article>> getArticlesByCategory(@PathVariable Integer category) {
        return Result.success(contentService.getArticlesByCategory(category));
    }

    // ========== 营销活动 ==========

    @GetMapping("/campaigns")
    @ApiOperation("分页查询营销活动列表")
    public Result<Page<MarketingCampaign>> listCampaigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(marketingService.findAll(pageable));
    }

    @GetMapping("/campaigns/{id}")
    @ApiOperation("根据ID查询营销活动")
    public Result<MarketingCampaign> getCampaignById(@PathVariable Long id) {
        MarketingCampaign campaign = marketingService.findById(id);
        return campaign != null ? Result.success(campaign) : Result.error("活动不存在");
    }

    @PostMapping("/campaigns")
    @ApiOperation("新增营销活动")
    public Result<MarketingCampaign> createCampaign(@RequestBody MarketingCampaign campaign) {
        return Result.success(marketingService.save(campaign));
    }

    @PutMapping("/campaigns/{id}")
    @ApiOperation("更新营销活动")
    public Result<MarketingCampaign> updateCampaign(@PathVariable Long id, @RequestBody MarketingCampaign campaign) {
        campaign.setId(id);
        return Result.success(marketingService.save(campaign));
    }

    @DeleteMapping("/campaigns/{id}")
    @ApiOperation("删除营销活动")
    public Result<Void> deleteCampaign(@PathVariable Long id) {
        marketingService.delete(id);
        return Result.success();
    }

    @PutMapping("/campaigns/{id}/status")
    @ApiOperation("更新活动状态")
    public Result<MarketingCampaign> updateCampaignStatus(@PathVariable Long id, @RequestParam Integer status) {
        MarketingCampaign campaign = marketingService.updateStatus(id, status);
        return campaign != null ? Result.success(campaign) : Result.error("活动不存在");
    }

    @GetMapping("/campaigns/active")
    @ApiOperation("获取进行中的活动列表")
    public Result<List<MarketingCampaign>> getActiveCampaigns() {
        return Result.success(marketingService.getActiveCampaigns());
    }
}
