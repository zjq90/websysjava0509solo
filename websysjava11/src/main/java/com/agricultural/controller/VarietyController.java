package com.agricultural.controller;

import com.agricultural.entity.Variety;
import com.agricultural.service.VarietyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 品种管理控制器
 */
@Controller
@RequestMapping("/variety")
public class VarietyController {

    private final VarietyService varietyService;

    public VarietyController(VarietyService varietyService) {
        this.varietyService = varietyService;
    }

    @GetMapping
    public String list(@RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int size,
                       Model model) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Variety> varietyPage = varietyService.findAll(pageRequest);
        model.addAttribute("varieties", varietyPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", varietyPage.getTotalPages());
        model.addAttribute("totalItems", varietyPage.getTotalElements());
        model.addAttribute("size", size);
        return "variety/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("variety", new Variety());
        return "variety/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<Variety> variety = varietyService.findById(id);
        if (!variety.isPresent()) {
            attributes.addFlashAttribute("error", "品种不存在");
            return "redirect:/variety";
        }
        model.addAttribute("variety", variety.get());
        return "variety/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Variety variety, RedirectAttributes attributes) {
        try {
            varietyService.save(variety);
            attributes.addFlashAttribute("success", "保存成功");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/variety";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            varietyService.deleteById(id);
            attributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/variety";
    }

    @GetMapping("/toggle/{id}")
    @ResponseBody
    public String toggleEnabled(@PathVariable Long id) {
        boolean enabled = varietyService.toggleEnabled(id);
        return "{\"success\":true,\"enabled\":" + enabled + "}";
    }
}
