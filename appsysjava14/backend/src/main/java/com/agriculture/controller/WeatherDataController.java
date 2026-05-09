package com.agriculture.controller;

import com.agriculture.common.Result;
import com.agriculture.entity.WeatherData;
import com.agriculture.service.WeatherDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 气象数据控制器
 * 对接气象API，实时获取并记录种植区域环境数据
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Tag(name = "气象数据管理", description = "气象数据的获取、查询操作")
@RestController
@RequestMapping("/weather")
@CrossOrigin(origins = "*")
public class WeatherDataController {

    @Autowired
    private WeatherDataService weatherDataService;

    /**
     * 查询所有气象数据
     */
    @Operation(summary = "查询所有气象数据", description = "获取所有气象记录")
    @GetMapping
    public Result<List<WeatherData>> list() {
        return Result.success(weatherDataService.findAll());
    }

    /**
     * 根据ID获取详情
     */
    @Operation(summary = "获取气象详情", description = "根据ID获取气象数据详细信息")
    @GetMapping("/{id}")
    public Result<WeatherData> getById(@Parameter(description = "气象数据ID") @PathVariable Long id) {
        Optional<WeatherData> dataOpt = weatherDataService.findById(id);
        if (dataOpt.isPresent()) {
            return Result.success(dataOpt.get());
        } else {
            return Result.notFound("气象数据不存在");
        }
    }

    /**
     * 根据地块ID查询气象数据
     */
    @Operation(summary = "按地块查询气象", description = "查询某个地块的气象数据")
    @GetMapping("/plot/{plotId}")
    public Result<List<WeatherData>> getByPlotId(@Parameter(description = "地块ID") @PathVariable Long plotId) {
        return Result.success(weatherDataService.findByPlotId(plotId));
    }

    /**
     * 根据城市编码查询
     */
    @Operation(summary = "按城市查询气象", description = "查询某个城市的气象数据")
    @GetMapping("/city/{cityCode}")
    public Result<List<WeatherData>> getByCityCode(@Parameter(description = "城市编码") @PathVariable String cityCode) {
        return Result.success(weatherDataService.findByCityCode(cityCode));
    }

    /**
     * 查询地块在时间范围内的气象数据
     */
    @Operation(summary = "按时间范围查询", description = "查询某个地块在指定时间范围内的气象数据")
    @GetMapping("/plot/{plotId}/range")
    public Result<List<WeatherData>> getByDateRange(
            @Parameter(description = "地块ID") @PathVariable Long plotId,
            @Parameter(description = "开始时间") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(weatherDataService.findByPlotIdAndDateRange(plotId, startTime, endTime));
    }

    /**
     * 从气象API获取最新数据（模拟）
     */
    @Operation(summary = "获取最新气象", description = "模拟从气象API获取指定城市的最新气象数据")
    @PostMapping("/fetch")
    public Result<WeatherData> fetchFromApi(
            @Parameter(description = "城市编码") @RequestParam String cityCode,
            @Parameter(description = "地块ID") @RequestParam(required = false) Long plotId) {
        try {
            WeatherData data = weatherDataService.fetchFromApi(cityCode, plotId);
            return Result.success("获取成功", data);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取城市最新气象数据
     */
    @Operation(summary = "获取城市最新气象", description = "获取某个城市的最新气象数据")
    @GetMapping("/latest/{cityCode}")
    public Result<WeatherData> getLatest(@Parameter(description = "城市编码") @PathVariable String cityCode) {
        WeatherData data = weatherDataService.getLatestWeather(cityCode);
        if (data != null) {
            return Result.success(data);
        } else {
            return Result.notFound("暂无气象数据，请先调用fetch接口获取");
        }
    }
}
