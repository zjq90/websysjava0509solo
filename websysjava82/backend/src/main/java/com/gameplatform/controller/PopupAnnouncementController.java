package com.gameplatform.controller;

import com.gameplatform.common.Result;
import com.gameplatform.entity.PopupAnnouncement;
import com.gameplatform.service.PopupAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/popup-announcements")
public class PopupAnnouncementController {
    @Autowired
    private PopupAnnouncementService popupAnnouncementService;

    @GetMapping
    public Result<List<PopupAnnouncement>> findAll() {
        return Result.success(popupAnnouncementService.findAll());
    }

    @GetMapping("/active")
    public Result<List<PopupAnnouncement>> findAllActive() {
        return Result.success(popupAnnouncementService.findAllActive());
    }

    @GetMapping("/active/trigger/{trigger}")
    public Result<List<PopupAnnouncement>> findActiveByTrigger(@PathVariable String trigger) {
        return Result.success(popupAnnouncementService.findActiveByTrigger(trigger));
    }

    @GetMapping("/{id}")
    public Result<PopupAnnouncement> findById(@PathVariable Long id) {
        return popupAnnouncementService.findById(id)
                .map(Result::success)
                .orElse(Result.error("公告不存在"));
    }

    @PostMapping
    public Result<PopupAnnouncement> save(@RequestBody PopupAnnouncement announcement) {
        return Result.success(popupAnnouncementService.save(announcement));
    }

    @PutMapping("/{id}")
    public Result<PopupAnnouncement> update(@PathVariable Long id, @RequestBody PopupAnnouncement announcement) {
        return Result.success(popupAnnouncementService.update(id, announcement));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        popupAnnouncementService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/{id}/toggle")
    public Result<PopupAnnouncement> toggleActive(@PathVariable Long id) {
        PopupAnnouncement announcement = popupAnnouncementService.toggleActive(id);
        if (announcement != null) {
            return Result.success(announcement);
        }
        return Result.error("公告不存在");
    }

    @PostMapping("/{id}/view")
    public Result<Void> incrementViewCount(@PathVariable Long id) {
        popupAnnouncementService.incrementViewCount(id);
        return Result.success();
    }

    @PostMapping("/{id}/click")
    public Result<Void> incrementClickCount(@PathVariable Long id) {
        popupAnnouncementService.incrementClickCount(id);
        return Result.success();
    }
}
