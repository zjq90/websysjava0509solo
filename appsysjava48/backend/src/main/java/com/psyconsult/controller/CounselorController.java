package com.psyconsult.controller;

import com.psyconsult.common.Result;
import com.psyconsult.entity.Counselor;
import com.psyconsult.repository.CounselorRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/counselor")
@Api(tags = "咨询师管理")
@CrossOrigin
public class CounselorController {

    @Autowired
    private CounselorRepository counselorRepository;

    @GetMapping
    @ApiOperation("获取所有咨询师")
    public Result<List<Counselor>> getAllCounselors() {
        return Result.success(counselorRepository.findByStatus(1));
    }

    @GetMapping("/senior")
    @ApiOperation("获取资深咨询师（督导）")
    public Result<List<Counselor>> getSeniorCounselors() {
        return Result.success(counselorRepository.findByIsSeniorAndStatus(true, 1));
    }

    @GetMapping("/search")
    @ApiOperation("搜索咨询师")
    public Result<List<Counselor>> searchCounselors(@RequestParam String keyword) {
        return Result.success(counselorRepository.findByNameContaining(keyword));
    }

    @PostMapping
    @ApiOperation("创建咨询师")
    public Result<Counselor> createCounselor(@RequestBody Counselor counselor) {
        return Result.success(counselorRepository.save(counselor));
    }

    @PutMapping("/{id}")
    @ApiOperation("更新咨询师信息")
    public Result<Counselor> updateCounselor(@PathVariable Long id, @RequestBody Counselor counselor) {
        return counselorRepository.findById(id).map(existing -> {
            existing.setName(counselor.getName());
            existing.setTitle(counselor.getTitle());
            existing.setQualification(counselor.getQualification());
            existing.setBio(counselor.getBio());
            existing.setExpertise(counselor.getExpertise());
            existing.setConsultationFee(counselor.getConsultationFee());
            existing.setExperienceYears(counselor.getExperienceYears());
            existing.setIsSenior(counselor.getIsSenior());
            existing.setAccepting(counselor.getAccepting());
            return Result.success(counselorRepository.save(existing));
        }).orElse(Result.error("咨询师不存在"));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取咨询师详情")
    public Result<Counselor> getCounselorById(@PathVariable Long id) {
        return Result.success(counselorRepository.findById(id).orElse(null));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除咨询师")
    public Result<Void> deleteCounselor(@PathVariable Long id) {
        counselorRepository.findById(id).ifPresent(counselor -> {
            counselor.setStatus(0);
            counselorRepository.save(counselor);
        });
        return Result.success();
    }
}
