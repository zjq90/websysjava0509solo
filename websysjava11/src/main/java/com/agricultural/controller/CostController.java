package com.agricultural.controller;

import com.agricultural.entity.CostRecord;
import com.agricultural.entity.enums.CostCategory;
import com.agricultural.service.CostService;
import com.agricultural.service.CustomerService;
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
 * 成本管理控制器
 */
@Controller
@RequestMapping("/cost")
public class CostController {

    private final CostService costService;
    private final VarietyService varietyService;
    private final CustomerService customerService;

    public CostController(CostService costService,
                          VarietyService varietyService,
                          CustomerService customerService) {
        this.costService = costService;
        this.varietyService = varietyService;
        this.customerService = customerService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String category,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int size,
                       Model model) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<CostRecord> costPage;
        if (category != null && !category.isEmpty()) {
            model.addAttribute("costRecords", costService.findByCategory(CostCategory.valueOf(category)));
            model.addAttribute("isFiltered", true);
        } else {
            costPage = costService.findAll(pageRequest);
            model.addAttribute("costRecords", costPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", costPage.getTotalPages());
            model.addAttribute("totalItems", costPage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("isFiltered", false);
        }
        model.addAttribute("selectedCategory", category);
        model.addAttribute("categories", CostCategory.values());
        model.addAttribute("costSummary", costService.getCostSummary());
        model.addAttribute("totalAll", costService.getTotalAll());
        return "cost/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<CostRecord> record = costService.findById(id);
        if (!record.isPresent()) {
            attributes.addFlashAttribute("error", "成本记录不存在");
            return "redirect:/cost";
        }
        model.addAttribute("costRecord", record.get());
        return "cost/view";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        CostRecord record = new CostRecord();
        model.addAttribute("costRecord", record);
        model.addAttribute("costNo", costService.generateCostNo());
        model.addAttribute("categories", CostCategory.values());
        model.addAttribute("varieties", varietyService.findAllEnabled());
        model.addAttribute("suppliers", customerService.findByType("SUPPLIER"));
        return "cost/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<CostRecord> record = costService.findById(id);
        if (!record.isPresent()) {
            attributes.addFlashAttribute("error", "成本记录不存在");
            return "redirect:/cost";
        }
        model.addAttribute("costRecord", record.get());
        model.addAttribute("categories", CostCategory.values());
        model.addAttribute("varieties", varietyService.findAllEnabled());
        model.addAttribute("suppliers", customerService.findByType("SUPPLIER"));
        return "cost/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute CostRecord costRecord, RedirectAttributes attributes) {
        try {
            costService.save(costRecord);
            attributes.addFlashAttribute("success", "保存成功");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/cost";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            costService.deleteById(id);
            attributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/cost";
    }
}
