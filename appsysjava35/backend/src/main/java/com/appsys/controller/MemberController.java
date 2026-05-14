package com.appsys.controller;

import com.appsys.common.Result;
import com.appsys.entity.MemberLevel;
import com.appsys.entity.User;
import com.appsys.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@Tag(name = "会员管理", description = "会员等级和权益相关接口")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/levels")
    @Operation(summary = "获取所有会员等级")
    public Result<List<MemberLevel>> getAllMemberLevels() {
        return Result.success(memberService.getAllMemberLevels());
    }

    @GetMapping("/level/{id}")
    @Operation(summary = "根据ID获取会员等级")
    public Result<MemberLevel> getMemberLevelById(@PathVariable Long id) {
        MemberLevel level = memberService.getMemberLevelById(id);
        if (level != null) {
            return Result.success(level);
        }
        return Result.error("会员等级不存在");
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户会员信息")
    public Result<User> getUserWithMemberLevel(@PathVariable Long userId) {
        User user = memberService.getUserWithMemberLevel(userId);
        if (user != null) {
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }

    @PostMapping("/upgrade")
    @Operation(summary = "增加成长值并自动升级会员")
    public Result<Void> upgradeMemberLevel(@RequestParam Long userId, @RequestParam Integer growth) {
        memberService.upgradeMemberLevel(userId, growth);
        return Result.success("成长值已增加");
    }
}
