package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.AuthenticationRequest;
import com.heritage.entity.Expert;
import com.heritage.repository.AuthenticationRequestRepository;
import com.heritage.repository.ExpertRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 鉴定服务Controller
 * 
 * @author Heritage Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/authentication")
@Tag(name = "鉴定服务", description = "文物鉴定申请、专家匹配相关接口")
public class AuthenticationController {

    @Autowired
    private AuthenticationRequestRepository authRepository;

    @Autowired
    private ExpertRepository expertRepository;

    /**
     * 获取专家列表
     */
    @GetMapping("/experts")
    @Operation(summary = "获取认证专家列表")
    public Result<List<Expert>> getExpertList(
            @RequestParam(required = false) Integer category) {
        List<Expert> experts;
        if (category != null) {
            experts = expertRepository.findBySpecialtiesContainingAndStatus(String.valueOf(category), 1);
        } else {
            experts = expertRepository.findByStatus(1);
        }
        return Result.success(experts);
    }

    /**
     * 获取专家详情
     */
    @GetMapping("/expert/{id}")
    @Operation(summary = "获取专家详情")
    public Result<Expert> getExpertDetail(@PathVariable Long id) {
        Optional<Expert> expert = expertRepository.findById(id);
        return expert.map(Result::success)
                .orElse(Result.error("专家不存在"));
    }

    /**
     * 提交鉴定申请
     */
    @PostMapping("/request")
    @Operation(summary = "提交鉴定申请")
    public Result<AuthenticationRequest> submitRequest(@RequestBody AuthenticationRequest request) {
        // 生成申请单号
        String requestNo = "JD" + System.currentTimeMillis();
        request.setRequestNo(requestNo);
        request.setStatus(0); // 待审核
        AuthenticationRequest saved = authRepository.save(request);
        return Result.success("申请提交成功", saved);
    }

    /**
     * 获取我的鉴定申请列表
     */
    @GetMapping("/my-requests")
    @Operation(summary = "获取我的鉴定申请列表")
    public Result<Page<AuthenticationRequest>> getMyRequests(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<AuthenticationRequest> result = authRepository.findByUserId(userId, pageRequest);
        return Result.success(result);
    }

    /**
     * 获取鉴定申请详情
     */
    @GetMapping("/request/{id}")
    @Operation(summary = "获取鉴定申请详情")
    public Result<AuthenticationRequest> getRequestDetail(@PathVariable Long id) {
        Optional<AuthenticationRequest> request = authRepository.findById(id);
        return request.map(Result::success)
                .orElse(Result.error("申请不存在"));
    }

    /**
     * 匹配专家
     */
    @PostMapping("/match-expert")
    @Operation(summary = "为鉴定申请匹配专家")
    public Result<Void> matchExpert(
            @RequestParam Long requestId,
            @RequestParam Long expertId) {
        Optional<AuthenticationRequest> requestOpt = authRepository.findById(requestId);
        if (!requestOpt.isPresent()) {
            return Result.error("申请不存在");
        }
        Optional<Expert> expertOpt = expertRepository.findById(expertId);
        if (!expertOpt.isPresent()) {
            return Result.error("专家不存在");
        }
        AuthenticationRequest request = requestOpt.get();
        request.setExpertId(expertId);
        request.setStatus(1); // 已匹配专家
        authRepository.save(request);
        return Result.success("专家匹配成功");
    }

    /**
     * 提交鉴定结果
     */
    @PostMapping("/submit-result")
    @Operation(summary = "提交鉴定结果")
    public Result<Void> submitResult(
            @RequestParam Long requestId,
            @RequestParam Integer result,
            @RequestParam String opinion,
            @RequestParam(required = false) java.math.BigDecimal estimatedValue) {
        Optional<AuthenticationRequest> requestOpt = authRepository.findById(requestId);
        if (!requestOpt.isPresent()) {
            return Result.error("申请不存在");
        }
        AuthenticationRequest request = requestOpt.get();
        request.setResult(result);
        request.setExpertOpinion(opinion);
        request.setEstimatedValue(estimatedValue);
        request.setStatus(3); // 已完成
        authRepository.save(request);
        return Result.success("鉴定结果已提交");
    }

    /**
     * 取消鉴定申请
     */
    @DeleteMapping("/request/{id}")
    @Operation(summary = "取消鉴定申请")
    public Result<Void> cancelRequest(@PathVariable Long id) {
        Optional<AuthenticationRequest> requestOpt = authRepository.findById(id);
        if (!requestOpt.isPresent()) {
            return Result.error("申请不存在");
        }
        AuthenticationRequest request = requestOpt.get();
        if (request.getStatus() >= 2) {
            return Result.error("鉴定已开始，无法取消");
        }
        request.setStatus(5); // 已取消
        authRepository.save(request);
        return Result.success("申请已取消");
    }
}