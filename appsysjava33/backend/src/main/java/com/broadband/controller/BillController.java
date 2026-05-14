package com.broadband.controller;

import com.broadband.common.Result;
import com.broadband.entity.Bill;
import com.broadband.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账单控制器
 * 处理账单管理、支付等接口
 * 
 * @author broadband
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/bill")
@Tag(name = "账单管理", description = "账单查询、支付等接口")
public class BillController {

    @Autowired
    private BillService billService;

    @Operation(summary = "获取用户账单列表", description = "获取指定用户的所有账单")
    @GetMapping("/user/{userId}")
    public Result<List<Bill>> getUserBills(@PathVariable Long userId) {
        try {
            List<Bill> bills = billService.getUserBills(userId);
            return Result.success(bills);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "按状态获取用户账单", description = "0-待支付 1-已支付 2-已逾期")
    @GetMapping("/user/{userId}/status/{status}")
    public Result<List<Bill>> getUserBillsByStatus(@PathVariable Long userId, @PathVariable Integer status) {
        try {
            List<Bill> bills = billService.getUserBillsByStatus(userId, status);
            return Result.success(bills);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "获取账单详情", description = "根据账单ID获取详细信息")
    @GetMapping("/{billId}")
    public Result<Bill> getBillById(@PathVariable Long billId) {
        try {
            Bill bill = billService.getBillById(billId);
            return Result.success(bill);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "支付账单", description = "支付指定金额的账单")
    @PostMapping("/{billId}/pay")
    public Result<Bill> payBill(@PathVariable Long billId, @RequestParam BigDecimal amount) {
        try {
            Bill bill = billService.payBill(billId, amount);
            return Result.success("支付成功", bill);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
