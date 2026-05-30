package com.gameplatform.controller;

import com.gameplatform.common.Result;
import com.gameplatform.entity.Advertisement;
import com.gameplatform.service.AdvertisementService;
import com.gameplatform.service.RevenueStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advertisements")
public class AdvertisementController {
    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private RevenueStatsService revenueStatsService;

    @GetMapping
    public Result<List<Advertisement>> findAll() {
        return Result.success(advertisementService.findAll());
    }

    @GetMapping("/active")
    public Result<List<Advertisement>> findAllActive() {
        return Result.success(advertisementService.findAllActive());
    }

    @GetMapping("/slot/{adSlotId}")
    public Result<List<Advertisement>> findByAdSlotId(@PathVariable Long adSlotId) {
        return Result.success(advertisementService.findByAdSlotId(adSlotId));
    }

    @GetMapping("/slot/{adSlotId}/active")
    public Result<List<Advertisement>> findActiveByAdSlotId(@PathVariable Long adSlotId) {
        return Result.success(advertisementService.findActiveByAdSlotId(adSlotId));
    }

    @GetMapping("/{id}")
    public Result<Advertisement> findById(@PathVariable Long id) {
        return advertisementService.findById(id)
                .map(Result::success)
                .orElse(Result.error("广告不存在"));
    }

    @PostMapping
    public Result<Advertisement> save(@RequestBody Advertisement advertisement) {
        return Result.success(advertisementService.save(advertisement));
    }

    @PutMapping("/{id}")
    public Result<Advertisement> update(@PathVariable Long id, @RequestBody Advertisement advertisement) {
        return Result.success(advertisementService.update(id, advertisement));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        advertisementService.deleteById(id);
        return Result.success();
    }

    @PutMapping("/{id}/toggle")
    public Result<Advertisement> toggleActive(@PathVariable Long id) {
        Advertisement ad = advertisementService.toggleActive(id);
        if (ad != null) {
            return Result.success(ad);
        }
        return Result.error("广告不存在");
    }

    @PostMapping("/{id}/impression")
    public Result<Void> recordImpression(@PathVariable Long id) {
        advertisementService.findById(id).ifPresent(ad -> {
            revenueStatsService.recordImpression(ad.getAdSlotId());
        });
        return Result.success();
    }

    @PostMapping("/{id}/click")
    public Result<Void> recordClick(@PathVariable Long id) {
        advertisementService.findById(id).ifPresent(ad -> {
            revenueStatsService.recordClick(ad.getAdSlotId());
        });
        return Result.success();
    }
}
