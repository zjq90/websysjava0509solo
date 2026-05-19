package com.pethospital.controller;

import com.pethospital.common.Result;
import com.pethospital.entity.Pet;
import com.pethospital.entity.VaccineRecord;
import com.pethospital.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宠物档案管理Controller
 * 提供宠物信息管理、疫苗记录管理、到期提醒等API接口
 * 
 * @author Pet Hospital Team
 */
@RestController
@RequestMapping("/pets")
@Tag(name = "宠物档案管理", description = "宠物信息管理、疫苗记录、到期提醒等接口")
public class PetController {

    @Autowired
    private PetService petService;

    /**
     * 获取用户的所有宠物
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户宠物列表", description = "获取指定用户的所有宠物档案")
    public Result<List<Pet>> getUserPets(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(petService.getUserPets(userId));
    }

    /**
     * 获取宠物详情
     */
    @GetMapping("/{petId}")
    @Operation(summary = "获取宠物详情", description = "根据宠物ID获取宠物详细信息")
    public Result<Pet> getPetById(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        return Result.success(petService.getPetById(petId));
    }

    /**
     * 添加宠物
     */
    @PostMapping
    @Operation(summary = "添加宠物", description = "创建新的宠物档案")
    public Result<Pet> addPet(@RequestBody Pet pet) {
        return Result.success(petService.addPet(pet));
    }

    /**
     * 更新宠物信息
     */
    @PutMapping("/{petId}")
    @Operation(summary = "更新宠物信息", description = "修改宠物档案信息")
    public Result<Pet> updatePet(
            @Parameter(description = "宠物ID") @PathVariable Long petId,
            @RequestBody Pet pet) {
        pet.setId(petId);
        return Result.success(petService.updatePet(pet));
    }

    /**
     * 删除宠物
     */
    @DeleteMapping("/{petId}")
    @Operation(summary = "删除宠物", description = "删除指定宠物档案")
    public Result<Void> deletePet(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        petService.deletePet(petId);
        return Result.success();
    }

    /**
     * 获取宠物疫苗记录
     */
    @GetMapping("/{petId}/vaccines")
    @Operation(summary = "获取疫苗记录", description = "获取宠物的所有疫苗接种记录")
    public Result<List<VaccineRecord>> getPetVaccineRecords(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        return Result.success(petService.getPetVaccineRecords(petId));
    }

    /**
     * 添加疫苗记录
     */
    @PostMapping("/{petId}/vaccines")
    @Operation(summary = "添加疫苗记录", description = "为宠物添加新的疫苗接种记录")
    public Result<VaccineRecord> addVaccineRecord(
            @Parameter(description = "宠物ID") @PathVariable Long petId,
            @RequestBody VaccineRecord record) {
        record.setPetId(petId);
        return Result.success(petService.addVaccineRecord(record));
    }

    /**
     * 获取疫苗到期提醒
     */
    @GetMapping("/{petId}/vaccine-reminders")
    @Operation(summary = "获取疫苗到期提醒", description = "获取30天内即将到期的疫苗提醒")
    public Result<Map<String, Object>> getVaccineReminders(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        return Result.success(petService.getVaccineReminders(petId));
    }
}
