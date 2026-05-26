package com.accounting.controller;

import com.accounting.dto.ApiResponse;
import com.accounting.dto.BillDTO;
import com.accounting.entity.Bill;
import com.accounting.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
@Tag(name = "账单管理", description = "账单的增删改查接口")
public class BillController {

    private final BillService billService;

    @GetMapping
    @Operation(summary = "获取全部账单列表", description = "获取所有未删除的账单，按交易时间倒序排列")
    public ApiResponse<List<Bill>> getAllBills() {
        log.info("API: 获取全部账单列表");
        return ApiResponse.success(billService.getAllBills());
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取账单", description = "根据账单ID获取账单详情")
    public ApiResponse<Bill> getBillById(
            @Parameter(description = "账单ID", required = true)
            @PathVariable Long id) {
        log.info("API: 根据ID获取账单: {}", id);
        return ApiResponse.success(billService.getBillById(id));
    }

    @GetMapping("/recent/{days}")
    @Operation(summary = "获取最近N天的账单", description = "获取指定天数内的账单，支持Redis缓存")
    public ApiResponse<List<Bill>> getRecentBills(
            @Parameter(description = "天数", required = true, example = "30")
            @PathVariable Integer days) {
        log.info("API: 获取最近{}天的账单", days);
        return ApiResponse.success(billService.getRecentBills(days));
    }

    @GetMapping("/range")
    @Operation(summary = "获取指定时间范围的账单", description = "根据开始和结束日期获取账单列表")
    public ApiResponse<List<Bill>> getBillsByDateRange(
            @Parameter(description = "开始日期", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期", required = true)
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        log.info("API: 获取指定时间范围的账单: {} - {}", startDate, endDate);
        LocalDateTime start = billService.getStartOfDay(startDate);
        LocalDateTime end = billService.getEndOfDay(endDate);
        return ApiResponse.success(billService.getBillsByDateRange(start, end));
    }

    @PostMapping
    @Operation(summary = "创建账单", description = "创建一条新的账单记录，同时更新账户余额")
    public ApiResponse<Bill> createBill(
            @Valid @RequestBody BillDTO billDTO) {
        log.info("API: 创建账单: {}", billDTO);
        return ApiResponse.success("账单创建成功", billService.createBill(billDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新账单", description = "更新账单信息，同时调整账户余额")
    public ApiResponse<Bill> updateBill(
            @Parameter(description = "账单ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody BillDTO billDTO) {
        log.info("API: 更新账单: {}", id);
        return ApiResponse.success("账单更新成功", billService.updateBill(id, billDTO));
    }

    @PatchMapping("/{id}/category")
    @Operation(summary = "修改账单分类", description = "仅修改账单的分类，用于左滑快速修改")
    public ApiResponse<Bill> updateBillCategory(
            @Parameter(description = "账单ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "新的分类ID", required = true)
            @RequestParam Long categoryId) {
        log.info("API: 修改账单分类: billId={}, categoryId={}", id, categoryId);
        return ApiResponse.success("分类更新成功", billService.updateBillCategory(id, categoryId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除账单", description = "软删除账单，同时回滚账户余额")
    public ApiResponse<Void> deleteBill(
            @Parameter(description = "账单ID", required = true)
            @PathVariable Long id) {
        log.info("API: 删除账单: {}", id);
        billService.deleteBill(id);
        return ApiResponse.success("账单删除成功", null);
    }

    @PostMapping("/sync")
    @Operation(summary = "批量同步账单", description = "离线数据同步接口，支持批量导入账单")
    public ApiResponse<List<Bill>> syncBills(
            @RequestBody List<BillDTO> billDTOs) {
        log.info("API: 批量同步账单, 数量: {}", billDTOs.size());
        return ApiResponse.success("同步完成", billService.syncBills(billDTOs));
    }

    @GetMapping("/pending")
    @Operation(summary = "获取待同步账单", description = "获取所有待同步和同步失败的账单")
    public ApiResponse<List<Bill>> getPendingSyncBills() {
        log.info("API: 获取待同步账单");
        return ApiResponse.success(billService.getPendingSyncBills());
    }
}
