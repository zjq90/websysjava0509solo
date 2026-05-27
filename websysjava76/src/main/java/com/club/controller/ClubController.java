package com.club.controller;

import com.club.common.Result;
import com.club.dto.PageQuery;
import com.club.entity.Club;
import com.club.entity.ClubApplication;
import com.club.entity.AnnualRegistration;
import com.club.entity.ViolationRecord;
import com.club.service.ClubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 社团管理控制器
 * 提供社团CRUD、成立申请审核、年度注册、违规处理等API接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/club")
@Tag(name = "社团管理", description = "社团审核与管理相关接口")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    // ==================== 社团基本信息管理 ====================

    @GetMapping("/list")
    @Operation(summary = "分页查询社团列表")
    public Result<Page<Club>> getClubList(PageQuery query) {
        return Result.success(clubService.getClubList(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询社团详情")
    public Result<Club> getClubById(@PathVariable Long id) {
        return Result.success(clubService.getClubById(id));
    }

    @PostMapping
    @Operation(summary = "新增社团")
    public Result<Club> createClub(@RequestBody Club club) {
        try {
            return Result.success(clubService.createClub(club));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新社团信息")
    public Result<Club> updateClub(@PathVariable Long id, @RequestBody Club club) {
        try {
            return Result.success(clubService.updateClub(id, club));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除社团")
    public Result<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取社团统计数据")
    public Result<Map<String, Object>> getClubStatistics() {
        return Result.success(clubService.getClubStatistics());
    }

    // ==================== 社团成立申请管理 ====================

    @GetMapping("/application/list")
    @Operation(summary = "分页查询社团成立申请列表")
    public Result<Page<ClubApplication>> getApplicationList(PageQuery query) {
        return Result.success(clubService.getApplicationList(query));
    }

    @GetMapping("/application/{id}")
    @Operation(summary = "根据ID查询申请详情")
    public Result<ClubApplication> getApplicationById(@PathVariable Long id) {
        return Result.success(clubService.getApplicationById(id));
    }

    @PostMapping("/application/{id}/approve")
    @Operation(summary = "审核社团成立申请")
    public Result<ClubApplication> approveApplication(
            @PathVariable Long id,
            @Parameter(description = "是否通过") @RequestParam boolean approved,
            @Parameter(description = "审核意见") @RequestParam String opinion,
            @Parameter(description = "审核人ID") @RequestParam(required = false) Long approverId,
            @Parameter(description = "审核人姓名") @RequestParam(required = false) String approverName) {
        try {
            return Result.success(clubService.approveApplication(id, approved, opinion, approverId, approverName));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 年度注册管理 ====================

    @GetMapping("/registration/list")
    @Operation(summary = "分页查询年度注册申请列表")
    public Result<Page<AnnualRegistration>> getAnnualRegistrationList(PageQuery query) {
        return Result.success(clubService.getAnnualRegistrationList(query));
    }

    @PostMapping("/registration/{id}/approve")
    @Operation(summary = "审核年度注册")
    public Result<AnnualRegistration> approveAnnualRegistration(
            @PathVariable Long id,
            @Parameter(description = "是否通过") @RequestParam boolean approved,
            @Parameter(description = "审核意见") @RequestParam String opinion,
            @Parameter(description = "审核人ID") @RequestParam(required = false) Long approverId,
            @Parameter(description = "审核人姓名") @RequestParam(required = false) String approverName) {
        try {
            return Result.success(clubService.approveAnnualRegistration(id, approved, opinion, approverId, approverName));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ==================== 违规处理管理 ====================

    @GetMapping("/violation/list")
    @Operation(summary = "分页查询违规记录列表")
    public Result<Page<ViolationRecord>> getViolationRecordList(PageQuery query) {
        return Result.success(clubService.getViolationRecordList(query));
    }

    @PostMapping("/violation")
    @Operation(summary = "创建违规记录并处理社团")
    public Result<ViolationRecord> handleViolation(
            @RequestBody ViolationRecord record,
            @Parameter(description = "处理人ID") @RequestParam(required = false) Long handlerId,
            @Parameter(description = "处理人姓名") @RequestParam(required = false) String handlerName) {
        return Result.success(clubService.handleViolation(record, handlerId, handlerName));
    }

    @PostMapping("/violation/{id}/rectify")
    @Operation(summary = "标记违规已整改")
    public Result<ViolationRecord> markRectified(
            @PathVariable Long id,
            @Parameter(description = "整改说明") @RequestParam(required = false) String note) {
        try {
            return Result.success(clubService.markRectified(id, note));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/pending/counts")
    @Operation(summary = "获取待处理数量统计")
    public Result<Map<String, Long>> getPendingCounts() {
        return Result.success(clubService.getPendingCounts());
    }

    @GetMapping("/all")
    @Operation(summary = "获取所有社团列表（不分页）")
    public Result<List<Club>> getAllClubs() {
        return Result.success(clubService.getAllClubs());
    }

    @PostMapping("/violation/{id}/handle")
    @Operation(summary = "处理违规记录")
    public Result<ViolationRecord> handleViolationRecord(
            @PathVariable Long id,
            @Parameter(description = "处理措施") @RequestParam String action,
            @Parameter(description = "处理意见") @RequestParam String handlerOpinion,
            @Parameter(description = "处理人ID") @RequestParam(required = false) Long handlerId,
            @Parameter(description = "处理人姓名") @RequestParam(required = false) String handlerName) {
        try {
            return Result.success(clubService.handleViolationRecord(id, action, handlerOpinion, handlerId, handlerName));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/violation/{id}")
    @Operation(summary = "删除违规记录")
    public Result<Void> deleteViolationRecord(@PathVariable Long id) {
        clubService.deleteViolationRecord(id);
        return Result.success();
    }
}
