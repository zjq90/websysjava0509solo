package com.breeding.controller;

import com.breeding.entity.WeatherData;
import com.breeding.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 气象数据控制器
 */
@Controller
@RequestMapping("/weather")
public class WeatherDataController {

    @Autowired
    private WeatherDataService weatherService;

    @GetMapping
    public String listPage(Model model, 
                           @RequestParam(required = false) String location,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<WeatherData> weatherPage;
        if (location != null && !location.trim().isEmpty()) {
            List<WeatherData> allResults = weatherService.findByLocation(location);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allResults.size());
            List<WeatherData> pageContent = start > allResults.size() ? 
                new ArrayList<>() : allResults.subList(start, end);
            weatherPage = new PageImpl<>(pageContent, pageable, allResults.size());
        } else {
            weatherPage = weatherService.findAll(pageable);
        }
        model.addAttribute("page", weatherPage);
        model.addAttribute("weatherList", weatherPage.getContent());
        model.addAttribute("location", location);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", weatherPage.getTotalPages());
        model.addAttribute("totalItems", weatherPage.getTotalElements());
        return "weather/list";
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("weather", new WeatherData());
        return "weather/form";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        return weatherService.findById(id)
                .map(weather -> {
                    model.addAttribute("weather", weather);
                    return "weather/form";
                })
                .orElse("redirect:/weather");
    }

    @GetMapping("/api")
    @ResponseBody
    public List<WeatherData> getAll() {
        return weatherService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<WeatherData> getById(@PathVariable Long id) {
        return weatherService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody WeatherData weather) {
        try {
            WeatherData saved = weatherService.save(weather);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", saved);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody WeatherData weather) {
        try {
            WeatherData updated = weatherService.update(id, weather);
            if (updated != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("data", updated);
                return ResponseEntity.ok(result);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean deleted = weatherService.delete(id);
        Map<String, Object> result = new HashMap<>();
        if (deleted) {
            result.put("success", true);
            result.put("message", "删除成功");
            return ResponseEntity.ok(result);
        }
        result.put("success", false);
        result.put("message", "记录不存在");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/api/analyze")
    @ResponseBody
    public Map<String, Object> analyze(
            @RequestParam String location,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return weatherService.analyzeWeatherImpact(location, startDate, endDate);
    }
}
