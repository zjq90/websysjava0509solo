package com.example.websys.controller;

import com.example.websys.dto.Result;
import com.example.websys.entity.BusinessData;
import com.example.websys.service.BusinessDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 经营数据REST API控制器
 * 提供经营数据的增删改查接口
 */
@RestController
@RequestMapping("/api/business-data")
@CrossOrigin(origins = "*")
public class BusinessDataController {

    @Autowired
    private BusinessDataService businessDataService;

    @GetMapping
    public Result<List<BusinessData>> getAll() {
        return Result.success(businessDataService.getAll());
    }

    @GetMapping("/{id}")
    public Result<BusinessData> getById(@PathVariable Long id) {
        Optional<BusinessData> data = businessDataService.getById(id);
        return data.map(Result::success).orElse(Result.error("数据不存在"));
    }

    @GetMapping("/date/{date}")
    public Result<List<BusinessData>> getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.success(businessDataService.getByDate(date));
    }

    @GetMapping("/range")
    public Result<List<BusinessData>> getByStatCodeAndDateRange(
            @RequestParam String statCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(businessDataService.getByStatCodeAndDateRange(statCode, startDate, endDate));
    }

    @PostMapping
    public Result<BusinessData> save(@RequestBody BusinessData businessData) {
        BusinessData saved = businessDataService.save(businessData);
        return Result.success("保存成功", saved);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        businessDataService.deleteById(id);
        return Result.success();
    }
}
