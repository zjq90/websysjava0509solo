package com.agricultural.controller;

import com.agricultural.entity.Finance;
import com.agricultural.entity.FinancePayment;
import com.agricultural.entity.enums.FinanceStatus;
import com.agricultural.entity.enums.FinanceType;
import com.agricultural.service.CustomerService;
import com.agricultural.service.FinanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * 财务账款管理控制器
 */
@Controller
@RequestMapping("/finance")
public class FinanceController {

    private final FinanceService financeService;
    private final CustomerService customerService;

    public FinanceController(FinanceService financeService,
                             CustomerService customerService) {
        this.financeService = financeService;
        this.customerService = customerService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String type,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int size,
                       Model model) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Finance> financePage;
        if (type != null && !type.isEmpty()) {
            model.addAttribute("finances", financeService.findByType(FinanceType.valueOf(type)));
            model.addAttribute("isFiltered", true);
        } else {
            financePage = financeService.findAll(pageRequest);
            model.addAttribute("finances", financePage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", financePage.getTotalPages());
            model.addAttribute("totalItems", financePage.getTotalElements());
            model.addAttribute("size", size);
            model.addAttribute("isFiltered", false);
        }
        model.addAttribute("selectedType", type);
        return "finance/list";
    }

    @GetMapping("/receivable")
    public String receivableList(Model model) {
        model.addAttribute("finances", financeService.findByType(FinanceType.RECEIVABLE));
        model.addAttribute("selectedType", "RECEIVABLE");
        return "finance/list";
    }

    @GetMapping("/payable")
    public String payableList(Model model) {
        model.addAttribute("finances", financeService.findByType(FinanceType.PAYABLE));
        model.addAttribute("selectedType", "PAYABLE");
        return "finance/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<Finance> finance = financeService.findById(id);
        if (!finance.isPresent()) {
            attributes.addFlashAttribute("error", "账款不存在");
            return "redirect:/finance";
        }
        model.addAttribute("finance", finance.get());
        model.addAttribute("payments", financeService.findPaymentsByFinanceId(id));
        return "finance/view";
    }

    @GetMapping("/add")
    public String addForm(@RequestParam String type, Model model) {
        Finance finance = new Finance();
        finance.setFinanceType(FinanceType.valueOf(type));
        finance.setStatus(FinanceStatus.UNSETTLED);
        model.addAttribute("finance", finance);
        model.addAttribute("financeNo", financeService.generateFinanceNo(FinanceType.valueOf(type)));
        model.addAttribute("customers", customerService.findAllEnabled());
        return "finance/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Finance finance, RedirectAttributes attributes) {
        try {
            financeService.save(finance);
            attributes.addFlashAttribute("success", "保存成功");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "保存失败：" + e.getMessage());
        }
        return "redirect:/finance?type=" + finance.getFinanceType();
    }

    @GetMapping("/payment/{id}")
    public String paymentForm(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        Optional<Finance> finance = financeService.findById(id);
        if (!finance.isPresent()) {
            attributes.addFlashAttribute("error", "账款不存在");
            return "redirect:/finance";
        }
        model.addAttribute("finance", finance.get());
        model.addAttribute("payment", new FinancePayment());
        return "finance/payment";
    }

    @PostMapping("/payment/{id}")
    public String makePayment(@PathVariable Long id, @ModelAttribute FinancePayment payment,
                               RedirectAttributes attributes) {
        try {
            financeService.makePayment(id, payment);
            attributes.addFlashAttribute("success", "收付款操作成功");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "操作失败：" + e.getMessage());
        }
        return "redirect:/finance/view/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            financeService.deleteById(id);
            attributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/finance";
    }
}
