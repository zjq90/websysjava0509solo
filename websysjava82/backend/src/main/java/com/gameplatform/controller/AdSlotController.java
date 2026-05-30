package com.gameplatform.controller;

import com.gameplatform.common.Result;
import com.gameplatform.entity.AdSlot;
import com.gameplatform.service.AdSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ad-slots")
public class AdSlotController {
    @Autowired
    private AdSlotService adSlotService;

    @GetMapping
    public Result<List<AdSlot>> findAll() {
        return Result.success(adSlotService.findAll());
    }

    @GetMapping("/enabled")
    public Result<List<AdSlot>> findAllEnabled() {
        return Result.success(adSlotService.findAllEnabled());
    }

    @GetMapping("/{id}")
    public Result<AdSlot> findById(@PathVariable Long id) {
        return adSlotService.findById(id)
                .map(Result::success)
                .orElse(Result.error("广告位不存在"));
    }

    @GetMapping("/code/{code}")
    public Result<AdSlot> findByCode(@PathVariable String code) {
        return adSlotService.findByCode(code)
                .map(Result::success)
                .orElse(Result.error("广告位不存在"));
    }

    @PostMapping
    public Result<AdSlot> save(@RequestBody AdSlot adSlot) {
        return Result.success(adSlotService.save(adSlot));
    }

    @PutMapping("/{id}")
    public Result<AdSlot> update(@PathVariable Long id, @RequestBody AdSlot adSlot) {
        return Result.success(adSlotService.update(id, adSlot));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adSlotService.deleteById(id);
        return Result.success();
    }
}
