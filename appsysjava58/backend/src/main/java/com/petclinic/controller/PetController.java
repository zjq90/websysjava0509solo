package com.petclinic.controller;

import com.petclinic.dto.Result;
import com.petclinic.entity.Pet;
import com.petclinic.repository.PetRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宠物管理控制器
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
@Tag(name = "宠物管理", description = "宠物信息管理相关接口")
public class PetController {

    private final PetRepository petRepository;

    @GetMapping("/list/{userId}")
    @Operation(summary = "获取用户宠物列表", description = "根据用户ID获取其所有宠物")
    public Result<List<Pet>> getPetList(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<Pet> pets = petRepository.findByUserIdAndDeletedFalse(userId);
        return Result.success(pets);
    }

    @GetMapping("/{petId}")
    @Operation(summary = "获取宠物详情", description = "根据宠物ID获取详细信息")
    public Result<Pet> getPetDetail(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        return petRepository.findById(petId)
                .map(Result::success)
                .orElse(Result.error("宠物不存在"));
    }

    @PostMapping
    @Operation(summary = "添加宠物", description = "创建新的宠物档案")
    public Result<Pet> addPet(@RequestBody Pet pet) {
        Pet saved = petRepository.save(pet);
        return Result.success("添加成功", saved);
    }

    @PutMapping("/{petId}")
    @Operation(summary = "更新宠物信息", description = "更新宠物的基本信息")
    public Result<Pet> updatePet(
            @Parameter(description = "宠物ID") @PathVariable Long petId,
            @RequestBody Pet pet) {
        return petRepository.findById(petId)
                .map(existing -> {
                    pet.setId(petId);
                    Pet saved = petRepository.save(pet);
                    return Result.success("更新成功", saved);
                })
                .orElse(Result.error("宠物不存在"));
    }

    @DeleteMapping("/{petId}")
    @Operation(summary = "删除宠物", description = "逻辑删除宠物档案")
    public Result<Object> deletePet(
            @Parameter(description = "宠物ID") @PathVariable Long petId) {
        return petRepository.findById(petId)
                .map(pet -> {
                    pet.setDeleted(true);
                    petRepository.save(pet);
                    return Result.success();
                })
                .orElse(Result.error("宠物不存在"));
    }
}