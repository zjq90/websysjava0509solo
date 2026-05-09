package com.breeding.controller;

import com.breeding.entity.FieldExperiment;
import com.breeding.service.FieldExperimentService;
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
 * 田间试验控制器
 */
@Controller
@RequestMapping("/experiments")
public class FieldExperimentController {

    @Autowired
    private FieldExperimentService experimentService;

    @GetMapping
    public String listPage(Model model, 
                           @RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<FieldExperiment> experimentPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<FieldExperiment> allResults = experimentService.search(keyword);
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), allResults.size());
            List<FieldExperiment> pageContent = start > allResults.size() ? 
                new ArrayList<>() : allResults.subList(start, end);
            experimentPage = new PageImpl<>(pageContent, pageable, allResults.size());
        } else {
            experimentPage = experimentService.findAll(pageable);
        }
        model.addAttribute("page", experimentPage);
        model.addAttribute("experiments", experimentPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", experimentPage.getTotalPages());
        model.addAttribute("totalItems", experimentPage.getTotalElements());
        return "experiment/list";
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("experiment", new FieldExperiment());
        return "experiment/form";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        return experimentService.findById(id)
                .map(experiment -> {
                    model.addAttribute("experiment", experiment);
                    return "experiment/form";
                })
                .orElse("redirect:/experiments");
    }

    @GetMapping("/api")
    @ResponseBody
    public List<FieldExperiment> getAll() {
        return experimentService.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<FieldExperiment> getById(@PathVariable Long id) {
        return experimentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody FieldExperiment experiment) {
        try {
            FieldExperiment saved = experimentService.save(experiment);
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
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FieldExperiment experiment) {
        try {
            FieldExperiment updated = experimentService.update(id, experiment);
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
        boolean deleted = experimentService.delete(id);
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
}
