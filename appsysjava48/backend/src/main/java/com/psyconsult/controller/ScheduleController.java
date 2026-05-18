package com.psyconsult.controller;

import com.psyconsult.common.Result;
import com.psyconsult.entity.Schedule;
import com.psyconsult.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@Api(tags = "日程管理")
@CrossOrigin
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/counselor/{counselorId}/date/{date}")
    @ApiOperation("获取咨询师指定日期的日程")
    public Result<List<Schedule>> getSchedulesByDate(
            @PathVariable Long counselorId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return Result.success(scheduleService.getSchedulesByCounselorAndDate(counselorId, date));
    }

    @GetMapping("/counselor/{counselorId}/range")
    @ApiOperation("获取咨询师日期范围内的日程")
    public Result<List<Schedule>> getSchedulesByDateRange(
            @PathVariable Long counselorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.success(scheduleService.getSchedulesByDateRange(counselorId, startDate, endDate));
    }

    @GetMapping("/counselor/{counselorId}/available/{date}")
    @ApiOperation("获取咨询师指定日期的可预约日程")
    public Result<List<Schedule>> getAvailableSchedules(
            @PathVariable Long counselorId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return Result.success(scheduleService.getAvailableSchedules(counselorId, date));
    }

    @PostMapping
    @ApiOperation("创建日程排班")
    public Result<Schedule> createSchedule(@RequestBody Schedule schedule) {
        try {
            return Result.success(scheduleService.createSchedule(schedule));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("更新日程排班")
    public Result<Schedule> updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) {
        try {
            return Result.success(scheduleService.updateSchedule(id, schedule));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除日程排班")
    public Result<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("获取日程详情")
    public Result<Schedule> getScheduleById(@PathVariable Long id) {
        return Result.success(scheduleService.getScheduleById(id).orElse(null));
    }
}
