package com.flowerstore.backend.controller;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.MemberLevel;
import com.flowerstore.backend.entity.PointRecord;
import com.flowerstore.backend.service.MemberService;
import com.flowerstore.backend.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 会员控制器
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/member")
@Tag(name = "会员接口", description = "会员等级、积分管理")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 获取会员等级列表
     */
    @GetMapping("/levels")
    @Operation(summary = "获取会员等级", description = "获取所有会员等级配置")
    public Result<List<MemberLevel>> getMemberLevels() {
        return memberService.getMemberLevels();
    }

    /**
     * 获取用户会员信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取会员信息", description = "获取当前用户的会员信息")
    public Result<Map<String, Object>> getMemberInfo(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return memberService.getUserMemberInfo(userId);
    }

    /**
     * 获取积分记录
     */
    @GetMapping("/points")
    @Operation(summary = "获取积分记录", description = "获取用户的积分变更记录")
    public Result<List<PointRecord>> getPointRecords(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return memberService.getPointRecords(userId);
    }

    /**
     * 积分兑换礼品
     */
    @PostMapping("/exchange")
    @Operation(summary = "积分兑换", description = "使用积分兑换礼品")
    public Result<String> exchangeGift(
            @RequestParam Integer points,
            @RequestParam String giftName,
            HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        if (userId == null) {
            return Result.error("未登录或token已过期");
        }
        return memberService.exchangeGift(userId, points, giftName);
    }

    /**
     * 从token中获取用户ID
     */
    private Long getUserIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}
