package com.breeding.controller;

import com.breeding.entity.SoilData;
import com.breeding.service.SoilDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 土壤数据控制器
 */
@Controller
@RequestMapping("/soil")
public class SoilDataController {

    @Autowired
    private SoilDataService soilService;

    @GetMapping
    public String listPage(Model model, 
                           @RequestParam(required = false) String location,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<SoilData> soilPage;
        if (location != null && !location.trim().isEmpty()) {
            List<SoilData> allResults = soilService.findByLocation(location);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allResults.size());
            List<SoilData> pageContent = start > allResults.size() ? 
                new ArrayList<>() : allResults.subList(start, end);
            soilPage = new PageImpl<>(pageContent, pageable, allResults.size());
        } else {
            soilPage = soilService.findAll(pageable);
        }
        model.addAttribute("page", soilPage);
        model.addAttribute("soilList", soilPage.getContent());
        model.addAttribute("location", location);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", soilPage.getTotalPages());
        model.addAttribute("totalItems", soilPage.getTotalElements());
        return "soil/list";
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("soil", new SoilData());
        return "soil/form";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        return soilService.findById(id)
                .map(soil -> {
                    model.addAttribute("soil", soil);
                    return "soil/form";
                })
                .orElse("redirect:/soil");
    }

    @GetMapping("/api")
    @ResponseBody
    public List<SoilData> getAll() {
        return soilService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<SoilData> getById(@PathVariable Long id) {
        return soilService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody SoilData soil) {
        try {
            SoilData saved = soilService.save(soil);
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SoilData soil) {
        try {
            SoilData updated = soilService.update(id, soil);
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
        boolean deleted = soilService.delete(id);
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
    public Map<String, Object> analyze(@RequestParam String location) {
        return soilService.analyzeSoilQuality(location);
    }
}
