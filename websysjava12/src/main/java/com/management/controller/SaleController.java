package com.management.controller;

import com.management.entity.Customer;
import com.management.entity.Product;
import com.management.entity.Sale;
import com.management.entity.SaleItem;
import com.management.service.CustomerService;
import com.management.service.ProductService;
import com.management.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 销售控制器
 * 提供销售单的增删改查功能
 */
@Controller
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String list(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {

        List<Sale> sales;
        if (startDate != null && endDate != null) {
            LocalDateTime start = LocalDateTime.of(startDate, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(endDate, LocalTime.MAX);
            sales = saleService.findByDateRange(start, end);
        } else {
            sales = saleService.findAll();
        }

        model.addAttribute("sales", sales);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "sale/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        Sale sale = new Sale();
        sale.setSaleItems(new ArrayList<>());

        SaleItem item = new SaleItem();
        item.setQuantity(1);
        sale.getSaleItems().add(item);

        model.addAttribute("sale", sale);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("customers", customerService.findAll());
        return "sale/form";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<Sale> saleOpt = saleService.findById(id);
        if (saleOpt.isPresent()) {
            model.addAttribute("sale", saleOpt.get());
            return "sale/detail";
        }
        return "redirect:/sales";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute Sale sale,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) List<Long> productIds,
            @RequestParam(required = false) List<Integer> quantities,
            RedirectAttributes redirectAttributes) {

        if (customerId != null) {
            Optional<Customer> customerOpt = customerService.findById(customerId);
            customerOpt.ifPresent(sale::setCustomer);
        }

        List<SaleItem> saleItems = new ArrayList<>();
        if (productIds != null && quantities != null) {
            for (int i = 0; i < productIds.size(); i++) {
                Long productId = productIds.get(i);
                Integer quantity = quantities.get(i);

                if (productId != null && quantity != null && quantity > 0) {
                    Optional<Product> productOpt = productService.findById(productId);
                    if (productOpt.isPresent()) {
                        SaleItem item = new SaleItem();
                        item.setProduct(productOpt.get());
                        item.setQuantity(quantity);
                        saleItems.add(item);
                    }
                }
            }
        }
        sale.setSaleItems(saleItems);

        saleService.save(sale);
        redirectAttributes.addFlashAttribute("message", "销售单创建成功！");
        return "redirect:/sales";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        saleService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "销售单删除成功！");
        return "redirect:/sales";
    }
}
