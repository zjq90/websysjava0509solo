package com.sales.controller;

import com.sales.entity.Customer;
import com.sales.entity.CustomerLevel;
import com.sales.entity.CustomerType;
import com.sales.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * 客户管理控制器
 * 处理客户档案管理的请求，支持分级与标签化
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 客户列表页面
     */
    @GetMapping
    public String list(Model model,
                      @RequestParam(required = false) String keyword,
                      @RequestParam(required = false) CustomerType type,
                      @RequestParam(required = false) CustomerLevel level,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customerPage;

        if (keyword != null && !keyword.isEmpty()) {
            customerPage = customerService.searchByName(keyword, pageable);
        } else if (type != null) {
            customerPage = customerService.findByType(type, pageable);
        } else if (level != null) {
            customerPage = customerService.findByLevel(level, pageable);
        } else {
            customerPage = customerService.findAll(pageable);
        }

        model.addAttribute("customers", customerPage.getContent());
        model.addAttribute("page", customerPage);
        model.addAttribute("types", CustomerType.values());
        model.addAttribute("levels", CustomerLevel.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedLevel", level);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "customer/list";
    }

    /**
     * 新增客户页面
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("types", CustomerType.values());
        model.addAttribute("levels", CustomerLevel.values());
        return "customer/form";
    }

    /**
     * 保存客户
     */
    @PostMapping
    public String save(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {
        try {
            customerService.save(customer);
            redirectAttributes.addFlashAttribute("success", "客户保存成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/customers";
    }

    /**
     * 编辑客户页面
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            model.addAttribute("customer", customer.get());
            model.addAttribute("types", CustomerType.values());
            model.addAttribute("levels", CustomerLevel.values());
            return "customer/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "客户不存在");
            return "redirect:/customers";
        }
    }

    /**
     * 查看客户详情
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            model.addAttribute("customer", customer.get());
            return "customer/detail";
        } else {
            redirectAttributes.addFlashAttribute("error", "客户不存在");
            return "redirect:/customers";
        }
    }

    /**
     * 删除客户
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.delete(id);
            redirectAttributes.addFlashAttribute("success", "客户删除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/customers";
    }
}
